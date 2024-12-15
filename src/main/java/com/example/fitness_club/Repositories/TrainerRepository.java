package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Trainers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainers, Long> {
    @Query("""
    SELECT DISTINCT t 
    FROM Trainers t
    JOIN FETCH t.trainerSubcategories ts
    JOIN FETCH ts.subcategory sc
    JOIN FETCH sc.specialization sp
    WHERE sp.name = :specializationName
""")
    List<Trainers> findTrainersBySpecialization(@Param("specializationName") String specializationName);


    Optional<Trainers> findByPhoneNumber(String phoneNumber);
    @Query("""
    SELECT DISTINCT t 
    FROM Trainers t
    JOIN FETCH t.trainerSubcategories ts
    JOIN FETCH ts.subcategory sc
    JOIN FETCH sc.specialization sp
    WHERE sp.name = :specializationName and t.is_on_main = true
""")
    List<Trainers> findBySpecializationAndIsOnMainTrue(@Param("specializationName") String specializationName);

    List<Trainers> findByNameContainingIgnoreCase(String name);
}
