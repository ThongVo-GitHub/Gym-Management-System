package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Đã thêm import này
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.service.CheckInService;
import com.fitness.gymManagementSystem.dto.CheckInRequest;
import com.fitness.gymManagementSystem.dto.CheckInResponse;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {
    
    private final CheckInService checkInService;

    // 🔥 BỔ SUNG: Constructor để Spring Boot tiêm (inject) dependency
    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CheckInResponse> verifyCheckIn(
            @Valid @RequestBody CheckInRequest request, 
            Authentication authentication) {
        
        // Dù là QR hay Face sau này, request vẫn sẽ truyền về định danh
        return ResponseEntity.ok(checkInService.processCheckIn(request, authentication.getName()));
    }
}