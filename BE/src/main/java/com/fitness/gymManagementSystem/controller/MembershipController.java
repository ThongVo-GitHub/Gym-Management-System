package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.BuyMembershipRequest;
import com.fitness.gymManagementSystem.dto.InvoiceResponse;
import com.fitness.gymManagementSystem.service.MembershipService;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    // ===== BUY =====
    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InvoiceResponse> buyMembership(
            @Valid @RequestBody BuyMembershipRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        // Sửa thành 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(
                membershipService.buyMembership(request, username)
        );
    }

    // ===== CONFIRM =====
    @PutMapping("/{invoiceId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> confirmPayment(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(
                membershipService.confirmPayment(invoiceId)
        );
    }

    // ===== GET MY MEMBERSHIP =====
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InvoiceResponse> getMyMembership(Authentication authentication) {
        return ResponseEntity.ok(
                membershipService.getMyMembership(authentication.getName())
        );
    }
}