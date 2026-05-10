package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fitness.gymManagementSystem.service.MembershipService;

@WebMvcTest(MembershipController.class)
class MembershipControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private MembershipService membershipService;
    
    @MockitoBean private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    @MockitoBean private com.fitness.gymManagementSystem.security.JwtService jwtService;

    @Autowired 
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper; // Khai báo thêm ObjectMapper ở đầu class nếu chưa có


    @Test
    @WithMockUser(username = "testuser")
    void buyMembership_ReturnsCreated() throws Exception {
        
        // 1. TẠO REQUEST HỢP LỆ VỚI 2 THAM SỐ (packageId, paymentMethod)
        com.fitness.gymManagementSystem.dto.BuyMembershipRequest request = 
                new com.fitness.gymManagementSystem.dto.BuyMembershipRequest(1L, "VNPAY"); 

        // Giả lập service trả về null (vì ta chỉ cần test luồng Controller)
        when(membershipService.buyMembership(any(), eq("testuser"))).thenReturn(null);
        
        mockMvc.perform(post("/api/membership/buy")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Convert sang JSON chuẩn
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void confirmPayment_AsAdmin_ReturnsOk() throws Exception {
        mockMvc.perform(put("/api/membership/1/confirm").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getMyMembership_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/membership/me"))
                .andExpect(status().isOk());
    }
}