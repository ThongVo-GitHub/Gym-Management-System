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
import com.fitness.gymManagementSystem.entity.CheckInMethod;
import com.fitness.gymManagementSystem.entity.CheckInType;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.entity.User;
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
        // Khởi tạo User giả lập
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setFullName("Test Member");
        // Giả sử UserStatus.ACTIVE đã được set mặc định theo Entity của bạn

        // Khởi tạo GymClass giả lập
        mockGymClass = new GymClass();
        mockGymClass.setId(100L);
        mockGymClass.setName("Yoga Masterclass");
    }

    // ==========================================
    // 1. LUỒNG GYM_ENTRY (Điểm danh cổng)
    // ==========================================

    @Test
    void processCheckIn_GymEntry_ActiveMembership_ReturnsSuccess() {
        // Arrange
        CheckInRequest request = new CheckInRequest(null, CheckInMethod.QR_CODE);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(membershipService.isUserActive("testuser")).thenReturn(true);
        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(i -> {
            CheckIn saved = i.getArgument(0);
            saved.setId(999L);
            return saved;
        });

        // Act
        CheckInResponse response = checkInService.processCheckIn(request, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(CheckInType.GYM_ENTRY, response.type());
        assertEquals("Test Member", response.fullName());
        verify(gymClassRepository, never()).findById(any()); // Đảm bảo không query DB tìm lớp học
    }

    @Test
    void processCheckIn_GymEntry_InactiveMembership_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(null, CheckInMethod.FACE_RECOGNITION);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(membershipService.isUserActive("testuser")).thenReturn(false); // Gói tập hết hạn

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        assertEquals("Hội viên không có gói tập đang hoạt động.", ex.getMessage());
        verify(checkInRepository, never()).save(any());
    }

    // ==========================================
    // 2. LUỒNG CLASS_SESSION (Điểm danh lớp) - NEGATIVE CASES
    // ==========================================

    @Test
    void processCheckIn_ClassSession_NotBooked_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, CheckInMethod.QR_CODE);
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
        CheckInRequest request = new CheckInRequest(100L, CheckInMethod.QR_CODE);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(gymClassRepository.findById(100L)).thenReturn(Optional.of(mockGymClass));
        when(classBookingRepository.existsByUser_IdAndGymClass_Id(1L, 100L)).thenReturn(true);
        
        CheckIn oldCheckIn = new CheckIn();
        oldCheckIn.setCheckInTime(Instant.now());
        when(checkInRepository.findByUserIdAndGymClassId(1L, 100L)).thenReturn(Optional.of(oldCheckIn));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> checkInService.processCheckIn(request, "testuser"));
        assertTrue(ex.getMessage().contains("Bạn đã điểm danh lớp này rồi vào lúc:"));
    }

    // ==========================================
    // 3. LUỒNG CLASS_SESSION - BOUNDARY CASES (Thời gian)
    // ==========================================

    @Test
    void processCheckIn_ClassSession_TooEarly_ThrowsException() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, CheckInMethod.QR_CODE);; // Hoặc method tương ứng
        
        // Mẹo Kiến trúc: Đặt lịch học là 40 phút NỮA tính từ hiện tại (Quy định chỉ cho check-in trước 30p)
        LocalDateTime futureTime = LocalDateTime.now().plusMinutes(40);
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
        CheckInRequest request = new CheckInRequest(100L, CheckInMethod.STAFF_CONFIRM);
        
        // Mẹo Kiến trúc: Lớp học đã bắt đầu từ 20 phút TRƯỚC (Quy định trễ tối đa 15p)
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
    void processCheckIn_ClassSession_ValidTime_ReturnsSuccess() {
        // Arrange
        CheckInRequest request = new CheckInRequest(100L, CheckInMethod.QR_CODE);
        
        // Hợp lệ: Lớp sẽ bắt đầu sau 10 phút nữa (Nằm trong khoảng trước 30p)
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
        assertEquals("Yoga Masterclass", response.className());
        verify(checkInRepository, times(1)).save(any(CheckIn.class));
    }
}