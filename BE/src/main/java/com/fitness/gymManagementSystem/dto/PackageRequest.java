package com.fitness.gymManagementSystem.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PackageRequest(

    @NotBlank(message = "Tên gói không được để trống")
    @Size(max = 100)
    String packageName,

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    BigDecimal price,

    @NotNull(message = "Thời hạn không được để trống")
    @Min(value = 1, message = "Tối thiểu 1 tháng")
    @Max(value = 24, message = "Tối đa 24 tháng")
    Integer durationMonths,

    @Size(max = 500)
    String description

) {}