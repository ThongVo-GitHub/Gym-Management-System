package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fitness.gymManagementSystem.dto.VNPayResponse;
import com.fitness.gymManagementSystem.service.PaymentService;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PaymentService paymentService;

    @MockitoBean private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    @MockitoBean private com.fitness.gymManagementSystem.security.JwtService jwtService;

    @Test
    @WithMockUser
    void pay_ReturnsOk() throws Exception {
        when(paymentService.createVnPayPayment(any())).thenReturn(new VNPayResponse("URL", "OK", ""));
        
        mockMvc.perform(get("/api/payment/vn-pay"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void payCallbackHandler_SuccessBranch_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/payment/vn-pay-callback")
                .param("vnp_ResponseCode", "00")) // Nhánh Thành công
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    @WithMockUser
    void payCallbackHandler_FailedBranch_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/payment/vn-pay-callback")
                .param("vnp_ResponseCode", "01")) // Nhánh Thất bại
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("01"));
    }
}