package com.fitness.gymManagementSystem.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.dto.PackageRequest;
import com.fitness.gymManagementSystem.dto.PackageResponse;
import com.fitness.gymManagementSystem.service.PackageService;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // TỐI ƯU: Chỉ ADMIN mới được thêm Gói tập
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> addPackage(@Valid @RequestBody PackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.addPackage(request));
    }

    // TỐI ƯU: Ai cũng xem được danh sách gói tập (hoặc thêm isAuthenticated tùy nghiệp vụ)
    @GetMapping
    public ResponseEntity<List<PackageResponse>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.getPackageById(id));
    }

    // TỐI ƯU: Chỉ ADMIN mới được sửa
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody PackageRequest request) {
        return ResponseEntity.ok(packageService.updatePackage(id, request));
    }

    // TỐI ƯU: Chỉ ADMIN mới được xóa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}