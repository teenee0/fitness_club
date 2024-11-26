package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    // Здесь вы можете добавить свои собственные методы для работы с данными
}

