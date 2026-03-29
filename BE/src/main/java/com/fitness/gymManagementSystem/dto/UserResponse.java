package com.fitness.gymManagementSystem.dto;

import java.time.Instant;

import com.fitness.gymManagementSystem.entity.Role;

public record UserResponse(
    Long id,
    String username,
    String email,
    Role role,
    String fullName,
    Instant createdAt,
    Instant updatedAt
) {

}