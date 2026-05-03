package com.fitness.gymManagementSystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.fitness.gymManagementSystem.dto.CheckInRequest;
import com.fitness.gymManagementSystem.dto.CheckInResponse;
import com.fitness.gymManagementSystem.entity.CheckInType;
import com.fitness.gymManagementSystem.entity.CheckIn;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.CheckInRepository;
import com.fitness.gymManagementSystem.repository.GymClassRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;
import com.fitness.gymManagementSystem.repository.ClassBookingRepository;

@Service
public class CheckInService {
    
    private final CheckInRepository checkInRepository;
    private final MembershipService membershipService;
    private final GymClassRepository gymClassRepository;
    private final UserRepository userRepository;
    private final ClassBookingRepository classBookingRepository;

    public CheckInService(CheckInRepository checkInRepository, 
                        MembershipService membershipService, 
                        GymClassRepository gymClassRepository, 
                        UserRepository userRepository, 
                        ClassBookingRepository classBookingRepository) {
        this.checkInRepository = checkInRepository;
        this.membershipService = membershipService;
        this.gymClassRepository = gymClassRepository;
        this.userRepository = userRepository;
        this.classBookingRepository = classBookingRepository;
    }

    @Transactional
    public CheckInResponse processCheckIn(CheckInRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        CheckInType type = request.classId() == null ? CheckInType.GYM_ENTRY : CheckInType.CLASS_SESSION;
        GymClass targetClass = null;

        // LUỒNG 1: Check-in vào phòng tập chung
        if (type == CheckInType.GYM_ENTRY) {
            if (!membershipService.isUserActive(username)) {
                throw new IllegalStateException("Hội viên không có gói tập đang hoạt động.");
            }
        } 
        // LUỒNG 2: Check-in vào lớp tập cụ thể
        else {
            targetClass = gymClassRepository.findById(request.classId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lớp học", request.classId()));
            
            // Logic kiểm tra booking và thời gian
            validateClassCheckIn(user, targetClass);
        }

        // TẠO CHECKIN BẰNG JAVA THUẦN (Không dùng .builder())
        CheckIn checkIn = new CheckIn();
        checkIn.setUser(user);
        checkIn.setGymClass(targetClass);
        checkIn.setType(type);
        checkIn.setMethod(request.method());
        checkIn.setCheckInTime(Instant.now());

        // Lưu xuống DB
        CheckIn saved = checkInRepository.save(checkIn);

        return mapToResponse(saved);
    }

    // ───────────────── PRIVATE HELPERS ─────────────────

    private void validateClassCheckIn(User user, GymClass gymClass) {
        // 1. Kiểm tra xem user có đặt lịch lớp này không
        if (!classBookingRepository.existsByUser_IdAndGymClass_Id(user.getId(), gymClass.getId())) {
            throw new IllegalStateException("Bạn chưa đăng ký lớp học này nên không thể điểm danh.");
        }

        // 2. Kiểm tra tránh điểm danh 2 lần
        Optional<CheckIn> existingCheckIn = checkInRepository.findByUserIdAndGymClassId(user.getId(), gymClass.getId());
        if (existingCheckIn.isPresent()) {
            Instant time = existingCheckIn.get().getCheckInTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss 'ngày' dd/MM/yyyy")
                    .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            throw new IllegalStateException("Bạn đã điểm danh lớp này rồi vào lúc: " + formatter.format(time));
        }

        // 3. Kiểm tra khung giờ (Chỉ cho phép check-in trước 30p và sau khi bắt đầu 15p)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(gymClass.getDate(), gymClass.getStartTime());
        
        if (now.isBefore(startTime.minusMinutes(30))) {
            throw new IllegalStateException("Chưa đến giờ điểm danh (Chỉ cho phép trước 30 phút).");
        }
        if (now.isAfter(startTime.plusMinutes(15))) {
            throw new IllegalStateException("Đã quá giờ điểm danh (Quá 15 phút từ khi lớp bắt đầu).");
        }
    }

    // ───────────────── MAPPER CHUẨN GIAO DIỆN UI ─────────────────

    private CheckInResponse mapToResponse(CheckIn saved) {
        User user = saved.getUser();

        // 1. Tạo dữ liệu User cho UI
        String fullName = user.getFullName() != null ? user.getFullName() : user.getUsername();
        String memberCode = String.format("MEM%03d", user.getId()); 
        
        // Trạng thái thẻ
        String displayStatus = "Bị khóa";
        if (user.getStatus() != null && user.getStatus() == com.fitness.gymManagementSystem.entity.UserStatus.ACTIVE) {
            displayStatus = "Đang hoạt động";
        }

        // 2. Lấy thời gian hiện tại chuẩn múi giờ Việt Nam cho UI
        java.time.LocalDate date = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
        java.time.LocalTime timeIn = java.time.LocalTime.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));

        // 3. Trả về DTO tổng hợp (Phải truyền đúng thứ tự các trường trong CheckInResponse)
        return new CheckInResponse(
                saved.getId(),
                
                // --- CÁC TRƯỜNG DÀNH CHO GIAO DIỆN QR ---
                fullName,
                memberCode,
                displayStatus,
                "Twelve Lite",        // Gói hội viên (Tạm fix cứng theo thiết kế)
                "Chi nhánh Quận 1",   // Chi nhánh (Tạm fix cứng)
                date,
                timeIn,
                0,                    // Streak chuỗi ngày (Tạm fix cứng)
                
                // --- CÁC TRƯỜNG HỆ THỐNG (Giữ nguyên logic cũ của bạn) ---
                saved.getGymClass() != null ? saved.getGymClass().getId() : null,
                saved.getGymClass() != null ? saved.getGymClass().getName() : null,
                saved.getType(),
                saved.getMethod(),
                "Điểm danh thành công!",
                saved.getCheckInTime()
        );
}
}