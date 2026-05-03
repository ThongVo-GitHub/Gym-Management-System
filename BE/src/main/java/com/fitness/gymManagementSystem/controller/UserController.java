package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.UpdateUserRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // GET CURRENT USER (/me)
    // =========================
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    // =========================
    // UPDATE CURRENT USER (/me)
    // =========================
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateMe(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        // Lấy thông tin user hiện tại để lấy ID
        UserResponse currentUser = userService.getByUsername(username);

        // Cập nhật dựa trên ID của chính họ
        return ResponseEntity.ok(
                userService.update(currentUser.id(), request, username)
        );
    }

    // =========================
    // ADMIN: LIST USERS
    // =========================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> listUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sort,
            @RequestParam(defaultValue = "asc") String order) {

        // Whitelist sort fields (security + stability)
        List<String> allowedSortFields = List.of("username", "email", "createdAt");

        if (!allowedSortFields.contains(sort)) {
            sort = "username";
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                Math.min(size, 100), // Giới hạn size tối đa để tránh quá tải
                Sort.by(direction, sort)
        );

        return ResponseEntity.ok(
                userService.findAll(search, role, pageable)
        );
    }

    // =========================
    // UPDATE USER BY ID (ADMIN ONLY)
    // =========================
    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        
        // Chỉ ADMIN mới có quyền gọi API update thông qua ID của người khác
        String adminUsername = authentication.getName();

        return ResponseEntity.ok(
                userService.update(id, request, adminUsername)
        );
    }

    // =========================
    // DELETE USER (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // =========================
}