package com.fitness.gymManagementSystem.exception;

// Khi dữ liệu trùng (username, email..) - trả về 409 conflict
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}