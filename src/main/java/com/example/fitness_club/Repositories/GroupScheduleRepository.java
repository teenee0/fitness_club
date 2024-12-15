package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
    @Query("""
    SELECT gs
    FROM GroupSchedule gs
    JOIN gs.trainerSubcategories ts
    JOIN ts.subcategory sc
    JOIN sc.specialization sp
    WHERE sp.name = :specialization
    ORDER BY gs.startTime DESC
""")
    List<GroupSchedule> findBySpecialization(@Param("specialization") String specialization);

    @Query("SELECT gs FROM GroupSchedule gs " +
            "JOIN gs.trainerSubcategories ts " +
            "JOIN ts.trainer t " +
            "WHERE t.id = :trainerId")
    List<GroupSchedule> findGroupSchedulesByTrainerId(@Param("trainerId") Long trainerId);

    @Query("SELECT gs FROM GroupSchedule gs " +
            "WHERE gs.trainerSubcategories.subcategory.specialization.name = :specialization " +
            "AND (:name IS NULL OR gs.trainerSubcategories.subcategory.id = :name) " +
            "AND (:hallNumber IS NULL OR gs.hallNumber = :hallNumber) " +
            "AND (:trainerId IS NULL OR gs.trainerSubcategories.trainer.id = :trainerId) " +
            "AND (:startDate IS NULL OR gs.startTime >= :startDate) " +
            "AND (:endDate IS NULL OR gs.startTime <= :endDate)")
    List<GroupSchedule> searchSchedules(
            @Param("specialization") String specialization,
            @Param("name") Long name, // Изменено на Long для поддержки null
            @Param("hallNumber") String hallNumber,
            @Param("trainerId") Long trainerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
