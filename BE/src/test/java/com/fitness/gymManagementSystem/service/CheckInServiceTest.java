package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.gymManagementSystem.dto.CheckInRequest;
import com.fitness.gymManagementSystem.dto.CheckInResponse;
import com.fitness.gymManagementSystem.entity.CheckIn;
import com.fitness.gymManagementSystem.entity.CheckInType;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.CheckInRepository;
import com.fitness.gymManagementSystem.repository.ClassBookingRepository;
import com.fitness.gymManagementSystem.repository.GymClassRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {

    @Mock private CheckInRepository checkInRepository;
    @Mock private MembershipService membershipService;
    @Mock private GymClassRepository gymClassRepository;
    @Mock private UserRepository userRepository;
    @Mock private ClassBookingRepository classBookingRepository;

    @InjectMocks
    private CheckInService checkInService;

    private User mockUser;
    private GymClass mockGymClass;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .fullName("Test Member")
                .status(UserStatus.ACTIVE)
                .build();

        mockGymClass = new GymClass();
        mockGymClass.setId(100L);
        mockGymClass.setName("Yoga Cơ Bản");
        // Ngày giờ sẽ được set linh hoạt trong từng test case
    }

    // ==========================================
    // MODULE 1: CHECK-IN PHÒNG TẬP CHUNG (GYM_ENTRY)
    // ==========================================

    @Test
    void processCheckIn_GymEntry_ActiveMembership_Success() {
        // Arrange
        CheckInRequest request = new CheckInRequest(null, "QR_CODE"); // classId null -> GYM_ENTRY
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(membershipService.isUserActive("testuser")).thenReturn(true);
        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(i -> {
            CheckIn saved = i.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // Act
        CheckInResponse response = checkInService.processCheckIn(request, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(CheckInType.GYM_ENTRY, response.type());
        assertEquals("QR_CODE", response.method());
        assertEquals("Test Member", response.fullName()); // Test logic mapper UI
        verify(gymClassRepository, never()).findById(any());
    }

    @Test
    void processCheckIn_GymEntry_InactiveMembership_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(null, "FACE_ID");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(membershipService.isUserActive("testuser")).thenReturn(false); // Gói tập hết hạn

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        
        assertEquals("Hội viên không có gói tập đang hoạt động.", ex.getMessage());
        verify(checkInRepository, never()).save(any());
    }

    // ==========================================
    // MODULE 2: CHECK-IN LỚP HỌC (CLASS_SESSION) - VALIDATION
    // ==========================================

    @Test
    void processCheckIn_ClassSession_UserNotBooked_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(1, "QR_CODE");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(false); // Chưa đặt lịch

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        
        assertEquals("Bạn chưa đăng ký lớp học này nên không thể điểm danh.", ex.getMessage());
    }

    @Test
    void processCheckIn_ClassSession_AlreadyCheckedIn_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, "QR_CODE");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(true);
        
        // Giả lập đã check-in
        CheckIn oldCheckIn = new CheckIn();
        oldCheckIn.setCheckInTime(Instant.now());
        when(checkInRepository.findByUserIdAndGymClassId(1L, 100L)).thenReturn(Optional.of(oldCheckIn));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        
        assertTrue(ex.getMessage().contains("Bạn đã điểm danh lớp này rồi vào lúc"));
    }

    // ==========================================
    // MODULE 3: KIỂM THỬ BIÊN THỜI GIAN (TIME BOUNDARY)
    // ==========================================

    @Test
    void processCheckIn_ClassSession_TooEarly_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, "APP_BUTTON");
        
        // Thiết lập giờ học là 45 phút SAU so với hiện tại (Quá sớm, quy định là trước 30p)
        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(45);
        mockGymClass.setDate(futureTime.toLocalDate());
        mockGymClass.setStartTime(futureTime.toLocalTime());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(true);
        when(checkInRepository.findByUserIdAndGymClassId(1L, 100L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        
        assertEquals("Chưa đến giờ điểm danh (Chỉ cho phép trước 30 phút).", ex.getMessage());
    }

    @Test
    void processCheckIn_ClassSession_TooLate_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, "APP_BUTTON");
        
        // Thiết lập giờ học đã bắt đầu từ 20 phút TRƯỚC (Quá muộn, quy định là trễ tối đa 15p)
        LocalDateTime pastTime = LocalDateTime.now().minusMinutes(20);
        mockGymClass.setDate(pastTime.toLocalDate());
        mockGymClass.setStartTime(pastTime.toLocalTime());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(true);
        when(checkInRepository.findByUserIdAndGymClassId(1L, 100L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        
        assertEquals("Đã quá giờ điểm danh (Quá 15 phút từ khi lớp bắt đầu).", ex.getMessage());
    }

    @Test
    void processCheckIn_ClassSession_ValidTime_Success() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, "QR_CODE");
        
        // Thiết lập lớp học bắt đầu sau 10 phút nữa (Hoàn toàn hợp lệ: trong vùng trước 30p)
        LocalDateTime validTime = LocalDateTime.now().plusMinutes(10);
        mockGymClass.setDate(validTime.toLocalDate());
        mockGymClass.setStartTime(validTime.toLocalTime());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(true);
        when(checkInRepository.findByUserIdAndGymClassId(1L, 100L)).thenReturn(Optional.empty());
        
        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CheckInResponse response = checkInService.processCheckIn(request, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(CheckInType.CLASS_SESSION, response.type());
        assertEquals("Yoga Cơ Bản", response.className());
        assertEquals("Điểm danh thành công!", response.message());
        
        verify(checkInRepository, times(1)).save(any(CheckIn.class));
    }
}