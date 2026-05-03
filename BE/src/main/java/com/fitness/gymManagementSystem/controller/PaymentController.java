package com.fitness.gymManagementSystem.controller;

import com.fitness.gymManagementSystem.dto.VNPayResponse;
import com.fitness.gymManagementSystem.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // Thay thế @RequiredArgsConstructor
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/vn-pay")
    public ResponseEntity<VNPayResponse> pay(HttpServletRequest request) {
        Long amount = Long.valueOf(request.getParameter("amount"));
        String orderId = request.getParameter("orderId");
        String orderInfo = request.getParameter("orderInfo");
        return ResponseEntity.ok(paymentService.createVnPayPayment(amount, orderId, orderInfo));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        
        if ("00".equals(status)) {
            // Thanh toán thành công (Mã 00 từ VNPay)
            return ResponseEntity.ok(new VNPayResponse("00", "Thanh toán thành công", ""));
        } else {
            // Thanh toán thất bại (Khách hủy, sai mã OTP, không đủ tiền...)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VNPayResponse(status, "Thanh toán thất bại", ""));
        }
    }
}