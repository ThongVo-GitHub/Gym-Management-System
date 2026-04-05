package com.fitness.gymManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Query derivation : phát sinh truy vấn từ tên method
    // Dành cho method không có @Query
    // findByUsername 
    // Cơ chế: Spring Data JPA đọc tên method, suy ra điều kiện truy vấn -> tự sinh ra JPQL / SQL
    // Quy ước tên:
    // findBy... -> SELECT, điều kiện theo thuộc tính sau "By"
    // existsBy... -> Kiểm tra tồn tại (COUNT / EXISTS)
    // ...AndIdNot -> thêm điều kiện: AND id != :id
    // Không viết câu lệnh truy vấn, chỉ cần đặt tên method đúng quy tắc => Spring Data JPA sẽ implement giúp

    
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    List<User> findByRole(Role role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByFullName(String fullName);
    

    // Tìm kiếm theo search (username hoặc email chứa chuỗi input) và role)
    // Ứng với GET /api/users?search=...&role=...

    @Query("SELECT u FROM User u WHERE " +
        "(:search IS NULL OR :search = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
        "AND (:role IS NULL OR u.role = :role)"
    )
    Page<User> findAllBySearchAndRole(@Param("search") String search, @Param("role") Role role, Pageable pageable);
}
