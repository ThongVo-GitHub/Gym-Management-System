        package com.fitness.gymManagementSystem.service;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
        import static org.mockito.ArgumentMatchers.anyLong;
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
        import org.springframework.dao.DataIntegrityViolationException;

        import com.fitness.gymManagementSystem.dto.BookingResponse;
        import com.fitness.gymManagementSystem.entity.*;
        import com.fitness.gymManagementSystem.repository.ClassBookingRepository;
        import com.fitness.gymManagementSystem.repository.UserRepository;

        @ExtendWith(MockitoExtension.class)
        class BookingServiceTest {

        // 1. MOCK DEPENDENCIES (Giả lập các layer phụ thuộc)
        @Mock private GymClassService gymClassService;
        @Mock private ClassBookingRepository bookingRepository;
        @Mock private UserRepository userRepository;

        // 2. INJECT VÀO SERVICE CẦN TEST
        @InjectMocks private BookingService bookingService;

        // 3. MOCK DATA
        private User mockUser;
        private GymClass mockClass;

        @BeforeEach
        void setUp() {
                mockUser = new User();
                mockUser.setId(1L);
                mockUser.setUsername("member123");

                mockClass = new GymClass();
                mockClass.setId(10L);
                mockClass.setName("Yoga Cơ Bản");
                mockClass.setStatus(ClassStatus.OPEN);
                mockClass.setDate(LocalDate.now());
                mockClass.setStartTime(LocalTime.of(18, 0));
                mockClass.setEndTime(LocalTime.of(19, 0));
        }

        // ================= NEGATIVE CASES (KIỂM THỬ NGOẠI LỆ) =================

        @Test
        void book_ClassIsCancelled_ThrowsException() {
                // Arrange
                mockClass.setStatus(ClassStatus.CANCELLED); // Đổi trạng thái lớp
                when(gymClassService.getEntityById(10L)).thenReturn(mockClass);

                // Act & Assert
                IllegalStateException ex = assertThrows(IllegalStateException.class, 
                        () -> bookingService.book(10L, "member123"));
                assertEquals("Lớp đã bị hủy", ex.getMessage());
                
                // Verify: Đảm bảo luồng thực thi bị chặn đứng, tuyệt đối không chạm vào DB
                verify(bookingRepository, never()).save(any());
        }

        @Test
        void book_ClassIsFull_ThrowsException() {
                // Arrange
                mockClass.setStatus(ClassStatus.FULL);
                when(gymClassService.getEntityById(10L)).thenReturn(mockClass);

                // Act & Assert
                IllegalStateException ex = assertThrows(IllegalStateException.class, 
                        () -> bookingService.book(10L, "member123"));
                assertEquals("Lớp đã đầy", ex.getMessage());
        }

        @Test
        void book_ScheduleConflict_ThrowsException() {
                // Arrange
                when(gymClassService.getEntityById(10L)).thenReturn(mockClass);
                when(userRepository.findByUsername("member123")).thenReturn(Optional.of(mockUser));
                
                // Giả lập DB báo về: Có 1 lớp khác bị trùng giờ
                when(bookingRepository.findUserConflicts(anyLong(), any(), any(), any()))
                        .thenReturn(List.of(new ClassBooking()));

                // Act & Assert
                IllegalStateException ex = assertThrows(IllegalStateException.class, 
                        () -> bookingService.book(10L, "member123"));
                assertEquals("Bạn bị trùng lịch với một lớp khác", ex.getMessage());
        }

        @Test
        void book_DuplicateBooking_ThrowsDataIntegrityException() {
                // Arrange
                when(gymClassService.getEntityById(10L)).thenReturn(mockClass);
                when(userRepository.findByUsername("member123")).thenReturn(Optional.of(mockUser));
                when(bookingRepository.findUserConflicts(anyLong(), any(), any(), any()))
                        .thenReturn(Collections.emptyList()); // Không trùng lịch

                // Giả lập Database ném lỗi Constraint (Do Unique Key UserId-ClassId)
                when(bookingRepository.save(any(ClassBooking.class)))
                        .thenThrow(new DataIntegrityViolationException("Duplicate Key"));

                // Act & Assert
                IllegalStateException ex = assertThrows(IllegalStateException.class, 
                        () -> bookingService.book(10L, "member123"));
                assertEquals("Bạn đã đăng ký lớp này rồi", ex.getMessage());
        }

        // ================= NORMAL / BOUNDARY CASES =================

        @Test
        void book_ValidData_SuccessAndIncreasesCapacity() {
                // Arrange
                when(gymClassService.getEntityById(10L)).thenReturn(mockClass);
                when(userRepository.findByUsername("member123")).thenReturn(Optional.of(mockUser));
                when(bookingRepository.findUserConflicts(anyLong(), any(), any(), any()))
                        .thenReturn(Collections.emptyList()); // Không trùng lịch
                
                // Cấu hình mock khi save thành công
                ClassBooking savedBooking = new ClassBooking();
                savedBooking.setId(100L);
                savedBooking.setUser(mockUser);
                savedBooking.setGymClass(mockClass);
                
                when(bookingRepository.save(any(ClassBooking.class))).thenReturn(savedBooking);

                // Act
                BookingResponse response = bookingService.book(10L, "member123");

                // Assert
                assertNotNull(response);
                assertEquals(100L, response.bookingId());
                assertEquals("Đăng ký lớp thành công", response.message());
                
                // Verify (Vô cùng quan trọng trong System Design): 
                // Phải chắc chắn rằng hàm tăng capacity được gọi ĐÚNG 1 LẦN để giữ toàn vẹn dữ liệu
                verify(gymClassService, times(1)).increaseCapacity(mockClass);
        }
        
        // ================= CANCEL BOOKING =================
        
        @Test
        void cancel_ValidData_SuccessAndDecreasesCapacity() {
                // Arrange
                ClassBooking existingBooking = new ClassBooking();
                existingBooking.setId(100L);
                existingBooking.setGymClass(mockClass);
                
                when(userRepository.findByUsername("member123")).thenReturn(Optional.of(mockUser));
                when(bookingRepository.findByUser_IdAndGymClass_Id(1L, 10L))
                        .thenReturn(Optional.of(existingBooking));

                // Act
                bookingService.cancel(10L, "member123");

                // Assert & Verify
                verify(bookingRepository, times(1)).delete(existingBooking); // Đã xóa khỏi DB
                verify(gymClassService, times(1)).decreaseCapacity(mockClass); // Phải nhả slot lại cho người khác
        }
        }