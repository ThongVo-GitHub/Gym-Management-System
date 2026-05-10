package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.gymManagementSystem.dto.CreateClassRequest;
import com.fitness.gymManagementSystem.dto.GymClassResponse;
import com.fitness.gymManagementSystem.entity.ClassStatus; // Import thêm ClassStatus
import com.fitness.gymManagementSystem.service.BookingService;
import com.fitness.gymManagementSystem.service.GymClassService;

@WebMvcTest(GymClassController.class)
class GymClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GymClassService gymClassService;

    @MockitoBean
    private BookingService bookingService;

    // --- CÁC BEAN SECURITY CẦN THIẾT ĐỂ TRÁNH LỖI CONTEXT SẬP ---
    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @MockitoBean
    private com.fitness.gymManagementSystem.security.JwtService jwtService;


    

    // ==========================================
    // TEST POST API (Tạo lớp)
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createClass_AsAdmin_ReturnsCreated() throws Exception {
        // Truyền đủ 8 tham số cho CreateClassRequest
        CreateClassRequest request = new CreateClassRequest(
            "Yoga", "Thứ 2 - 4 - 6", LocalDate.now().plusDays(1), 
            LocalTime.of(18, 0), LocalTime.of(19, 0), "Studio 1", 20, 1L
        );
        
        // Truyền đủ 14 tham số cho GymClassResponse
        GymClassResponse response = new GymClassResponse(
            1L, "Yoga", "Thứ 2 - 4 - 6", LocalDate.now().plusDays(1), 
            LocalTime.of(18, 0), LocalTime.of(19, 0), "Studio 1", 
            20, 0, 20, "Trainer Admin", null, false, Instant.now()
        );

        when(gymClassService.createClass(any(CreateClassRequest.class), eq("admin"))).thenReturn(response);

        mockMvc.perform(post("/api/classes")
                .with(csrf()) // Bắt buộc phải có khi test API POST/PUT có security
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Yoga"))
                .andExpect(jsonPath("$.schedule").value("Thứ 2 - 4 - 6"));
    }

    // ==========================================
    // TEST GET API (Lịch tập)
    // ==========================================
    @Test
    @WithMockUser(username = "testuser")
    void getMySchedule_AuthenticatedUser_ReturnsOk() throws Exception {
        // Truyền đủ 14 tham số cho GymClassResponse
        GymClassResponse class1 = new GymClassResponse(
            1L, "Yoga", "Thứ 2 - 4 - 6", LocalDate.now().plusDays(1), 
            LocalTime.of(18, 0), LocalTime.of(19, 0), "Studio 1", 
            20, 10, 10, "Trainer A", null, false, Instant.now()
        );
        
        when(gymClassService.getMySchedule("testuser")).thenReturn(List.of(class1));

        mockMvc.perform(get("/api/classes/my-schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Yoga"))
                .andExpect(jsonPath("$[0].studio").value("Studio 1"));
    }

    // ==========================================
    // TEST DELETE API (Xóa lớp)
    // ==========================================
    @Test
    @WithMockUser(username = "trainer", roles = {"TRAINER"})
    void deleteClass_AsTrainer_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/classes/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
    // ==========================================
    // BỔ SUNG 1: TEST PUT API (Cập nhật lớp học)
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateClass_AsAdmin_ReturnsOk() throws Exception {
        // Tạo request hợp lệ để vượt qua @Valid
        CreateClassRequest request = new CreateClassRequest(
            "Yoga Update", "Thứ 3 - 5 - 7", java.time.LocalDate.now().plusDays(2), 
            java.time.LocalTime.of(18, 0), java.time.LocalTime.of(19, 0), "Studio 2", 25, 1L
        );

        mockMvc.perform(put("/api/classes/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ==========================================
    // BỔ SUNG 2: TEST POST API (Học viên đặt lớp)
    // ==========================================
    @Test
    @WithMockUser(username = "testuser")
    void book_AuthenticatedUser_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/classes/1/book")
                .with(csrf())) // API POST yêu cầu csrf
                .andExpect(status().isCreated());
    }

    // ==========================================
    // BỔ SUNG 3: TEST DELETE API (Học viên hủy đặt lớp)
    // ==========================================
    @Test
    @WithMockUser(username = "testuser")
    void cancel_AuthenticatedUser_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/classes/1/cancel")
                .with(csrf())) // API DELETE yêu cầu csrf
                .andExpect(status().isNoContent());
    }

    // ==========================================
    // BỔ SUNG 4: TEST GET API (Lấy danh sách tất cả lớp - Có phân trang)
    // ==========================================
    @Test
    @WithMockUser
    void getAll_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk());
    }

    // ==========================================
    // BỔ SUNG 5: TEST GET API (Lấy chi tiết 1 lớp)
    // ==========================================
    @Test
    @WithMockUser
    void getById_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk());
    }

    // ==========================================
    // BỔ SUNG 6: TEST GET API (Trainer xem danh sách học viên trong lớp)
    // ==========================================
    @Test
    @WithMockUser(username = "trainer", roles = {"TRAINER"})
    void getMembers_AsTrainer_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/classes/1/members"))
                .andExpect(status().isOk());
    }
}