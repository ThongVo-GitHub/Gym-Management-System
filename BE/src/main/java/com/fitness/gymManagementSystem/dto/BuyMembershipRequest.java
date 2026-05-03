package com.fitness.gymManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyMembershipRequest(
    @NotNull(message = "Package ID không được để trống")
    @Positive(message = "Package ID không hợp lệ")
    Long packageId,

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    String paymentMethod
) {}