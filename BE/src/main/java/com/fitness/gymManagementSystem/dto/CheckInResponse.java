package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fitness.gymManagementSystem.entity.CheckInMethod;
import com.fitness.gymManagementSystem.entity.CheckInType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CheckInResponse(
    Long checkInId,
    
    // =========================================================
    // CÁC TRƯỜNG BỔ SUNG ĐỂ FRONTEND VẼ GIAO DIỆN QR THÀNH CÔNG
    // =========================================================
    String fullName,
    String memberCode,
    String status,
    String membershipPackage,
    String branch,

    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    LocalDate date,

    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Ho_Chi_Minh")
    LocalTime timeIn,

    Integer streak,

    // =========================================================
    // CÁC TRƯỜNG HỆ THỐNG CŨ (Giữ nguyên logic của bạn)
    // =========================================================
    // Hai trường này tự động ẩn nếu điểm danh vào cổng chung (GYM_ENTRY)
    Long classId,
    String className,
    
    CheckInType type,
    CheckInMethod method,
    String message,
    
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    Instant checkInTime
) {}