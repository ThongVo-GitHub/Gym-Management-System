package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.gymManagementSystem.config.VNPayConfig;
import com.fitness.gymManagementSystem.dto.VNPayResponse;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private VNPayConfig vnPayConfig;

    // Phải Mock toàn bộ HttpServletRequest (Hậu quả của Layer Leakage)
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        // Cài đặt giả lập cấu hình mặc định của VNPAY
        lenient().when(vnPayConfig.getSecretKey()).thenReturn("SECRET_KEY_123");
        lenient().when(vnPayConfig.getVnp_PayUrl()).thenReturn("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");

        Map<String, String> mockConfigMap = new HashMap<>();
        mockConfigMap.put("vnp_Version", "2.1.0");
        mockConfigMap.put("vnp_Command", "pay");
        
        // Return một Map mới để tránh lỗi tham chiếu khi code chính put thêm dữ liệu
        lenient().when(vnPayConfig.getVNPayConfig()).thenAnswer(invocation -> new HashMap<>(mockConfigMap));
    }

    // ==========================================
    // 1. NORMAL CASE (Luồng thành công)
    // ==========================================
    @Test
    void createVnPayPayment_ValidRequest_ReturnsPaymentUrl() {
        // Arrange
        // Giả lập Frontend gửi lên form-data hoặc query params
        when(request.getParameter("amount")).thenReturn("500000");
        when(request.getParameter("bankCode")).thenReturn("NCB");
        
        // Giả sử hàm VNPayUtil.getIpAddress() gọi vào getRemoteAddr() của request
        lenient().when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        // Act
        VNPayResponse response = paymentService.createVnPayPayment(request);

        // Assert
        assertNotNull(response);
        assertEquals("00", response.code());
        assertEquals("success", response.message());
        
        // Kiểm chứng URL tạo ra có khớp với dữ liệu truyền vào không
        assertTrue(response.paymentUrl().startsWith("https://sandbox.vnpayment.vn"));
        assertTrue(response.paymentUrl().contains("vnp_Amount=50000000")); // Phải được nhân 100
        assertTrue(response.paymentUrl().contains("vnp_BankCode=NCB"));
        assertTrue(response.paymentUrl().contains("vnp_SecureHash="));
    }

    // ==========================================
    // 2. NEGATIVE CASE (Thiếu dữ liệu - Gây lỗi hệ thống)
    // ==========================================
    @Test
    void createVnPayPayment_MissingAmount_ThrowsNumberFormatException() {
        // Arrange
        // Hacker cố tình không truyền tham số amount lên
        when(request.getParameter("amount")).thenReturn(null);

        // Act & Assert
        // Code của bạn dùng Integer.parseInt(null) -> Chắc chắn văng lỗi NumberFormatException
        assertThrows(NumberFormatException.class, () -> {
            paymentService.createVnPayPayment(request);
        });
    }

    @Test
    void createVnPayPayment_InvalidAmountFormat_ThrowsNumberFormatException() {
        // Arrange
        // Hacker nhập số tiền là chữ cái
        when(request.getParameter("amount")).thenReturn("abc");

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> {
            paymentService.createVnPayPayment(request);
        });
    }

    // ==========================================
    // 3. BOUNDARY CASE (Biên tùy chọn)
    // ==========================================
    @Test
    void createVnPayPayment_NullBankCode_StillBuildsUrlSuccessfully() {
        // Arrange
        when(request.getParameter("amount")).thenReturn("100000");
        // User không chọn ngân hàng (Để VNPAY tự hiển thị danh sách)
        when(request.getParameter("bankCode")).thenReturn(null);

        // Act
        VNPayResponse response = paymentService.createVnPayPayment(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.paymentUrl().contains("vnp_BankCode=")); // Không được chứa tham số này trên URL
    }
}