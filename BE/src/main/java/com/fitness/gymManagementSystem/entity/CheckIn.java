package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "check_ins")
public class CheckIn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_class_id")
    private GymClass gymClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckInType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckInMethod method;

    @Column(nullable = false)
    private Instant checkInTime;

    // Constructor rỗng bắt buộc của JPA
    public CheckIn() {}

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public GymClass getGymClass() { return gymClass; }
    public void setGymClass(GymClass gymClass) { this.gymClass = gymClass; }

    public CheckInType getType() { return type; }
    public void setType(CheckInType type) { this.type = type; }

    public CheckInMethod getMethod() { return method; }
    public void setMethod(CheckInMethod method) { this.method = method; }

    public Instant getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }
}