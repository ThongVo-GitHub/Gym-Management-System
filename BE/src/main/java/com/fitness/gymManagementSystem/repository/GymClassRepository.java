package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.*;
import java.util.List;

import com.fitness.gymManagementSystem.entity.ClassStatus;
import com.fitness.gymManagementSystem.entity.GymClass;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    List<GymClass> findByDate(LocalDate date);

    List<GymClass> findByStatus(ClassStatus status);

    @Query("""
        SELECT g FROM GymClass g
        WHERE g.trainer.id = :trainerId
        AND g.date = :date
        AND g.startTime < :endTime
        AND g.endTime > :startTime
    """)
    List<GymClass> findConflictingClasses(
        @Param("trainerId") Long trainerId,
        @Param("date")      LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime")   LocalTime endTime
    );
}
