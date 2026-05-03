package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.gymManagementSystem.dto.AuthResponse;
import com.fitness.gymManagementSystem.dto.LoginRequest;
import com.fitness.gymManagementSystem.dto.RegisterRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.service.AuthService;
import com.fitness.gymManagementSystem.security.JwtService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Tạm tắt Security Filter để test thuần Controller
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private AuthService authService;
    @MockitoBean private JwtService jwtService;

    @Test
    void register_ValidRequest_Returns201Created() throws Exception {
        // Arrange
        RegisterRequest req = new RegisterRequest("newuser", "new@gmail.com", "pass123", "New User");
        UserResponse mockResponse = new UserResponse(1L, "newuser", "new@gmail.com", "New User", null, null, null, null, null, null, null, null);
        
        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated()) // Kỳ vọng 201
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void login_ValidRequest_Returns200Ok() throws Exception {
        // Arrange
        LoginRequest req = new LoginRequest("newuser", "pass123");
        AuthResponse mockResponse = new AuthResponse("access_jwt", "refresh_jwt", "Bearer", 3600L, null);
        
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()) // Kỳ vọng 200
                .andExpect(jsonPath("$.accessToken").value("access_jwt"));
    }
}