package com.fitness.gymManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "gym_classes", indexes = {
        @Index(name = "idx_class_date",    columnList = "class_date"),
        @Index(name = "idx_class_trainer", columnList = "trainer_id"),
        @Index(name = "idx_class_status",  columnList = "status")
})
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // THÊM MỚI: Phục vụ trực tiếp cho UI (VD: "Thứ 2 - 4 - 6")
    @Column(length = 50)
    private String schedule;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "class_date", nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime endTime;

    @Column(nullable = false)
    private String studio;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private Integer currentCapacity = 0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStatus status = ClassStatus.OPEN;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    @Column(columnDefinition = "TIMESTAMPTZ")
    private Instant updatedAt;

    // ===== LIFECYCLE =====
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // GETTER/SETTER CHO SCHEDULE
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

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

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ===== HELPER LOGIC =====
    public boolean hasAvailableSlot() {
        return currentCapacity < maxCapacity;
    }

    public int getSpotsLeft() {
        return maxCapacity - currentCapacity;
    }
}