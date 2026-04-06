// package com.fitness.gymManagementSystem.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import java.time.*;
// import java.util.List;

// import com.fitness.gymManagementSystem.entity.ClassBooking;
// import com.fitness.gymManagementSystem.entity.User;
// import com.fitness.gymManagementSystem.entity.GymClass;

// public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

//     boolean existsByUserAndGymClass(User user, GymClass gymClass);

//     @Query("""
//         SELECT b FROM ClassBooking b
//         WHERE b.user.id = :userId
//         AND b.gymClass.date = :date
//         AND (
//             b.gymClass.startTime < :endTime
//             AND b.gymClass.endTime > :startTime
//         )
//     """)
//     List<ClassBooking> findUserConflicts(
//         Long userId,
//         LocalDate date,
//         LocalTime startTime,
//         LocalTime endTime
//     );
// }