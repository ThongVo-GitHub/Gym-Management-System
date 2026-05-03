package com.fitness.gymManagementSystem.dto;

import com.fitness.gymManagementSystem.entity.CheckInMethod;
import jakarta.validation.constraints.NotNull;

public record CheckInRequest(
    
    // Có thể null. Nếu null -> Hiểu là Check-in vào cửa phòng Gym (GYM_ENTRY)
    // Nếu có giá trị -> Hiểu là Check-in vào lớp học (CLASS_SESSION)
    Long classId,

    // Bắt buộc phải truyền để biết nguồn check-in từ đâu (QR, FACE, hay Nhân viên ấn)
    @NotNull(message = "Phương thức check-in không được để trống")
    CheckInMethod method
) {}