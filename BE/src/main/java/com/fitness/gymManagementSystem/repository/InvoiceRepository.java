package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fitness.gymManagementSystem.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
}