package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class Class {
    
    @Id
    private String id;
    
    private String className;
    private String trainer;
    private String startTime;
    private int maxSlots;
    private int booked;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getTrainer() { return trainer; }
    public void setTrainer(String trainer) { this.trainer = trainer; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public int getMaxSlots() { return maxSlots; }
    public void setMaxSlots(int maxSlots) { this.maxSlots = maxSlots; }
    public int getBooked() { return booked; }
    public void setBooked(int booked) { this.booked = booked; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}