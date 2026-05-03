package com.fitness.gymManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username", unique = true),
    @Index(name = "idx_users_email",    columnList = "email",    unique = true),
    @Index(name = "idx_users_role",     columnList = "role")      // thêm index cho filter theo role
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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // ===== USER-SPECIFIC: số buổi còn lại của hội viên =====

    @Column(name = "group_sessions")
    private Integer groupSessions;

    @Column(name = "pt_sessions")
    private Integer ptSessions;

    // ===== TRAINER-SPECIFIC =====

    /**
     * Chuyên môn của HLV — hiển thị trên card lớp học và profile.
     * Ví dụ: "Yoga", "CrossFit", "Strength Training", "Zumba"
     * Chỉ dùng khi role = TRAINER, null với USER/ADMIN.
     */
    @Column(name = "specialization", length = 100)
    private String specialization;

    /**
     * Mô tả ngắn về HLV — hiển thị trên trang profile trainer.
     * Ví dụ: "5 năm kinh nghiệm, chứng chỉ ACE"
     * Chỉ dùng khi role = TRAINER.
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * URL ảnh đại diện — dùng cho cả USER lẫn TRAINER.
     * Lưu đường dẫn tương đối: "/uploads/avatars/user_1.jpg"
     */
    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    // ===== TIMESTAMP =====

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ")
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

    // ===== GETTERS & SETTERS =====

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

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Integer getGroupSessions() { return groupSessions; }
    public void setGroupSessions(Integer groupSessions) { this.groupSessions = groupSessions; }

    public Integer getPtSessions() { return ptSessions; }
    public void setPtSessions(Integer ptSessions) { this.ptSessions = ptSessions; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Instant getCreatedAt() { return createdAt; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ===== BUILDER =====

    public static UserBuilder builder() { return new UserBuilder(); }

    public static final class UserBuilder {
        private String username;
        private String email;
        private String passwordHash;
        private Role role;
        private String fullName;
        private UserStatus status;
        private Integer groupSessions;
        private Integer ptSessions;
        private String specialization;
        private String bio;
        private String avatarUrl;

        public UserBuilder username(String v)       { this.username = v;       return this; }
        public UserBuilder email(String v)           { this.email = v;          return this; }
        public UserBuilder passwordHash(String v)    { this.passwordHash = v;   return this; }
        public UserBuilder role(Role v)              { this.role = v;           return this; }
        public UserBuilder fullName(String v)        { this.fullName = v;       return this; }
        public UserBuilder status(UserStatus v)      { this.status = v;         return this; }
        public UserBuilder groupSessions(Integer v)  { this.groupSessions = v;  return this; }
        public UserBuilder ptSessions(Integer v)     { this.ptSessions = v;     return this; }
        public UserBuilder specialization(String v)  { this.specialization = v; return this; }
        public UserBuilder bio(String v)             { this.bio = v;            return this; }
        public UserBuilder avatarUrl(String v)       { this.avatarUrl = v;      return this; }

        public User build() {
            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPasswordHash(passwordHash);
            u.setRole(role != null ? role : Role.USER);
            u.setFullName(fullName);
            u.setStatus(status != null ? status : UserStatus.ACTIVE);
            u.setGroupSessions(groupSessions != null ? groupSessions : 0);
            u.setPtSessions(ptSessions != null ? ptSessions : 0);
            u.setSpecialization(specialization);
            u.setBio(bio);
            u.setAvatarUrl(avatarUrl);
            return u;
        }
        public UserBuilder id(long l) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'id'");
        }
    }
}