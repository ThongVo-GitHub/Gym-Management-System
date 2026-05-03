
package com.fitness.gymManagementSystem.repository;

import com.fitness.gymManagementSystem.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByUserIdAndGymClassId(Long userId, Long gymClassId);
}