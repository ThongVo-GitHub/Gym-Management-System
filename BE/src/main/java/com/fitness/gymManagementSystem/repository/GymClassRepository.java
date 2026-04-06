// package com.fitness.gymManagementSystem.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
// import org.springframework.data.jpa.repository.Query;
// import java.time.*;
// import java.util.List;

// import com.fitness.gymManagementSystem.entity.GymClass;

// @Repository
// public interface GymClassRepository extends JpaRepository<GymClass, Long> {

//     List<GymClass> findByDate(LocalDate date);

//     @Query("""
//         SELECT g FROM GymClass g
//         WHERE g.trainer.id = :trainerId
//         AND g.date = :date
//         AND (
//             (g.startTime < :endTime AND g.endTime > :startTime)
//         )
//     """)
//     List<GymClass> findConflictingClasses(
//         Long trainerId,
//         LocalDate date,
//         LocalTime startTime,
//         LocalTime endTime
//     );
// }