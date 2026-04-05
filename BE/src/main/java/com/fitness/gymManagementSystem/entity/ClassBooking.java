// package com.fitness.gymManagementSystem.entity;

// import jakarta.persistence.*;
// import java.time.Instant;

// @Entity
// public class ClassBooking {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
//     private User user;

//     @ManyToOne
//     private GymClass gymClass;

//     private Instant bookedAt = Instant.now();

//     // ===== GETTER SETTER =====

//     public Long getId() {
//         return id;
//     }

//     public User getUser() {
//         return user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     public GymClass getGymClass() {
//         return gymClass;
//     }

//     public void setGymClass(GymClass gymClass) {
//         this.gymClass = gymClass;
//     }

//     public Instant getBookedAt() {
//         return bookedAt;
//     }

//     public void setBookedAt(Instant bookedAt) {
//         this.bookedAt = bookedAt;
//     }
// }