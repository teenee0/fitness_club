package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    // Здесь вы можете добавить свои собственные методы для работы с данными
}

