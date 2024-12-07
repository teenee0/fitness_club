package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
