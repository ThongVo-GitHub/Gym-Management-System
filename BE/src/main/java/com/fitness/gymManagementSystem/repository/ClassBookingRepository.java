package com.fitness.gymManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.fitness.gymManagementSystem.entity.ClassBooking;
import com.fitness.gymManagementSystem.entity.GymClass;

@Repository
public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

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

    Optional<ClassBooking> findByUser_IdAndGymClass_Id(Long userId, Long gymClassId);


    @Query("SELECT b FROM ClassBooking b JOIN FETCH b.user WHERE b.gymClass.id = :gymClassId")
    List<ClassBooking> findMembersByGymClassId(@Param("gymClassId") Long gymClassId);

    List<ClassBooking> findByUser_Id(Long userId);

    List<ClassBooking> findByGymClass_Id(Long gymClassId);

    @Query("""
        SELECT b.gymClass FROM ClassBooking b 
        WHERE b.user.id = :userId 
        ORDER BY b.gymClass.date ASC, b.gymClass.startTime ASC
    """)
    List<GymClass> findBookedClassesByUserId(@Param("userId") Long userId);

    
}