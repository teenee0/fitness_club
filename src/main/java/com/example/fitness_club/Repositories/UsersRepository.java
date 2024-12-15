package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByPhoneNumber(String phoneNumber); // Метод для поиска по номеру телефона

    @Query("SELECT u FROM Users u " +
            "JOIN UserTrainer ut ON u.id = ut.user.id " +
            "JOIN Trainers t ON ut.trainer.id = t.id " +
            "WHERE t.id = :trainerId")
    List<Users> findUsersByTrainerId(@Param("trainerId") Long trainerId);

}
