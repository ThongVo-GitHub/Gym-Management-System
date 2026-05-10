package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.gymManagementSystem.dto.BuyMembershipRequest;
import com.fitness.gymManagementSystem.dto.InvoiceResponse;
import com.fitness.gymManagementSystem.entity.*;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.InvoiceRepository;
import com.fitness.gymManagementSystem.repository.PackageRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock private PackageRepository packageRepository;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private MembershipService membershipService;

    private User mockUser;
    private GymPackage mockPackage;
    private Invoice mockPendingInvoice;
    private BuyMembershipRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setStatus(UserStatus.ACTIVE);

        mockPackage = new GymPackage();
        mockPackage.setId(1L);
        mockPackage.setPackageName("Gói 1 Tháng");
        mockPackage.setPrice(new BigDecimal("500000"));
        mockPackage.setDurationMonths(1);

        mockPendingInvoice = new Invoice();
        mockPendingInvoice.setId(10L);
        mockPendingInvoice.setUser(mockUser);
        mockPendingInvoice.setGymPackage(mockPackage);
        mockPendingInvoice.setStatus(InvoiceStatus.PENDING);
        mockPendingInvoice.setPaymentMethod(PaymentMethod.VNPAY); // Hoặc CASH tùy hệ thống của bạn

        mockRequest = new BuyMembershipRequest(1L, "VNPAY");
    }

    // --- TEST BUY MEMBERSHIP ---
    @Test
    void buyMembership_InvalidPaymentMethod_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        
        // Gửi chữ tào lao vào phương thức thanh toán
        BuyMembershipRequest badRequest = new BuyMembershipRequest(1L, "TIEN_AM_PHU"); 

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> membershipService.buyMembership(badRequest, "testuser"));
        assertEquals("Phương thức thanh toán không hợp lệ", ex.getMessage());
    }

    @Test
    void buyMembership_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        when(invoiceRepository.save(any())).thenReturn(mockPendingInvoice);

        InvoiceResponse res = membershipService.buyMembership(mockRequest, "testuser");
        assertNotNull(res);
        assertEquals(InvoiceStatus.PENDING, res.status()); // Chờ xác nhận
    }
    // --- BỔ SUNG ĐỂ ĐẠT 100% MEMBERSHIP SERVICE ---
    @Test
    void getMyMembership_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        
        // Giả lập gói tập đang còn hạn (Active)
        Invoice activeInvoice = new Invoice();
        activeInvoice.setUser(mockUser);
        activeInvoice.setGymPackage(mockPackage);
        activeInvoice.setExpiredDate(java.time.LocalDate.now().plusMonths(1));
        
        when(invoiceRepository.findTopByUserAndStatusOrderByExpiredDateDesc(mockUser, InvoiceStatus.PAID))
                .thenReturn(Optional.of(activeInvoice));

        InvoiceResponse res = membershipService.getMyMembership("testuser");
        assertNotNull(res);
    }

    @Test
    void getMyMembership_NoActive_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        
        // Giả lập không có hóa đơn nào
        when(invoiceRepository.findTopByUserAndStatusOrderByExpiredDateDesc(mockUser, InvoiceStatus.PAID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> membershipService.getMyMembership("testuser"));
    }

    // --- TEST CONFIRM PAYMENT (CỘNG DỒN NGÀY) ---
    @Test
    void confirmPayment_AlreadyPaid_ThrowsException() {
        mockPendingInvoice.setStatus(InvoiceStatus.PAID);
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(mockPendingInvoice));

        assertThrows(DuplicateResourceException.class, () -> membershipService.confirmPayment(10L));
    }

    @Test
    void confirmPayment_HasActivePackage_RenewsCorrectly() {
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(mockPendingInvoice));

        // Giả lập User đang có 1 gói CÒN HẠN (đến tháng sau)
        Invoice oldInvoice = new Invoice();
        oldInvoice.setExpiredDate(LocalDate.now().plusMonths(1));
        when(invoiceRepository.findTopByUserAndStatusOrderByExpiredDateDesc(mockUser, InvoiceStatus.PAID))
                .thenReturn(Optional.of(oldInvoice));

        when(invoiceRepository.save(any())).thenReturn(mockPendingInvoice);

        membershipService.confirmPayment(10L);

        // Kiểm tra xem expiredDate mới CÓ ĐƯỢC CỘNG DỒN TỪ THÁNG SAU không (1 tháng cũ + 1 tháng mua thêm = 2 tháng)
        assertEquals(LocalDate.now().plusMonths(2), mockPendingInvoice.getExpiredDate());
        assertEquals(InvoiceStatus.PAID, mockPendingInvoice.getStatus());
    }

    @Test
    void confirmPayment_NoActivePackage_StartsFromToday() {
        when(invoiceRepository.findById(10L)).thenReturn(Optional.of(mockPendingInvoice));

        // Giả lập User KHÔNG CÓ GÓI NÀO (hoặc đã hết hạn)
        when(invoiceRepository.findTopByUserAndStatusOrderByExpiredDateDesc(mockUser, InvoiceStatus.PAID))
                .thenReturn(Optional.empty());

        when(invoiceRepository.save(any())).thenReturn(mockPendingInvoice);

        membershipService.confirmPayment(10L);

        // Kiểm tra xem expiredDate có tính TỪ HÔM NAY không (1 tháng)
        assertEquals(LocalDate.now().plusMonths(1), mockPendingInvoice.getExpiredDate());
    }

    // --- TEST IS USER ACTIVE ---
    @Test
    void isUserActive_UserBanned_ReturnsFalse() {
        mockUser.setStatus(UserStatus.INACTIVE); // Bị khóa
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        assertFalse(membershipService.isUserActive("testuser"));
    }

    @Test
    void isUserActive_Valid_ReturnsTrue() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        // Có hóa đơn PAID và chưa hết hạn
        when(invoiceRepository.existsByUserAndStatusAndExpiredDateAfter(eq(mockUser), eq(InvoiceStatus.PAID), any())).thenReturn(true);

        assertTrue(membershipService.isUserActive("testuser"));
    }
}