package com.fitness.gymManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.gymManagementSystem.entity.Package;
import com.fitness.gymManagementSystem.repository.PackageRepository;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    // Thêm gói tập
    public Package addPackage(Package pack) {
        return packageRepository.save(pack);
    }

    // Lấy tất cả gói tập
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    // Lấy gói theo ID
    public Package getPackageById(String id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    // Xóa gói
    public void deletePackage(String id) {
        if (!packageRepository.existsById(id)) {
            throw new RuntimeException("Package not found");
        }
        packageRepository.deleteById(id);
    }

    // Update gói
    public Package updatePackage(String id, Package newPack) {
        Package existing = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        existing.setPackageName(newPack.getPackageName());
        existing.setPrice(newPack.getPrice());
        existing.setDurationMonths(newPack.getDurationMonths());

        return packageRepository.save(existing);
    }
}