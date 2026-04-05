package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "gym_classes")
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    private LocalTime startTime;
    private LocalTime endTime;

    private String studio;

    private Integer maxCapacity;
    private Integer currentCapacity = 0;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Enumerated(EnumType.STRING)
    private ClassStatus status;

    private Instant createdAt = Instant.now();

    // ===== GETTER & SETTER =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}