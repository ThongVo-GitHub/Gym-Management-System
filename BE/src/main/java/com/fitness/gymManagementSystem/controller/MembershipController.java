package com.fitness.gymManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.BuyMembershipRequest;
import com.fitness.gymManagementSystem.entity.Invoice;
import com.fitness.gymManagementSystem.service.MembershipService;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    // POST: /api/membership/buy
    @PostMapping("/buy")
    public ResponseEntity<?> buyMembership(
            @RequestBody BuyMembershipRequest request,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        if (request.getPackageId() == null || request.getPackageId().isEmpty()) {
            return ResponseEntity.badRequest().body("PackageId is required");
        }

        try {
            String username = authentication.getName();

            Invoice invoice = membershipService.buyMembership(request, username);

            return ResponseEntity.ok(invoice);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}