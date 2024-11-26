package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByPhoneNumber(String phoneNumber); // Метод для поиска по номеру телефона
}
