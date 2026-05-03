package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public record BookingResponse(
    Long bookingId,
    Long classId,
    String className,
    String message,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Ho_Chi_Minh")
    Instant bookedAt
) {}