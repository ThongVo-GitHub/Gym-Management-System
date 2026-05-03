package com.fitness.gymManagementSystem.dto;

public record VNPayResponse(
        String code,
        String message,
        String paymentUrl
) {}