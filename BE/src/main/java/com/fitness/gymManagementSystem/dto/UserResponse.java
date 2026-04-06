package com.fitness.gymManagementSystem.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.entity.UserStatus;

public record UserResponse(
    Long id,
    String username,
    String email,
    String fullName,
    Role role,
    UserStatus status,
    String gymPackage,
    LocalDate expiryDate,
    Integer groupSessions,
    Integer ptSessions,
    Instant createdAt,
    Instant updatedAt
) {}