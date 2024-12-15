package com.example.fitness_club.Repositories;

import com.example.fitness_club.Models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    UserSubscription findByUserIdAndSubscriptionId(Long userId, int subscriptionId);

    @Query(value = "SELECT DATE_FORMAT(purchase_date, '%Y-%m') AS month FROM user_subscription GROUP BY month", nativeQuery = true)
    List<String> getMonths();

    @Query(value = "SELECT COUNT(*) FROM user_subscription GROUP BY DATE_FORMAT(purchase_date, '%Y-%m')", nativeQuery = true)
    List<Long> getSubscriptionCounts();
}

