package com.fitness.gymManagementSystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fitness.gymManagementSystem.dto.PackageRequest;
import com.fitness.gymManagementSystem.dto.PackageResponse;
import com.fitness.gymManagementSystem.entity.GymPackage;
import com.fitness.gymManagementSystem.repository.PackageRepository;

@Service
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Transactional
    public PackageResponse addPackage(PackageRequest request) {
        if (packageRepository.existsByPackageName(request.packageName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên gói đã tồn tại");
        }
        GymPackage pack = toEntity(request);
        return toResponse(packageRepository.save(pack));
    }

    @Transactional(readOnly = true)
    public List<PackageResponse> getAllPackages() {
        return packageRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PackageResponse getPackageById(Long id) {
        return packageRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy gói tập"));
    }

    @Transactional
    public PackageResponse updatePackage(Long id, PackageRequest request) {
        GymPackage existing = packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy gói tập"));

        existing.setPackageName(request.packageName());
        existing.setPrice(request.price());
        existing.setDurationMonths(request.durationMonths());
        existing.setDescription(request.description());

        return toResponse(packageRepository.save(existing));
    }

    @Transactional
    public void deletePackage(Long id) {
        if (!packageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy gói tập");
        }
        packageRepository.deleteById(id);
    }

    // ── helpers ─────────────────────────────────────────────────────────────
    private GymPackage toEntity(PackageRequest req) {
        GymPackage pack = new GymPackage();
        pack.setPackageName(req.packageName());
        pack.setPrice(req.price());
        pack.setDurationMonths(req.durationMonths());
        pack.setDescription(req.description());
        return pack;
    }

    public PackageResponse toResponse(GymPackage p) {
        return new PackageResponse(
            p.getId(),
            p.getPackageName(),
            p.getPrice(),
            p.getDurationMonths(),
            p.getDescription()
        );
    }
}