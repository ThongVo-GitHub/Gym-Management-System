package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "gym_classes", indexes = {
        @Index(name = "idx_class_date", columnList = "date"),
        @Index(name = "idx_class_trainer", columnList = "trainer_id"),
        @Index(name = "idx_class_status", columnList = "status")
})
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String studio;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private Integer currentCapacity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User trainer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // ===== LIFECYCLE =====

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        validateTime();
        validateCapacity();
    }

    @PreUpdate
    protected void onUpdate() {
        validateTime();
        validateCapacity();
    }

    private void validateTime() {
        if (startTime != null && endTime != null && !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime phải sau startTime");
        }
    }

    private void validateCapacity() {
        if (currentCapacity != null && maxCapacity != null
                && currentCapacity > maxCapacity) {
            throw new IllegalArgumentException("currentCapacity vượt quá maxCapacity");
        }
    }

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Integer getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(Integer currentCapacity) { this.currentCapacity = currentCapacity; }
    public User getTrainer() { return trainer; }
    public void setTrainer(User trainer) { this.trainer = trainer; }
    public ClassStatus getStatus() { return status; }
    public void setStatus(ClassStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}