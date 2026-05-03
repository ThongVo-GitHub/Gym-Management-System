package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateClassRequest(
    @NotBlank(message = "Tên lớp không được để trống")
    String name,

    // BỔ SUNG: Trường schedule để nhận chuỗi "Thứ 2 - 4 - 6" từ Frontend
    @NotBlank(message = "Lịch học (chuỗi hiển thị) không được để trống")
    String schedule,

    @NotNull(message = "Ngày không được để trống")
    // ĐÃ TỐI ƯU: Cho phép tạo lớp học vào ngày hôm nay (FutureOrPresent) thay vì chỉ ngày mai (Future)
    @FutureOrPresent(message = "Ngày học phải là hôm nay hoặc trong tương lai")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date,

    @NotNull(message = "Giờ bắt đầu không được để trống")
    @JsonFormat(pattern = "HH:mm")
    LocalTime startTime,

    @NotNull(message = "Giờ kết thúc không được để trống")
    @JsonFormat(pattern = "HH:mm")
    LocalTime endTime,

    @NotBlank(message = "Studio không được để trống")
    String studio,

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa tối thiểu là 1")
    @Max(value = 100, message = "Sức chứa tối đa là 100")
    Integer maxCapacity,

    Long trainerId
) {}