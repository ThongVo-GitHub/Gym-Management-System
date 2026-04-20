package com.fitness.gymManagementSystem.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.*;
import com.fitness.gymManagementSystem.entity.ClassBooking;
import com.fitness.gymManagementSystem.service.GymClassService;
import com.fitness.gymManagementSystem.service.BookingService;

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

    @PostMapping
    public ResponseEntity<GymClassResponse> createClass(
            @Valid @RequestBody CreateClassRequest request,
            Principal principal) {

        return ResponseEntity.ok(
                gymClassService.mapToResponse(
                        gymClassService.createClass(request, principal.getName())
                )
        );
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<BookingResponse> book(
            @PathVariable Long id,
            Principal principal) {

        ClassBooking booking = bookingService.book(id, principal.getName());

        return ResponseEntity.ok(new BookingResponse(
                booking.getId(),
                booking.getGymClass().getId(),
                booking.getGymClass().getName(),
                "Đăng ký lớp thành công",
                booking.getBookedAt()
        ));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable Long id,
            Principal principal) {

        bookingService.cancel(id, principal.getName());
        return ResponseEntity.ok("Hủy thành công");
    }

    @GetMapping
    public ResponseEntity<List<GymClassResponse>> getAll() {
        return ResponseEntity.ok(gymClassService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymClassResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gymClassService.getById(id));
    }
}