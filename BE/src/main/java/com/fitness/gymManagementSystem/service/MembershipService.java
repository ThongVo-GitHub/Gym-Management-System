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

    // ================= BUY (CHỈ TẠO ĐƠN CHỜ) =================
    @Transactional
    public InvoiceResponse buyMembership(BuyMembershipRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        GymPackage gymPackage = packageRepository.findById(request.packageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package", request.packageId()));

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setGymPackage(gymPackage);
        
        // Xử lý an toàn lỗi sai format Enum
        try {
            invoice.setPaymentMethod(PaymentMethod.valueOf(request.paymentMethod().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
        }

        invoice.setStatus(InvoiceStatus.PENDING);
        // Lưu ý: Không set paymentDate và expiredDate ở đây vì tiền chưa vào tài khoản.

        return toResponse(invoiceRepository.save(invoice));
    }

    // ================= CONFIRM (TÍNH NGÀY TẠI ĐÂY) =================
    @Transactional
    public InvoiceResponse confirmPayment(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new DuplicateResourceException("Invoice đã được thanh toán trước đó");
        }

        // 🔥 RENEW LOGIC: Chỉ tính ngày khi Admin xác nhận đã nhận tiền
        LocalDate startDate;
        Optional<Invoice> latest = invoiceRepository
                .findTopByUserAndStatusOrderByExpiredDateDesc(invoice.getUser(), InvoiceStatus.PAID);

        // Nếu user đang có 1 gói Active -> Cộng dồn ngày
        if (latest.isPresent() && latest.get().getExpiredDate() != null && latest.get().getExpiredDate().isAfter(LocalDate.now())) {
            startDate = latest.get().getExpiredDate();
        } else {
            // Nếu user mới hoặc gói cũ đã hết hạn -> Bắt đầu tính từ ngày hôm nay (ngày confirm)
            startDate = LocalDate.now();
        }

        LocalDate expiredDate = startDate.plusMonths(invoice.getGymPackage().getDurationMonths());

        invoice.setPaymentDate(LocalDate.now());
        invoice.setExpiredDate(expiredDate);
        invoice.setStatus(InvoiceStatus.PAID);

        return toResponse(invoiceRepository.save(invoice));
    }

    // ================= GET ACTIVE =================
    @Transactional(readOnly = true)
    public InvoiceResponse getMyMembership(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        Invoice invoice = invoiceRepository
                .findTopByUserAndStatusOrderByExpiredDateDesc(user, InvoiceStatus.PAID)
                .filter(i -> i.getExpiredDate() != null && i.getExpiredDate().isAfter(LocalDate.now()))
                .orElseThrow(() -> new ResourceNotFoundException("Không có gói membership nào đang active"));

        return toResponse(invoice);
    }

    // ================= CHECK ACTIVE (GỘP LOGIC) =================
    @Transactional(readOnly = true)
    public boolean isUserActive(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        // 1. Kiểm tra tài khoản có bị khóa không
        if (user.getStatus() != UserStatus.ACTIVE) {
            return false;
        }

        // 2. Kiểm tra dựa trên lịch sử Invoice (Nguồn chân lý chuẩn xác nhất)
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