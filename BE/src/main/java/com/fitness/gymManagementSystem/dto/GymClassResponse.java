package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitness.gymManagementSystem.entity.ClassStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Instant;

public record GymClassResponse(
    Long id,
    String name,
    
    // "Thứ 2 - 4 - 6"
    String schedule, 
    
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
    @JsonFormat(pattern = "HH:mm") LocalTime startTime,
    @JsonFormat(pattern = "HH:mm") LocalTime endTime,
    
    String studio,
    Integer maxCapacity,
    Integer currentCapacity,
    Integer spotsLeft,
    String trainerName,
    ClassStatus status,
    
    // Thêm biến cờ (Xanh/Đỏ)

    Boolean isFull,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Ho_Chi_Minh") Instant createdAt
) {}