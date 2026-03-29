package com.fitness.gymManagementSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
    String username,

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email,

    @NotBlank(message = "Password không được để trống")
    @Size(min = 8, message = "Password tối thiểu 8 ký tự")
    String password,

    @NotBlank(message = "Full name không được để trống")
    @Size(max = 100, message = "Full name tối đa 100 ký tự")
    String fullName

) {}