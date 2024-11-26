package com.example.fitness_club.Services;

import com.example.fitness_club.Models.Subscription;
import com.example.fitness_club.Models.UserSubscription;
import com.example.fitness_club.Models.Users;
import com.example.fitness_club.Repositories.SubscriptionRepository;
import com.example.fitness_club.Repositories.UsersRepository;
import com.example.fitness_club.Repositories.UserSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSubscriptionService {

    private final UsersRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionService(UsersRepository userRepository, SubscriptionRepository subscriptionRepository, UserSubscriptionRepository userSubscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    public void addUserSubscription(Long userId, int subscriptionId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        LocalDateTime purchaseDate = LocalDateTime.now();
        UserSubscription userSubscription = new UserSubscription(user, subscription, purchaseDate);

        userSubscriptionRepository.save(userSubscription);
    }
}
