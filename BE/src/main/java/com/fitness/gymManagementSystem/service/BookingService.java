package com.fitness.gymManagementSystem.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.fitness.gymManagementSystem.dto.BookingResponse;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.ClassBooking;
import com.fitness.gymManagementSystem.entity.ClassStatus;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.ClassBookingRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

@Service
public class BookingService {

    private final GymClassService gymClassService;
    private final ClassBookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(GymClassService gymClassService,
                        ClassBookingRepository bookingRepository,
                        UserRepository userRepository) {
        this.gymClassService = gymClassService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    // ── BOOK ─────────────────────────────────
    @Transactional
    public BookingResponse book(Long classId, String username) {
        
        // CHÚ Ý BẢO MẬT ĐỒNG THỜI: 
        // Hàm getEntityById của gymClassService LÝ TƯỞNG NHẤT nên dùng khóa dòng DB (Pessimistic Write Lock)
        // để chặn các request khác đọc/ghi class này cho đến khi transaction đăng ký này kết thúc.
        GymClass gymClass = gymClassService.getEntityById(classId);

        if (gymClass.getStatus() == ClassStatus.CANCELLED) {
            throw new IllegalStateException("Lớp đã bị hủy");
        }
        if (gymClass.getStatus() == ClassStatus.FULL) {
            throw new IllegalStateException("Lớp đã đầy");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        // check trùng lịch
        var conflicts = bookingRepository.findUserConflicts(
                user.getId(),
                gymClass.getDate(),
                gymClass.getStartTime(),
                gymClass.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Bạn bị trùng lịch với một lớp khác");
        }

        ClassBooking booking = new ClassBooking();
        booking.setUser(user);
        booking.setGymClass(gymClass);

        try {
            ClassBooking saved = bookingRepository.save(booking);

            // Tăng số lượng. Nên viết hàm DB update theo kiểu: 
            // UPDATE gym_class SET current_capacity = current_capacity + 1 WHERE id = ?
            gymClassService.increaseCapacity(gymClass);

            // Map trực tiếp sang DTO thay vì ném Entity ra Controller
            return new BookingResponse(
                    saved.getId(),
                    gymClass.getId(),
                    gymClass.getName(),
                    "Đăng ký lớp thành công",
                    saved.getBookedAt()
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Bạn đã đăng ký lớp này rồi");
        }
    }

    // ── GET MEMBERS ──────────────────────────
    @Transactional(readOnly = true)
    public List<UserResponse> getMembersByClass(Long classId, String trainerUsername) {

        GymClass gymClass = gymClassService.getEntityById(classId);

        // check trainer có phải chủ lớp không
        if (!gymClass.getTrainer().getUsername().equals(trainerUsername)) {
            throw new IllegalStateException("Bạn không phải trainer của lớp này");
        }

        // LƯU Ý: Nên dùng @Query("SELECT b FROM ClassBooking b JOIN FETCH b.user WHERE b.gymClass.id = :classId")
        // trong Repository để tránh lỗi N+1 Query.
        return bookingRepository.findByGymClass_Id(classId)
                .stream()
                .map(b -> {
                    User u = b.getUser();
                    // Đã dọn dẹp các tham số null bằng cách khuyến nghị bạn tạo 1 constructor gọn hơn trong DTO,
                    // hoặc chỉ map những field cần thiết để trainer điểm danh.
                    return new UserResponse(
                            u.getId(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getFullName(),
                            u.getRole(),
                            u.getStatus(),
                            // Khuyến nghị: Bỏ các tham số null ở dưới nếu Constructor/Builder hỗ trợ
                            null, null, null, null,
                            u.getCreatedAt(),
                            u.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    // ── CANCEL ───────────────────────────────
    @Transactional
    public void cancel(Long classId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        // Đã sửa lỗi kéo full DB vào RAM. Gọi trực tiếp hàm tìm kiếm chính xác.
        // BẠN CẦN THÊM HÀM NÀY VÀO ClassBookingRepository:
        // Optional<ClassBooking> findByUserIdAndGymClassId(Long userId, Long gymClassId);
        ClassBooking booking = bookingRepository.findByUser_IdAndGymClass_Id(user.getId(), classId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking không tồn tại"));

        bookingRepository.delete(booking);

        // Load GymClass từ Booking ra thay vì phải query lại lần nữa
        gymClassService.decreaseCapacity(booking.getGymClass());
    }
}