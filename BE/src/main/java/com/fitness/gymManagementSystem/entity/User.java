package com.fitness.gymManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có cho Hibernate/JPA
@AllArgsConstructor // Bắt buộc phải có để @Builder hoạt động
@Builder // Lombok tự động sinh Builder hoàn chỉnh
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

    @Builder.Default // Lombok sẽ lấy giá trị này làm mặc định khi build()
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // ===== USER-SPECIFIC: số buổi còn lại của hội viên =====

    @Builder.Default
    @Column(name = "group_sessions")
    private Integer groupSessions = 0;

    @Builder.Default
    @Column(name = "pt_sessions")
    private Integer ptSessions = 0;

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
}