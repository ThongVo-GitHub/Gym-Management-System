package com.fitness.gymManagementSystem.controller;

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

import com.fitness.gymManagementSystem.service.PackageService;

@WebMvcTest(PackageController.class)
class PackageControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PackageService packageService;

    @MockitoBean private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    @MockitoBean private com.fitness.gymManagementSystem.security.JwtService jwtService;

    @Autowired 
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addPackage_AsAdmin_ReturnsCreated() throws Exception {
        // 1. TẠO REQUEST CHUẨN XÁC VỚI BIGDECIMAL
        com.fitness.gymManagementSystem.dto.PackageRequest request = 
                new com.fitness.gymManagementSystem.dto.PackageRequest(
                    "Gói 1 Tháng", 
                    new java.math.BigDecimal("500000.0"), // Truyền đúng kiểu BigDecimal
                    1, 
                    "Mô tả gói tập cơ bản"
                );

        mockMvc.perform(post("/api/packages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) 
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updatePackage_AsAdmin_ReturnsOk() throws Exception {
        // 1. TẠO REQUEST CHUẨN XÁC VỚI BIGDECIMAL
        com.fitness.gymManagementSystem.dto.PackageRequest request = 
                new com.fitness.gymManagementSystem.dto.PackageRequest(
                    "Gói VIP 3 Tháng", 
                    new java.math.BigDecimal("1400000.0"), // Truyền đúng kiểu BigDecimal
                    3, 
                    "Mô tả gói tập VIP"
                );

        mockMvc.perform(put("/api/packages/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) 
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void getAllPackages_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/packages"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getPackageById_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/packages/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deletePackage_AsAdmin_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/packages/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}