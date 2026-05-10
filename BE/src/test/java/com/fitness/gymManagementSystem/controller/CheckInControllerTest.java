package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.gymManagementSystem.service.CheckInService;

@WebMvcTest(CheckInController.class)
class CheckInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckInService checkInService;

    // --- BẢO VỆ APPLICATION CONTEXT ---
    @MockitoBean private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    @MockitoBean private com.fitness.gymManagementSystem.security.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper; // Thêm ObjectMapper để convert DTO sang JSON

    @Test
    @WithMockUser(username = "testuser")
    void verifyCheckIn_Authenticated_ReturnsOk() throws Exception {
        // Tạo một request giả lập có đầy đủ dữ liệu bắt buộc
        // Giả sử CheckInMethod của bạn có enum là QR_CODE (Bạn hãy đổi đúng theo Enum thực tế của bạn)
        com.fitness.gymManagementSystem.dto.CheckInRequest request = 
            new com.fitness.gymManagementSystem.dto.CheckInRequest(1L, com.fitness.gymManagementSystem.entity.CheckInMethod.QR_CODE);

        when(checkInService.processCheckIn(any(), eq("testuser"))).thenReturn(null);

        mockMvc.perform(post("/api/checkin/verify")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Convert object sang JSON chuẩn
                .andExpect(status().isOk());
    }
}