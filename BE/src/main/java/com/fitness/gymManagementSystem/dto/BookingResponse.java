package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public class BookingResponse {

    private Long bookingId;
    private Long classId;
    private String className;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private Instant bookedAt;

    public BookingResponse(Long bookingId, Long classId, String className,
                        String message, Instant bookedAt) {
        this.bookingId = bookingId;
        this.classId = classId;
        this.className = className;
        this.message = message;
        this.bookedAt = bookedAt;
    }

    public Long getBookingId() { return bookingId; }
    public Long getClassId() { return classId; }
    public String getClassName() { return className; }
    public String getMessage() { return message; }
    public Instant getBookedAt() { return bookedAt; }
}