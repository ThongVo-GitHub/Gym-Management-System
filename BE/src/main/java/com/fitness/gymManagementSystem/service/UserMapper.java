package com.fitness.gymManagementSystem.service;

import org.springframework.stereotype.Component;

import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
    if (user == null) return null;

    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getFullName(),
        user.getRole(),
        user.getStatus(),
        null, // ❌ remove gymPackage
        null, // ❌ remove expiryDate
        user.getGroupSessions(),
        user.getPtSessions(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
}
}