package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    private String id;

    private String userId;

    private String packageId;

    private String paymentMethod;

    private LocalDate paymentDate;   

    private LocalDate expiredDate;   

    @Enumerated(EnumType.STRING)     
    private InvoiceStatus status;

    // ===== Getter / Setter =====

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LocalDate getExpiredDate() { return expiredDate; }
    public void setExpiredDate(LocalDate expiredDate) { this.expiredDate = expiredDate; }

    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
}