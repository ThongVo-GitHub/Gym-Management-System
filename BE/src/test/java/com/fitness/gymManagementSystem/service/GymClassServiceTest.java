package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.gymManagementSystem.dto.CreateClassRequest;
import com.fitness.gymManagementSystem.dto.GymClassResponse;
import com.fitness.gymManagementSystem.entity.*;
import com.fitness.gymManagementSystem.repository.ClassBookingRepository;
import com.fitness.gymManagementSystem.repository.GymClassRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GymClassServiceTest {

    @Mock private GymClassRepository gymClassRepository;
    @Mock private UserRepository userRepository;
    @Mock private ClassBookingRepository bookingRepository;

    @InjectMocks private GymClassService gymClassService;

    private User adminUser;
    private User trainerUser;
    private User normalUser;
    private GymClass mockClass;
    private CreateClassRequest validRequest;

    @BeforeEach
    void setUp() {
        // 1. Mock Admin
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        // 2. Mock Trainer
        trainerUser = new User();
        trainerUser.setId(2L);
        trainerUser.setUsername("trainer_john");
        trainerUser.setRole(Role.TRAINER);

        // 3. Mock Normal User
        normalUser = new User();
        normalUser.setId(3L);
        normalUser.setUsername("user_anna");
        normalUser.setRole(Role.USER);

        // 4. Mock Gym Class
        mockClass = new GymClass();
        mockClass.setId(100L);
        mockClass.setName("Yoga Master");
        mockClass.setTrainer(trainerUser); // Lớp này thuộc về trainer_john
        mockClass.setMaxCapacity(20);
        mockClass.setCurrentCapacity(0);
        mockClass.setStatus(ClassStatus.OPEN);

        // 5. Mock Request DTO
        validRequest = new CreateClassRequest(
                "Yoga Master", "Thứ 2 - 4 - 6", LocalDate.now(),
                LocalTime.of(8, 0), LocalTime.of(9, 0), "Studio 1", 20, 2L
        );
    }

    // ================= 1. NEGATIVE CASES (LỖI THỜI GIAN & TRÙNG LỊCH) =================

    @Test
    void createClass_EndTimeBeforeStartTime_ThrowsException() {
        // Arrange
        CreateClassRequest invalidReq = new CreateClassRequest(
                "Yoga", "T2", LocalDate.now(),
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Lỗi: Kết thúc 9h, Bắt đầu 10h
                "Studio 1", 20, 2L
        );

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> gymClassService.createClass(invalidReq, "trainer_john"));
        assertEquals("Giờ kết thúc phải sau giờ bắt đầu", ex.getMessage());
        verify(gymClassRepository, never()).save(any());
    }

    @Test
    void createClass_TrainerHasScheduleConflict_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        
        // Giả lập DB báo về có 1 lớp khác bị trùng lịch
        when(gymClassRepository.findConflictingClasses(anyLong(), any(), any(), any()))
                .thenReturn(List.of(new GymClass()));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.createClass(validRequest, "trainer_john"));
        assertTrue(ex.getMessage().contains("đã có lịch hướng dẫn lớp khác"));
    }

    // ================= 2. BOUNDARY/LOGIC CASES (PHÂN QUYỀN RESOLVE TRAINER) =================

    @Test
    void createClass_ByAdmin_MissingTrainerId_ThrowsException() {
        // Arrange: Admin tạo lớp nhưng quên truyền ID của HLV (trainerId = null)
        CreateClassRequest noTrainerReq = new CreateClassRequest(
                "Yoga", "T2", LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), "Studio 1", 20, null
        );
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> gymClassService.createClass(noTrainerReq, "admin"));
        assertEquals("Admin phải chọn Huấn luyện viên (trainerId) cho lớp học.", ex.getMessage());
    }

    @Test
    void createClass_ByTrainer_AutoAssignsSelf() {
        // Arrange: Trainer tạo lớp (không quan tâm trainerId truyền lên là gì, tự động lấy chính họ)
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        when(gymClassRepository.findConflictingClasses(anyLong(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(gymClassRepository.save(any(GymClass.class))).thenAnswer(i -> {
            GymClass saved = i.getArgument(0);
            saved.setId(999L);
            return saved;
        });

        // Act
        GymClassResponse response = gymClassService.createClass(validRequest, "trainer_john");

        // Assert
        assertNotNull(response);
        // Đảm bảo hệ thống tự động gán đúng HLV đang đăng nhập
        assertEquals("trainer_john", response.trainerName()); 
        verify(gymClassRepository, times(1)).save(any(GymClass.class));
    }

    // ================= 3. NEGATIVE CASES (AUTHORIZATION - BẢO MẬT UPDATE/DELETE) =================

    @Test
    void updateClass_ByNormalUser_ThrowsException() {
        // Arrange: Một user bình thường cố tình chọc vào API update
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("user_anna")).thenReturn(Optional.of(normalUser));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.updateClass(100L, validRequest, "user_anna"));
        assertEquals("Bạn không có quyền thao tác trên lớp học này.", ex.getMessage());
    }

    @Test
    void updateClass_ByDifferentTrainer_ThrowsException() {
        // Arrange: Một HLV khác cố tình sửa lớp của trainer_john
        User anotherTrainer = new User();
        anotherTrainer.setId(9L);
        anotherTrainer.setUsername("trainer_fake");
        anotherTrainer.setRole(Role.TRAINER);

        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("trainer_fake")).thenReturn(Optional.of(anotherTrainer));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.updateClass(100L, validRequest, "trainer_fake"));
        assertEquals("Bạn không có quyền thao tác trên lớp học này.", ex.getMessage());
    }
}