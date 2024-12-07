package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.UserTrainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTrainersRepository extends JpaRepository<UserTrainer, Long> {
    @Query("SELECT ut FROM UserTrainer ut WHERE ut.user.id = :userId AND ut.trainer.id = :trainerId")
    List<UserTrainer> findByUserIdAndTrainerId(@Param("userId") Long userId, @Param("trainerId") Long trainerId);

}
