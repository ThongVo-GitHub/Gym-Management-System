package com.fitness.gymManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.gymManagementSystem.dto.UpdateUserRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.security.JwtService;
import com.fitness.gymManagementSystem.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Tắt Security Filter để test thuần Controller nhanh gọn
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private UserService userService;
    @MockitoBean private JwtService jwtService;

    private UserResponse mockUserResponse;

    // Helper method tạo Authentication ảo (Tuyệt chiêu thay thế @WithMockUser)
    private UsernamePasswordAuthenticationToken getMockAuth(String username) {
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }

    @BeforeEach
    void setUp() {
        mockUserResponse = new UserResponse(1L, "user_me", "me@gmail.com", "My Name", Role.USER, null, null, null, null, null, null, null);
    }

    // ================= GET / UPDATE CURRENT USER (/me) =================

    @Test
    void getCurrentUser_Returns200Ok() throws Exception {
        when(userService.getByUsername("user_me")).thenReturn(mockUserResponse);

        mockMvc.perform(get("/api/users/me")
                .principal(getMockAuth("user_me"))) // "Tiêm" thẳng user vào Request
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user_me"));
    }

    @Test
    void updateMe_ValidRequest_Returns200Ok() throws Exception {
        UpdateUserRequest updateReq = new UpdateUserRequest("new@gmail.com", "New Name", null, null);
        
        when(userService.getByUsername("user_me")).thenReturn(mockUserResponse);
        when(userService.update(eq(1L), any(UpdateUserRequest.class), eq("user_me"))).thenReturn(mockUserResponse);

        mockMvc.perform(put("/api/users/me")
                .principal(getMockAuth("user_me")) // Tránh NullPointerException ở đây
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk());
    }

    // ================= ADMIN APIs =================

    @Test
    void listUsers_PaginationAndSort_Returns200Ok() throws Exception {
        when(userService.findAll(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockUserResponse)));

        mockMvc.perform(get("/api/users")
                .principal(getMockAuth("admin"))
                .param("page", "0")
                .param("size", "5")
                .param("sort", "email")
                .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("user_me"));
    }

    @Test
    void deleteUser_ValidId_Returns204NoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                .principal(getMockAuth("admin")))
                .andExpect(status().isNoContent()); // 204

        verify(userService).delete(1L);
    }

    // ================= BOUNDARY / VALIDATION CASES =================

    @Test
    void updateUser_InvalidData_Returns400BadRequest() throws Exception {
        // Arrange: Giả lập Client gửi lên một Email sai định dạng (thiếu @)
        // Lưu ý: Đảm bảo trong DTO UpdateUserRequest của bạn có gắn annotation @Email cho trường này.
        // Nếu không có @Email, hãy test trường nào có @NotNull hoặc @Size
        UpdateUserRequest invalidReq = new UpdateUserRequest("email-sai-dinh-dang", "Test", null, null);

        // Act & Assert: Gọi vào ID hợp lệ (số 1), nhưng dữ liệu Body bị sai
        mockMvc.perform(put("/api/users/1")
                .principal(getMockAuth("admin")) // Quyền Admin hợp lệ
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest()); // Kỳ vọng mã 400 (Client Error)
        
        // Architect Verify: Đảm bảo Tầng Service KHÔNG bị gọi khi dữ liệu rác được gửi lên
        verify(userService, org.mockito.Mockito.never()).update(any(), any(), any());
    }
}