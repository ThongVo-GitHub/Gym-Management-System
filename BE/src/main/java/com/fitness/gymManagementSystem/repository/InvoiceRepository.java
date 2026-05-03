package com.fitness.gymManagementSystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.fitness.gymManagementSystem.entity.Invoice;
import com.fitness.gymManagementSystem.entity.InvoiceStatus;
import com.fitness.gymManagementSystem.entity.User;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // ================= BASIC =================
Page<Invoice> findByUser_Id(Long userId, Pageable pageable);

Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

Page<Invoice> findByUser_IdAndStatus(Long userId, InvoiceStatus status, Pageable pageable);

boolean existsByGymPackageId(Long packageId);

    // ================= CORE (QUAN TRỌNG NHẤT) =================
Optional<Invoice> findTopByUserAndStatusOrderByExpiredDateDesc(
        User user,
        InvoiceStatus status
);

    // ================= CHECK ACTIVE =================
boolean existsByUserAndStatusAndExpiredDateAfter(
        User user,
        InvoiceStatus status,
        java.time.LocalDate date
);
}