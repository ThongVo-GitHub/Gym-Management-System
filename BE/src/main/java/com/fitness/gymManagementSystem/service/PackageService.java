package com.fitness.gymManagementSystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.gymManagementSystem.dto.PackageRequest;
import com.fitness.gymManagementSystem.dto.PackageResponse;
import com.fitness.gymManagementSystem.entity.GymPackage;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.PackageRepository;
import com.fitness.gymManagementSystem.repository.InvoiceRepository; // Bổ sung

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final InvoiceRepository invoiceRepository; // Bổ sung để check constraint

    public PackageService(PackageRepository packageRepository,
                        InvoiceRepository invoiceRepository) {
        this.packageRepository = packageRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public PackageResponse addPackage(PackageRequest request) {
        if (packageRepository.existsByPackageName(request.packageName())) {
            throw new DuplicateResourceException("Tên gói đã tồn tại"); // Đồng bộ Exception
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
                .orElseThrow(() -> new ResourceNotFoundException("Package", id)); // Đồng bộ Exception
    }

    @Transactional
    public PackageResponse updatePackage(Long id, PackageRequest request) {
        GymPackage existing = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package", id));

        // TỐI ƯU: Check trùng tên khi update (nhưng bỏ qua chính nó)
        if (!existing.getPackageName().equalsIgnoreCase(request.packageName()) &&
            packageRepository.existsByPackageName(request.packageName())) {
            throw new DuplicateResourceException("Tên gói tập này đã được sử dụng cho gói khác");
        }

        existing.setPackageName(request.packageName());
        existing.setPrice(request.price());
        existing.setDurationMonths(request.durationMonths());
        existing.setDescription(request.description());

        return toResponse(packageRepository.save(existing));
    }

    @Transactional
    public void deletePackage(Long id) {
        if (!packageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Package", id);
        }

        // TỐI ƯU BẢO VỆ DỮ LIỆU: Không cho phép xóa nếu đã có hóa đơn
        if (invoiceRepository.existsByGymPackageId(id)) {
            throw new IllegalStateException("Không thể xóa gói tập này vì đã có hội viên đăng ký (tồn tại hóa đơn). Hãy cân nhắc tính năng ẩn gói tập.");
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