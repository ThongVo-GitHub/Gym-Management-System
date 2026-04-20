package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.*;
import java.util.List;

import com.fitness.gymManagementSystem.entity.ClassBooking;

@Repository
public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

    // Dùng ID để tránh load full entity
    boolean existsByUser_IdAndGymClass_Id(Long userId, Long gymClassId);

    @Query("""
        SELECT b FROM ClassBooking b
        WHERE b.user.id = :userId
        AND b.gymClass.date = :date
        AND b.gymClass.startTime < :endTime
        AND b.gymClass.endTime > :startTime
    """)
    List<ClassBooking> findUserConflicts(
        @Param("userId")    Long userId,
        @Param("date")      LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime")   LocalTime endTime
    );

    List<ClassBooking> findByUser_Id(Long userId);

    List<ClassBooking> findByGymClass_Id(Long gymClassId);
    
}