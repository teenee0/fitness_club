package com.example.fitness_club.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;



    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private LocalDateTime endDate; // Дата окончания абонемента

    public UserSubscription() {}

    public UserSubscription(Users user, Subscription subscription, LocalDateTime purchaseDate) {
        this.user = user;
        this.subscription = subscription;
        this.purchaseDate = purchaseDate;
        this.endDate = calculateEndDate(); // Рассчитываем дату окончания
    }

    private LocalDateTime calculateEndDate() {
        return purchaseDate.plusDays(subscription.getDurationDays());
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }


    public LocalDateTime getEndDate() {
        return endDate;
    }
}

