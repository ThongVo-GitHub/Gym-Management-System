package com.fitness.gymManagementSystem.service;
import com.fitness.gymManagementSystem.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fitness.gymManagementSystem.entity.ClassBooking;
import com.fitness.gymManagementSystem.entity.ClassStatus;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.ClassBookingRepository;



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
    public ClassBooking book(Long classId, String username) {

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
            throw new IllegalStateException("Bạn bị trùng lịch");
        }

        ClassBooking booking = new ClassBooking();
        booking.setUser(user);
        booking.setGymClass(gymClass);

        try {
            ClassBooking saved = bookingRepository.save(booking);

            gymClassService.increaseCapacity(gymClass);

            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Bạn đã đăng ký lớp này rồi");
        }
    }

    // ── CANCEL ───────────────────────────────
    @Transactional
    public void cancel(Long classId, String username) {

        GymClass gymClass = gymClassService.getEntityById(classId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        ClassBooking booking = bookingRepository.findByUser_Id(user.getId())
                .stream()
                .filter(b -> b.getGymClass().getId().equals(classId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Booking"));

        bookingRepository.delete(booking);

        gymClassService.decreaseCapacity(gymClass);
    }
}
