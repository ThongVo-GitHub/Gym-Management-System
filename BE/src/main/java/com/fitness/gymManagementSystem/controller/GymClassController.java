package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.*;
import com.fitness.gymManagementSystem.service.GymClassService;
import com.fitness.gymManagementSystem.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class GymClassController {

    private final GymClassService gymClassService;
    private final BookingService bookingService;

    public GymClassController(GymClassService gymClassService,
                            BookingService bookingService) {
        this.gymClassService = gymClassService;
        this.bookingService = bookingService;
    }

    // =========================
    // QUẢN LÝ LỚP HỌC (ADMIN/TRAINER)
    // =========================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<GymClassResponse> createClass(
            @Valid @RequestBody CreateClassRequest request,
            Authentication authentication) {

        GymClassResponse response = gymClassService.createClass(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<GymClassResponse> updateClass(
            @PathVariable Long id,
            @Valid @RequestBody CreateClassRequest request,
            Authentication authentication) {

        GymClassResponse response = gymClassService.updateClass(id, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Void> deleteClass(
            @PathVariable Long id,
            Authentication authentication) {

        gymClassService.deleteClass(id, authentication.getName());
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    // =========================
    // TƯƠNG TÁC CỦA USER (BOOKING & CÁ NHÂN)
    // =========================

    @PostMapping("/{id}/book")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponse> book(
            @PathVariable Long id,
            Authentication authentication) {

        BookingResponse response = bookingService.book(id, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            Authentication authentication) {

        bookingService.cancel(id, authentication.getName());
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    /**
     * TÍNH NĂNG MỚI: Lấy lịch tập cá nhân (Cho cả User và Trainer)
     * Endpoint: GET /api/classes/my-schedule
     */
    @GetMapping("/my-schedule")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GymClassResponse>> getMySchedule(Authentication authentication) {
        // 1. Trích xuất định danh (username) từ JWT Token đã được xác thực
        // Tuyệt đối không truyền userId trên URL để tránh lỗ hổng IDOR
        String currentUsername = authentication.getName();

        // 2. Gọi xuống tầng Service (Logic phân luồng Trainer/Học viên đã được xử lý ở đây)
        List<GymClassResponse> mySchedule = gymClassService.getMySchedule(currentUsername);

        // 3. Trả về Frontend với HTTP 200 OK
        return ResponseEntity.ok(mySchedule);
    }


    // =========================
    // TRUY VẤN CHUNG (PUBLIC / ADMIN)
    // =========================

    // TỐI ƯU: Chuyển từ List sang Page để bảo vệ RAM của Server
    @GetMapping
    public ResponseEntity<Page<GymClassResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(gymClassService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymClassResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gymClassService.getById(id));
    }

    @GetMapping("/{id}/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')") 
    public ResponseEntity<List<UserResponse>> getMembers(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                bookingService.getMembersByClass(id, authentication.getName())
        );
    }
}