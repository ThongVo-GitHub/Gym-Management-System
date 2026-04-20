package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitness.gymManagementSystem.entity.GymPackage;

@Repository
public interface PackageRepository extends JpaRepository<GymPackage, Long> {

    boolean existsByPackageName(String packageName);
}