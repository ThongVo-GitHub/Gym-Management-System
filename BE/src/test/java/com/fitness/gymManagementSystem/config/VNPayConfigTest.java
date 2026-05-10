package com.fitness.gymManagementSystem.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class VNPayConfigTest {

    private VNPayConfig vnPayConfig;

    @BeforeEach
    void setUp() {
        vnPayConfig = new VNPayConfig();
        // Bơm dữ liệu giả vào các biến @Value để lừa JaCoCo
        ReflectionTestUtils.setField(vnPayConfig, "vnp_PayUrl", "http://sandbox.vnpayment.vn");
        ReflectionTestUtils.setField(vnPayConfig, "vnp_ReturnUrl", "http://localhost:8081/callback");
        ReflectionTestUtils.setField(vnPayConfig, "vnp_TmnCode", "TMNCODE1");
        ReflectionTestUtils.setField(vnPayConfig, "secretKey", "SECRET_KEY_123");
        ReflectionTestUtils.setField(vnPayConfig, "vnp_Version", "2.1.0");
        ReflectionTestUtils.setField(vnPayConfig, "vnp_Command", "pay");
        ReflectionTestUtils.setField(vnPayConfig, "orderType", "other");
    }

    @Test
    void getVnp_PayUrl_ReturnsCorrectValue() {
        assertEquals("http://sandbox.vnpayment.vn", vnPayConfig.getVnp_PayUrl());
    }

    @Test
    void getSecretKey_ReturnsCorrectValue() {
        assertEquals("SECRET_KEY_123", vnPayConfig.getSecretKey());
    }

    @Test
    void getVNPayConfig_ReturnsMapWithCorrectParams() {
        Map<String, String> configMap = vnPayConfig.getVNPayConfig();

        assertNotNull(configMap);
        assertEquals("2.1.0", configMap.get("vnp_Version"));
        assertEquals("pay", configMap.get("vnp_Command"));
        assertEquals("TMNCODE1", configMap.get("vnp_TmnCode"));
        assertEquals("VND", configMap.get("vnp_CurrCode"));
        assertEquals("other", configMap.get("vnp_OrderType"));
        assertEquals("vn", configMap.get("vnp_Locale"));
        assertEquals("http://localhost:8081/callback", configMap.get("vnp_ReturnUrl"));
        
        // Các trường sinh tự động (Random/Time) nên mình chỉ check tồn tại
        assertTrue(configMap.containsKey("vnp_TxnRef"));
        assertTrue(configMap.containsKey("vnp_OrderInfo"));
        assertTrue(configMap.containsKey("vnp_CreateDate"));
        assertTrue(configMap.containsKey("vnp_ExpireDate"));
    }
}