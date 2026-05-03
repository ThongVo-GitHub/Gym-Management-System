package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    // ===== THÊM HÀM NÀY =====
    // Lấy danh sách lớp do HLV dạy, sắp xếp lịch học từ gần nhất đến xa nhất
    List<GymClass> findByTrainer_IdOrderByDateAscStartTimeAsc(Long trainerId);
    
    @Modifying
    @Query("""
        UPDATE GymClass g 
        SET g.currentCapacity = g.currentCapacity + 1,
            g.status = CASE WHEN (g.currentCapacity + 1) >= g.maxCapacity THEN com.fitness.gymManagementSystem.entity.ClassStatus.FULL ELSE g.status END
        WHERE g.id = :id AND g.currentCapacity < g.maxCapacity
    """)
    int incrementCapacitySafe(@Param("id") Long id);

    
    @Modifying
    @Query("""
        UPDATE GymClass g 
        SET g.currentCapacity = g.currentCapacity - 1,
            g.status = com.fitness.gymManagementSystem.entity.ClassStatus.OPEN
        WHERE g.id = :id AND g.currentCapacity > 0
    """)
    int decrementCapacitySafe(@Param("id") Long id);

    
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

    // Tìm các booking của User và JOIN FETCH luôn với bảng GymClass để tránh bị gọi DB nhiều lần
    @Query("SELECT b.gymClass FROM ClassBooking b JOIN FETCH b.gymClass.trainer WHERE b.user.id = :userId ORDER BY b.gymClass.date ASC, b.gymClass.startTime ASC")
    List<GymClass> findBookedClassesByUserId(@Param("userId") Long userId);
}
