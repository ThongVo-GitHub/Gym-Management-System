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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fitness.gymManagementSystem.dto.CreateClassRequest;
import com.fitness.gymManagementSystem.dto.GymClassResponse;
import com.fitness.gymManagementSystem.dto.UserResponse;
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
        trainerUser.setFullName("John Doe");
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
        mockClass.setDate(LocalDate.now());
        mockClass.setStartTime(LocalTime.of(8, 0));
        mockClass.setEndTime(LocalTime.of(9, 0));
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
        CreateClassRequest invalidReq = new CreateClassRequest(
                "Yoga", "T2", LocalDate.now(),
                LocalTime.of(10, 0), LocalTime.of(9, 0), // Lỗi: Kết thúc 9h, Bắt đầu 10h
                "Studio 1", 20, 2L
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> gymClassService.createClass(invalidReq, "trainer_john"));
        assertEquals("Giờ kết thúc phải sau giờ bắt đầu", ex.getMessage());
    }

    @Test
    void createClass_TrainerHasScheduleConflict_ThrowsException() {
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        when(gymClassRepository.findConflictingClasses(anyLong(), any(), any(), any()))
                .thenReturn(List.of(new GymClass())); // Giả lập bị trùng lịch

        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.createClass(validRequest, "trainer_john"));
        assertTrue(ex.getMessage().contains("đã có lịch hướng dẫn lớp khác"));
    }

    // ================= 2. BOUNDARY/LOGIC CASES (PHÂN QUYỀN RESOLVE TRAINER) =================

    @Test
    void createClass_ByAdmin_MissingTrainerId_ThrowsException() {
        CreateClassRequest noTrainerReq = new CreateClassRequest(
                "Yoga", "T2", LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), "Studio 1", 20, null
        );
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> gymClassService.createClass(noTrainerReq, "admin"));
        assertEquals("Admin phải chọn Huấn luyện viên (trainerId) cho lớp học.", ex.getMessage());
    }

    @Test
    void createClass_ByTrainer_AutoAssignsSelf() {
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        when(gymClassRepository.findConflictingClasses(anyLong(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(gymClassRepository.save(any(GymClass.class))).thenAnswer(i -> {
            GymClass saved = i.getArgument(0);
            saved.setId(999L);
            return saved;
        });

        GymClassResponse response = gymClassService.createClass(validRequest, "trainer_john");

        assertNotNull(response);
        assertEquals("John Doe", response.trainerName()); // Test đúng hàm mapToResponse lấy FullName
    }

    // ================= 3. NEGATIVE CASES (AUTHORIZATION - BẢO MẬT UPDATE/DELETE) =================

    @Test
    void updateClass_ByNormalUser_ThrowsException() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("user_anna")).thenReturn(Optional.of(normalUser));

        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.updateClass(100L, validRequest, "user_anna"));
        assertEquals("Bạn không có quyền thao tác trên lớp học này.", ex.getMessage());
    }

    @Test
    void updateClass_ByDifferentTrainer_ThrowsException() {
        User anotherTrainer = new User();
        anotherTrainer.setId(9L);
        anotherTrainer.setUsername("trainer_fake");
        anotherTrainer.setRole(Role.TRAINER);

        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("trainer_fake")).thenReturn(Optional.of(anotherTrainer));

        IllegalStateException ex = assertThrows(IllegalStateException.class, 
                () -> gymClassService.updateClass(100L, validRequest, "trainer_fake"));
        assertEquals("Bạn không có quyền thao tác trên lớp học này.", ex.getMessage());
    }

    // ================= 4. UPDATE & DELETE SUCCESS CASES =================

    @Test
    void updateClass_AdminChangeTime_Conflicts_ThrowsException() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(trainerUser));
        
        // Admin đổi thời gian -> Bị trùng
        CreateClassRequest changeTimeReq = new CreateClassRequest("Yoga", "T2", LocalDate.now().plusDays(1), LocalTime.of(8, 0), LocalTime.of(9, 0), "Studio 1", 20, 2L);
        when(gymClassRepository.findConflictingClasses(any(), any(), any(), any())).thenReturn(List.of(new GymClass()));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> gymClassService.updateClass(100L, changeTimeReq, "admin"));
        assertTrue(ex.getMessage().contains("đã có lịch hướng dẫn lớp khác"));
    }

    @Test
    void updateClass_Success_BecomesFull() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        
        // Cập nhật Max Capacity = Current Capacity (0) để test nhánh Status = FULL
        CreateClassRequest reqFull = new CreateClassRequest("Yoga", "T3", mockClass.getDate(), mockClass.getStartTime(), mockClass.getEndTime(), "Studio", 0, 2L);
        when(gymClassRepository.save(any())).thenReturn(mockClass);

        gymClassService.updateClass(100L, reqFull, "trainer_john");
        verify(gymClassRepository).save(argThat(g -> g.getStatus() == ClassStatus.FULL)); // Ép chạy qua nhánh FULL
    }

    @Test
    void deleteClass_Success() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        
        gymClassService.deleteClass(100L, "trainer_john");
        verify(gymClassRepository).delete(mockClass);
    }

    // ================= 5. GET CLASS (MEMBERS, SCHEDULE, ALL) =================

    @Test
    void getMySchedule_AsTrainer_ReturnsTrainerClasses() {
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        when(gymClassRepository.findByTrainer_IdOrderByDateAscStartTimeAsc(2L)).thenReturn(List.of(mockClass));
        
        List<GymClassResponse> res = gymClassService.getMySchedule("trainer_john");
        assertFalse(res.isEmpty());
    }

    @Test
    void getMySchedule_AsUser_ReturnsBookedClasses() {
        when(userRepository.findByUsername("user_anna")).thenReturn(Optional.of(normalUser));
        when(bookingRepository.findBookedClassesByUserId(3L)).thenReturn(List.of(mockClass));
        
        List<GymClassResponse> res = gymClassService.getMySchedule("user_anna");
        assertFalse(res.isEmpty());
    }

    @Test
    void getMembers_Success() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        when(userRepository.findByUsername("trainer_john")).thenReturn(Optional.of(trainerUser));
        
        ClassBooking booking = new ClassBooking(); 
        booking.setUser(normalUser);
        when(bookingRepository.findByGymClass_Id(100L)).thenReturn(List.of(booking));

        List<UserResponse> res = gymClassService.getMembers(100L, "trainer_john");
        assertFalse(res.isEmpty());
    }

    @Test
    void getAll_ReturnsPage() {
        Page<GymClass> page = new PageImpl<>(List.of(mockClass));
        when(gymClassRepository.findAll(any(PageRequest.class))).thenReturn(page);
        
        Page<GymClassResponse> res = gymClassService.getAll(PageRequest.of(0, 10));
        assertFalse(res.isEmpty());
    }

    @Test
    void getById_Success() {
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockClass));
        GymClassResponse res = gymClassService.getById(100L);
        assertNotNull(res);
    }

    // ================= 6. UTILITY / CAPACITY / NULL HANDLING =================

    @Test
    void getEntityById_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> gymClassService.getEntityById(null));
    }

    @Test
    void increaseCapacity_Fail_ThrowsException() {
        when(gymClassRepository.incrementCapacitySafe(100L)).thenReturn(0); // Cập nhật thất bại (hết chỗ)
        assertThrows(IllegalStateException.class, () -> gymClassService.increaseCapacity(mockClass));
    }

    @Test
    void decreaseCapacity_Success() {
        gymClassService.decreaseCapacity(mockClass);
        verify(gymClassRepository).decrementCapacitySafe(100L);
    }

    @Test
    void mapToResponse_TrainerNull_FullCapacity() {
        // Test nhánh nếu HLV bị xóa (null) và lớp đã đầy để lấy 100% Branch Coverage
        GymClass nullTrainerClass = new GymClass();
        nullTrainerClass.setMaxCapacity(10);
        nullTrainerClass.setCurrentCapacity(10); // Đầy
        nullTrainerClass.setTrainer(null); // Trống HLV

        GymClassResponse res = gymClassService.mapToResponse(nullTrainerClass);
        assertEquals("Chưa cập nhật", res.trainerName());
        assertTrue(res.isFull());
    }
}