package com.fitness.gymManagementSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import com.fitness.gymManagementSystem.entity.Role;

public record UpdateUserRequest(

    @Email(message = "Email không hợp lệ")
    String email,

    @Size(max = 100, message = "Full name tối đa 100 ký tự")
    String fullName,

    // ADMIN ONLY - phải check trong service
    Role role,

    // nullable = không update password
    @Size(min = 8, message = "Password tối thiểu 8 ký tự")
    String newPassword
) {}