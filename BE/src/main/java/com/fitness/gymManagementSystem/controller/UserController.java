package com.fitness.gymManagementSystem.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.UpdateUserRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sort,
            @RequestParam(defaultValue = "asc") String order) {

        List<String> allowedSortFields = List.of("username", "email");

        if (!allowedSortFields.contains(sort)) {
            sort = "username";
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sort));

        return ResponseEntity.ok(userService.findAll(search, null, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(
                userService.update(id, request, authentication.getName())
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(
                userService.getByUsername(authentication.getName())
        );
    }
}