package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "class_bookings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "gym_class_id"}),
    indexes = {
        @Index(name = "idx_booking_user", columnList = "user_id"),
        @Index(name = "idx_booking_class", columnList = "gym_class_id")
    }
)
public class ClassBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_class_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private GymClass gymClass;

    @Column(nullable = false, updatable = false)
    private Instant bookedAt;

    @PrePersist
    protected void onCreate() {
        this.bookedAt = Instant.now();
    }

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public GymClass getGymClass() { return gymClass; }
    public void setGymClass(GymClass gymClass) { this.gymClass = gymClass; }
    public Instant getBookedAt() { return bookedAt; }
    public void setBookedAt(Instant bookedAt) { this.bookedAt = bookedAt; }
}