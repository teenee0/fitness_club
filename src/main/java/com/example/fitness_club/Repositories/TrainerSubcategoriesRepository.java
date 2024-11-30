package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Subcategories;
import com.example.fitness_club.Models.TrainerSubcategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrainerSubcategoriesRepository extends JpaRepository<TrainerSubcategories, Integer> {

    @Query("SELECT ts.subcategory FROM TrainerSubcategories ts WHERE ts.trainer.id = :trainerId")
    List<Subcategories> findSubcategoriesByTrainerId(@Param("trainerId") Long trainerId);

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    @Modifying
    @Query("DELETE FROM TrainerSubcategories ts WHERE ts.trainer.id = :trainerId AND ts.subcategory.id IN :subcategoryIds")
    void deleteByTrainerIdAndSubcategoryIds(@Param("trainerId") Long trainerId, @Param("subcategoryIds") List<Long> subcategoryIds);

    @Query("SELECT ts FROM TrainerSubcategories ts WHERE ts.subcategory.id = :subcategoryId")
    List<TrainerSubcategories> findBySubcategoryId(@Param("subcategoryId") Long subcategoryId);

    @Query("SELECT ts FROM TrainerSubcategories ts WHERE ts.trainer.id = :trainerId AND ts.subcategory.id = :subcategoryId")
    TrainerSubcategories findByTrainerIdAndSubcategoryId(@Param("trainerId") Long trainerId, @Param("subcategoryId") Long subcategoryIds);
}
