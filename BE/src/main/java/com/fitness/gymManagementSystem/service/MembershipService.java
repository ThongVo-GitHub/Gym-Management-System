package com.fitness.gymManagementSystem.service;

import com.fitness.gymManagementSystem.dto.BuyMembershipRequest;
import com.fitness.gymManagementSystem.dto.InvoiceResponse;
import com.fitness.gymManagementSystem.entity.*;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.InvoiceRepository;
import com.fitness.gymManagementSystem.repository.PackageRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MembershipService {

    private final PackageRepository packageRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    public MembershipService(PackageRepository packageRepository,
                            InvoiceRepository invoiceRepository,
                            UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    // ================= BUY =================
    @Transactional
    public InvoiceResponse buyMembership(BuyMembershipRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        GymPackage gymPackage = packageRepository.findById(request.packageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package", request.packageId()));

        // 🔥 RENEW LOGIC
        LocalDate startDate;

        Optional<Invoice> latest = invoiceRepository
                .findTopByUserAndStatusOrderByExpiredDateDesc(user, InvoiceStatus.PAID);

        if (latest.isPresent() && latest.get().getExpiredDate().isAfter(LocalDate.now())) {
            startDate = latest.get().getExpiredDate();
        } else {
            startDate = LocalDate.now();
        }

        LocalDate expiredDate = startDate.plusMonths(gymPackage.getDurationMonths());

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setGymPackage(gymPackage);
    invoice.setPaymentMethod(
        PaymentMethod.valueOf(request.paymentMethod().toUpperCase())
    );        invoice.setPaymentDate(LocalDate.now());
        invoice.setExpiredDate(expiredDate);
        invoice.setStatus(InvoiceStatus.PENDING);

        return toResponse(invoiceRepository.save(invoice));
    }

    // ================= CONFIRM =================
    @Transactional
    public InvoiceResponse confirmPayment(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new DuplicateResourceException("Invoice đã được thanh toán");
        }

        invoice.setStatus(InvoiceStatus.PAID);

        return toResponse(invoiceRepository.save(invoice));
    }

    // ================= GET ACTIVE =================
    public InvoiceResponse getMyMembership(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        Invoice invoice = invoiceRepository
                .findTopByUserAndStatusOrderByExpiredDateDesc(user, InvoiceStatus.PAID)
                .filter(i -> i.getExpiredDate().isAfter(LocalDate.now()))
                .orElseThrow(() -> new ResourceNotFoundException("Không có gói active"));

        return toResponse(invoice);
    }

    // ================= CHECK ACTIVE =================
    public boolean isUserActive(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        return invoiceRepository.existsByUserAndStatusAndExpiredDateAfter(
                user,
                InvoiceStatus.PAID,
                LocalDate.now()
        );
    }

// ================= MAPPER =================
    public InvoiceResponse toResponse(Invoice i) {
        return new InvoiceResponse(
                i.getId(),
                i.getUser().getId(),
                i.getUser().getUsername(),
                i.getGymPackage().getId(),
                i.getGymPackage().getPackageName(),
                i.getGymPackage().getPrice(), 
                i.getPaymentMethod(),
                i.getPaymentDate(),
                i.getExpiredDate(),
                i.getStatus(),
                i.getTxnRef(),
                i.getCreatedAt() 
        );
    }
}