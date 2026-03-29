package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fitness.gymManagementSystem.entity.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, String> {
}