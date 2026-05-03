package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.service.AuthService;
import com.fitness.gymManagementSystem.dto.AuthResponse;
import com.fitness.gymManagementSystem.dto.LoginRequest;
import com.fitness.gymManagementSystem.dto.RegisterRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Đăng ký tài khoản mới.
     * Lưu ý: Hãy đảm bảo endpoint đăng ký trong UserController đã được xóa bỏ để tránh trùng lặp.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse createdUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Đăng nhập và trả về Token (Access + Refresh nếu có).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}