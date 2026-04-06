package com.fitness.gymManagementSystem.service;

import com.fitness.gymManagementSystem.dto.BuyMembershipRequest;
import com.fitness.gymManagementSystem.entity.Package;
import com.fitness.gymManagementSystem.entity.Invoice;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.entity.InvoiceStatus;
import com.fitness.gymManagementSystem.repository.PackageRepository;
import com.fitness.gymManagementSystem.repository.InvoiceRepository;
import com.fitness.gymManagementSystem.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MembershipService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Invoice buyMembership(BuyMembershipRequest request, String username) {

        // 1. Lấy gói tập
        Package gymPackage = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói tập"));

        // 2. Tính ngày hết hạn
        long daysToAdd = (long) (gymPackage.getDurationMonths() * 30);
        LocalDate expiredDate = LocalDate.now().plusDays(daysToAdd);

        // 3. Tạo invoice (CHƯA thanh toán)
        Invoice invoice = new Invoice();
        invoice.setId("INV_" + System.currentTimeMillis());
        invoice.setUserId(username);
        invoice.setPackageId(gymPackage.getId());
        invoice.setPaymentMethod(request.getPaymentMethod());
        invoice.setPaymentDate(LocalDate.now());
        invoice.setExpiredDate(expiredDate);

        // 🔥 QUAN TRỌNG: ban đầu là PENDING
        invoice.setStatus(InvoiceStatus.PENDING);

        // 4. Lưu invoice
        return invoiceRepository.save(invoice);
    }


    @Transactional
    public void confirmPayment(String invoiceId) {

        // 1. Lấy invoice
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy invoice"));

        // 2. Update trạng thái
        invoice.setStatus(InvoiceStatus.PAID);

        // 3. Cập nhật user
        User user = userRepository.findByUsername(invoice.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setStatus(UserStatus.ACTIVE);
        user.setGymPackage(invoice.getPackageId());
        user.setExpiryDate(invoice.getExpiredDate());

        userRepository.save(user);
        invoiceRepository.save(invoice);
    }
}