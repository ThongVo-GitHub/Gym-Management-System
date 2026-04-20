package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateClassRequest {

    @NotBlank(message = "Tên lớp không được để trống")
    private String name;

    @NotNull(message = "Ngày không được để trống")
    @Future(message = "Ngày phải là ngày trong tương lai")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "Giờ kết thúc không được để trống")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotBlank(message = "Studio không được để trống")
    private String studio;

    @NotNull
    @Min(value = 1, message = "Sức chứa tối thiểu là 1")
    @Max(value = 100, message = "Sức chứa tối đa là 100")
    private Integer maxCapacity;

    // ===== GETTERS & SETTERS =====
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}