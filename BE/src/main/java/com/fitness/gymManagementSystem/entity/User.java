package com.fitness.gymManagementSystem.entity;

import java.time.Instant;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username", unique = true),
    @Index(name = "idx_users_email", columnList = "email", unique = true),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== BASIC =====
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "group_sessions")
    private Integer groupSessions;

    @Column(name = "pt_sessions")
    private Integer ptSessions;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // ===== TIMESTAMP =====
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // ===== AUTO TIME =====
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

    // ===== GETTER / SETTER =====

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    

    public Integer getGroupSessions() { return groupSessions; }

    public void setGroupSessions(Integer groupSessions) { this.groupSessions = groupSessions; }

    public Integer getPtSessions() { return ptSessions; }

    public void setPtSessions(Integer ptSessions) { this.ptSessions = ptSessions; }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }

    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ===== BUILDER =====
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private String username;
        private String email;
        private String passwordHash;
        private Role role;
        private String fullName;
        private Integer groupSessions;
        private Integer ptSessions;
        private UserStatus status;

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


        public UserBuilder groupSessions(Integer groupSessions) {
            this.groupSessions = groupSessions;
            return this;
        }

        public UserBuilder ptSessions(Integer ptSessions) {
            this.ptSessions = ptSessions;
            return this;
        }

        public UserBuilder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            user.setRole(role != null ? role : Role.USER);
            user.setFullName(fullName);
            user.setGroupSessions(groupSessions);
            user.setPtSessions(ptSessions);
            user.setStatus(status != null ? status : UserStatus.ACTIVE);

            return user;
        }
    }
}