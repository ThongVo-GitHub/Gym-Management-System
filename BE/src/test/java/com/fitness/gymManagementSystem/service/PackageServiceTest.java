package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import com.fitness.gymManagementSystem.dto.PackageRequest;
import com.fitness.gymManagementSystem.dto.PackageResponse;
import com.fitness.gymManagementSystem.entity.GymPackage;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.InvoiceRepository;
import com.fitness.gymManagementSystem.repository.PackageRepository;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock private PackageRepository packageRepository;
    @Mock private InvoiceRepository invoiceRepository;

    @InjectMocks private PackageService packageService;

    private GymPackage mockPackage;
    private PackageRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockPackage = new GymPackage();
        mockPackage.setId(1L);
        mockPackage.setPackageName("Gói 1 Tháng");
        mockPackage.setPrice(new BigDecimal("500000.0"));
        mockPackage.setDurationMonths(1);

        mockRequest = new PackageRequest("Gói VIP", new BigDecimal("1000000.0"), 3, "Mô tả");
    }

    // --- TEST ADD ---
    @Test
    void addPackage_DuplicateName_ThrowsException() {
        when(packageRepository.existsByPackageName("Gói VIP")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> packageService.addPackage(mockRequest));
    }

    @Test
    void addPackage_Success_ReturnsResponse() {
        when(packageRepository.existsByPackageName("Gói VIP")).thenReturn(false);
        when(packageRepository.save(any())).thenReturn(mockPackage);

        PackageResponse res = packageService.addPackage(mockRequest);
        assertNotNull(res);
        assertEquals("Gói 1 Tháng", res.packageName());
    }

    // --- TEST UPDATE ---
    @Test
    void updatePackage_NotFound_ThrowsException() {
        when(packageRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> packageService.updatePackage(1L, mockRequest));
    }

    @Test
    void updatePackage_DuplicateNewName_ThrowsException() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage)); // Tên cũ: Gói 1 Tháng
        when(packageRepository.existsByPackageName("Gói VIP")).thenReturn(true); // Tên mới trùng với gói khác

        assertThrows(DuplicateResourceException.class, () -> packageService.updatePackage(1L, mockRequest));
    }

    @Test
    void updatePackage_Success() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        when(packageRepository.existsByPackageName("Gói VIP")).thenReturn(false);
        when(packageRepository.save(any())).thenReturn(mockPackage);

        PackageResponse res = packageService.updatePackage(1L, mockRequest);
        assertNotNull(res);
    }

    // --- TEST DELETE ---
    @Test
    void deletePackage_NotFound_ThrowsException() {
        when(packageRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> packageService.deletePackage(1L));
    }

    @Test
    void deletePackage_HasInvoice_ThrowsException() {
        when(packageRepository.existsById(1L)).thenReturn(true);
        when(invoiceRepository.existsByGymPackageId(1L)).thenReturn(true); // Đang có người mua

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> packageService.deletePackage(1L));
        assertTrue(ex.getMessage().contains("Không thể xóa gói tập này"));
    }

    @Test
    void deletePackage_Success() {
        when(packageRepository.existsById(1L)).thenReturn(true);
        when(invoiceRepository.existsByGymPackageId(1L)).thenReturn(false);

        packageService.deletePackage(1L);
        verify(packageRepository).deleteById(1L); // Đảm bảo lệnh xóa DB được gọi
    }
    // --- BỔ SUNG ĐỂ ĐẠT 100% PACKAGE SERVICE ---
    @Test
    void getAllPackages_ReturnsList() {
        when(packageRepository.findAll()).thenReturn(List.of(mockPackage));
        List<PackageResponse> res = packageService.getAllPackages();
        assertFalse(res.isEmpty());
    }

    @Test
    void getPackageById_Success() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        PackageResponse res = packageService.getPackageById(1L);
        assertNotNull(res);
    }

    @Test
    void updatePackage_KeepSameName_Success() {
        // Test nhánh cập nhật nhưng KHÔNG đổi tên gói (vượt qua lệnh if check trùng)
        when(packageRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        
        PackageRequest sameNameReq = new PackageRequest(
            "Gói 1 Tháng", new java.math.BigDecimal("600000"), 1, "Mô tả mới"
        );
        when(packageRepository.save(any())).thenReturn(mockPackage);

        PackageResponse res = packageService.updatePackage(1L, sameNameReq);
        assertNotNull(res);
    }
}