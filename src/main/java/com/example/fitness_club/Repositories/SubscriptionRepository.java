package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    @Query("SELECT s FROM Subscription s WHERE s.is_on_main = true")
    List<Subscription> findByIsOnMainTrue();
    @Query("SELECT s FROM Subscription s WHERE s.can_buy = true")
    List<Subscription> findByCanBuy();
}

