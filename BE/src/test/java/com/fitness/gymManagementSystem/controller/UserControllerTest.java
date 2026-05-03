package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.gymManagementSystem.dto.UpdateUserRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.security.JwtService;
import com.fitness.gymManagementSystem.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private UserService userService;
    @MockitoBean private JwtService jwtService;

    private UserResponse mockUserResponse;

    @BeforeEach
    void setUp() {
        mockUserResponse = new UserResponse(1L, "user_me", "me@gmail.com", "My Name", Role.USER, null, null, null, null, null, null, null);
    }

    // ================= GET / UPDATE CURRENT USER (/me) =================

    @Test
    @WithMockUser(username = "user_me") // Giả lập người dùng tên "user_me" đang gửi request
    void getCurrentUser_Returns200Ok() throws Exception {
        when(userService.getByUsername("user_me")).thenReturn(mockUserResponse);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user_me"));
    }

    @Test
    @WithMockUser(username = "user_me")
    void updateMe_ValidRequest_Returns200Ok() throws Exception {
        UpdateUserRequest updateReq = new UpdateUserRequest("new@gmail.com", "New Name", null, null);
        
        when(userService.getByUsername("user_me")).thenReturn(mockUserResponse);
        when(userService.update(eq(1L), any(UpdateUserRequest.class), eq("user_me"))).thenReturn(mockUserResponse);

        mockMvc.perform(put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk());
    }

    // ================= ADMIN APIs =================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listUsers_PaginationAndSort_Returns200Ok() throws Exception {
        when(userService.findAll(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockUserResponse)));

        // Giả lập Frontend gửi request với page, size, sort
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "email") // Test whitelist sort
                .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("user_me"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_ValidId_Returns204NoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent()); // 204

        verify(userService).delete(1L);
    }

    // ================= BOUNDARY / VALIDATION CASES =================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_IdNotNumber_Returns404() throws Exception {
        UpdateUserRequest req = new UpdateUserRequest("test@test.com", "Test", null, null);

        // Act & Assert: Gọi vào /api/users/abc (chữ thay vì số). 
        // Nhờ Regex /{id:\\d+}, Spring Boot sẽ không match được Route này và ném lỗi HTTP 404 Not Found.
        mockMvc.perform(put("/api/users/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound()); // Nếu không có Regex, chỗ này sẽ là 400 TypeMismatch
    }
}