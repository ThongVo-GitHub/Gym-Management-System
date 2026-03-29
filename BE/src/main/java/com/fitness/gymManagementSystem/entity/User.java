package com.fitness.gymManagementSystem.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username", unique = true),
    @Index(name = "idx_users_email", columnList = "email", unique = true),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "full_name", length = 100)
    private String fullName;

    // ================= NEW FIELDS (GYM) =================

    @Column(name = "gym_package", length = 50)
    private String gymPackage;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "group_sessions")
    private Integer groupSessions;

    @Column(name = "pt_sessions")
    private Integer ptSessions;

    @Column(length = 20)
    private String status;

    // ================= TIMESTAMP =================

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // ================= GETTER / SETTER =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGymPackage() {
        return gymPackage;
    }

    public void setGymPackage(String gymPackage) {
        this.gymPackage = gymPackage;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getGroupSessions() {
        return groupSessions;
    }

    public void setGroupSessions(Integer groupSessions) {
        this.groupSessions = groupSessions;
    }

    public Integer getPtSessions() {
        return ptSessions;
    }

    public void setPtSessions(Integer ptSessions) {
        this.ptSessions = ptSessions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // ================= BUILDER =================

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String passwordHash;
        private Role role;
        private String fullName;
        private String gymPackage;
        private LocalDate expiryDate;
        private Integer groupSessions;
        private Integer ptSessions;
        private String status;
        


        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserBuilder gymPackage(String gymPackage) {
            this.gymPackage = gymPackage;
            return this;
        }

        public UserBuilder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public UserBuilder groupSessions(Integer groupSessions) {
            this.groupSessions = groupSessions;
            return this;
        }

        public UserBuilder ptSessions(Integer ptSessions) {
            this.ptSessions = ptSessions;
            return this;
        }

        public UserBuilder status(String status) {
            this.status = status;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            user.setRole(role);
            user.setFullName(fullName);

            user.setGymPackage(gymPackage);
            user.setExpiryDate(expiryDate);
            user.setGroupSessions(groupSessions);
            user.setPtSessions(ptSessions);
            user.setStatus(status);

            // user.setCreatedAt(createdAt);
            // user.setUpdatedAt(updatedAt);

            return user;
        }
    }
}