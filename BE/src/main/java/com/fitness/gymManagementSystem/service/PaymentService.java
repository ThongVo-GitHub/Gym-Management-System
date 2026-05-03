package com.fitness.gymManagementSystem.service;

import com.fitness.gymManagementSystem.config.VNPayConfig;
import com.fitness.gymManagementSystem.dto.VNPayResponse;
import com.fitness.gymManagementSystem.util.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    private final VNPayConfig vnPayConfig;

    // Thay thế @RequiredArgsConstructor
    public PaymentService(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    public VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // Trả về DTO kiểu Record
        return new VNPayResponse("00", "success", paymentUrl);
    }
}