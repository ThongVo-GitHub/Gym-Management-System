package com.fitness.gymManagementSystem.service;

import com.fitness.gymManagementSystem.dto.CreateClassRequest;
import com.fitness.gymManagementSystem.dto.GymClassResponse;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.*;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GymClassService {

    private final GymClassRepository gymClassRepository;
    private final UserRepository userRepository;
    private final ClassBookingRepository bookingRepository;

    public GymClassService(GymClassRepository gymClassRepository,
                        UserRepository userRepository,
                        ClassBookingRepository bookingRepository) {
        this.gymClassRepository = gymClassRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    // ───────────────── CREATE ─────────────────
    @Transactional
    public GymClassResponse createClass(CreateClassRequest req, String currentUsername) {
        validateTime(req);
        
        // Xác định chính xác HLV sẽ dạy lớp này (Admin gán hoặc Trainer tự tạo)
        User trainer = resolveTrainerToAssign(currentUsername, req.trainerId());
        
        checkScheduleConflict(trainer, req);

        GymClass gymClass = new GymClass();
        gymClass.setName(req.name());
        gymClass.setSchedule(req.schedule());
        gymClass.setDate(req.date());
        gymClass.setStartTime(req.startTime());
        gymClass.setEndTime(req.endTime());
        gymClass.setStudio(req.studio());
        gymClass.setMaxCapacity(req.maxCapacity());
        gymClass.setCurrentCapacity(0);
        gymClass.setTrainer(trainer);
        gymClass.setStatus(ClassStatus.OPEN);

        return mapToResponse(gymClassRepository.save(gymClass));
    }

    // ───────────────── UPDATE ─────────────────
    @Transactional
    public GymClassResponse updateClass(Long id, CreateClassRequest req, String currentUsername) {
        // Kiểm tra quyền: Phải là Admin HOẶC Trainer sở hữu lớp
        GymClass gymClass = authorizeClassModification(id, currentUsername);
        validateTime(req);

        // Nếu Admin muốn đổi HLV khác cho lớp này
        User newTrainer = resolveTrainerToAssign(currentUsername, req.trainerId());
        
        // Chỉ check trùng lịch nếu đổi giờ học, đổi ngày hoặc đổi HLV
        if (!gymClass.getDate().equals(req.date()) || 
            !gymClass.getStartTime().equals(req.startTime()) ||
            !gymClass.getTrainer().getId().equals(newTrainer.getId())) {
            checkScheduleConflict(newTrainer, req);
        }

        gymClass.setName(req.name());
        gymClass.setSchedule(req.schedule());
        gymClass.setDate(req.date());
        gymClass.setStartTime(req.startTime());
        gymClass.setEndTime(req.endTime());
        gymClass.setStudio(req.studio());
        gymClass.setMaxCapacity(req.maxCapacity());
        gymClass.setTrainer(newTrainer); // Cập nhật lại HLV
        
        if (gymClass.getCurrentCapacity() >= gymClass.getMaxCapacity()) {
            gymClass.setStatus(ClassStatus.FULL);
        } else {
            gymClass.setStatus(ClassStatus.OPEN);
        }

        return mapToResponse(gymClassRepository.save(gymClass));
    }

    // ───────────────── DELETE ─────────────────
    @Transactional
    public void deleteClass(Long id, String currentUsername) {
        GymClass gymClass = authorizeClassModification(id, currentUsername);
        gymClassRepository.delete(gymClass);
    }

    // ───────────────── GET MY SCHEDULE ─────────────────
    @Transactional(readOnly = true)
    public List<GymClassResponse> getMySchedule(String currentUsername) {
        
        // 1. Lấy thông tin User từ Database thông qua định danh JWT
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", currentUsername));

        List<GymClass> myClasses;
        
        // 2. Phân luồng logic: HLV xem lịch dạy, Học viên xem lịch đã đặt
        if (currentUser.getRole() == Role.TRAINER) {
            myClasses = gymClassRepository.findByTrainer_IdOrderByDateAscStartTimeAsc(currentUser.getId());
        } else {
            // Đây chính là lệnh gọi DB để lấy danh sách lịch đã book của học viên
            myClasses = bookingRepository.findBookedClassesByUserId(currentUser.getId());
        }
        
        // 3. Map dữ liệu sang chuẩn DTO để trả về cho Frontend
        return myClasses.stream().map(this::mapToResponse).toList();
    }

    // ───────────────── GET MEMBERS ─────────────────
    @Transactional(readOnly = true)
    public List<UserResponse> getMembers(Long classId, String currentUsername) {
        // Admin xem được tất cả, Trainer chỉ xem được thành viên lớp mình
        GymClass gymClass = authorizeClassModification(classId, currentUsername);

        return bookingRepository.findByGymClass_Id(gymClass.getId())
                .stream()
                .map(b -> {
                    User u = b.getUser();
                    return new UserResponse(
                            u.getId(), u.getUsername(), u.getEmail(), u.getFullName(),
                            u.getRole(), u.getStatus(), null, null,
                            u.getGroupSessions(), u.getPtSessions(), u.getCreatedAt(), u.getUpdatedAt()
                    );
                }).toList();
    }

    // ───────────────── GET ALL (OPTIMIZED WITH PAGINATION) ─────────────────
    @Transactional(readOnly = true)
    public Page<GymClassResponse> getAll(Pageable pageable) {
        // Áp dụng Pageable để tối ưu truy vấn Database
        return gymClassRepository.findAll(pageable).map(this::mapToResponse);
    }

    // ───────────────── GET BY ID ─────────────────
    @Transactional(readOnly = true)
    public GymClassResponse getById(Long id) {
        return mapToResponse(getEntityById(id));
    }

    public GymClass getEntityById(Long id) {
        if (id == null) throw new IllegalArgumentException("ID lớp học không được null");
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GymClass", id));
    }

    // ───────────────── CAPACITY ─────────────────
    @Transactional
    public void increaseCapacity(GymClass gymClass) {
        int rowsUpdated = gymClassRepository.incrementCapacitySafe(gymClass.getId());
        if (rowsUpdated == 0) {
            throw new IllegalStateException("Lớp đã đầy hoặc không tồn tại, không thể đăng ký thêm");
        }
    }

    @Transactional
    public void decreaseCapacity(GymClass gymClass) {
        gymClassRepository.decrementCapacitySafe(gymClass.getId());
    }

    // ───────────────── PRIVATE HELPERS (BUSINESS LOGIC) ─────────────────

    private void validateTime(CreateClassRequest req) {
        if (!req.endTime().isAfter(req.startTime())) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }
    }

    /**
     * Logic quyết định ai là HLV của lớp học này.
     */

    @SuppressWarnings("unused")
    private User resolveTrainerToAssign(String currentUsername, Long requestedTrainerId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", currentUsername));

        if (currentUser.getRole() == Role.ADMIN) {
            // Nếu là Admin, bắt buộc phải truyền ID của HLV lên
            if (requestedTrainerId == null) {
                throw new IllegalArgumentException("Admin phải chọn Huấn luyện viên (trainerId) cho lớp học.");
            }
            return userRepository.findById(requestedTrainerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer", requestedTrainerId));
        } else if (currentUser.getRole() == Role.TRAINER) {
            // Nếu là Trainer tự tạo lớp, tự động gán chính mình
            return currentUser;
        } else {
            throw new IllegalStateException("Bạn không có quyền phân công lớp học.");
        }
    }

    /**
     * Logic phân quyền chi tiết: Ai được phép sửa/xóa/xem thành viên lớp này.
     */
    private GymClass authorizeClassModification(Long classId, String currentUsername) {
        GymClass gymClass = getEntityById(classId);
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", currentUsername));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = gymClass.getTrainer().getUsername().equals(currentUsername);

        if (!isAdmin && !isOwner) {
            throw new IllegalStateException("Bạn không có quyền thao tác trên lớp học này.");
        }
        return gymClass;
    }

    private void checkScheduleConflict(User trainer, CreateClassRequest req) {
        var conflicts = gymClassRepository.findConflictingClasses(
                trainer.getId(), req.date(), req.startTime(), req.endTime()
        );
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Trainer " + trainer.getFullName() + " đã có lịch hướng dẫn lớp khác trong khung giờ này");
        }
    }

    // ───────────────── MAPPER CHUẨN UI ─────────────────
    public GymClassResponse mapToResponse(GymClass g) {
        String trainerName = "Chưa cập nhật";
        if (g.getTrainer() != null) {
            trainerName = g.getTrainer().getFullName() != null 
                    ? g.getTrainer().getFullName() 
                    : g.getTrainer().getUsername();
        }

        Integer spotsLeft = g.getMaxCapacity() - g.getCurrentCapacity();
        Boolean isFull = spotsLeft <= 0 || g.getStatus() == ClassStatus.FULL;

        return new GymClassResponse(
                g.getId(), g.getName(), g.getSchedule(), g.getDate(),
                g.getStartTime(), g.getEndTime(), g.getStudio(),
                g.getMaxCapacity(), g.getCurrentCapacity(), spotsLeft,
                trainerName, g.getStatus(), isFull, g.getCreatedAt()
        );
    }
}