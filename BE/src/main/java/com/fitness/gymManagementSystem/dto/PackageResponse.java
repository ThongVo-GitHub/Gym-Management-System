package com.fitness.gymManagementSystem.dto;

import java.math.BigDecimal;

public record PackageResponse(
    Long id,
    String packageName,
    BigDecimal price,
    int durationMonths,
    String description
) {}
