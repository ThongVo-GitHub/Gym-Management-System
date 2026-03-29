package com.fitness.gymManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fitness.gymManagementSystem.entity.Package;
import com.fitness.gymManagementSystem.service.PackageService;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "http://localhost:3000")
public class PackageController {

    @Autowired
    private PackageService packageService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> addPackage(@RequestBody Package pack) {

        if (pack.getPackageName() == null || pack.getPackageName().isEmpty()) {
            return ResponseEntity.badRequest().body("Package name is required");
        }

        Package saved = packageService.addPackage(pack);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(packageService.getPackageById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(
            @PathVariable String id,
            @RequestBody Package pack) {
        try {
            return ResponseEntity.ok(packageService.updatePackage(id, pack));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable String id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}