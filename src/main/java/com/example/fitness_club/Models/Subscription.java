package com.example.fitness_club.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String subscription_type;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private String description;

    public List<UserSubscription> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscription> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }

    @Column(nullable = false)
    private int durationDays;


    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<UserSubscription> userSubscriptions;

    public Subscription() {
    }

    public Subscription(String subscription_type) {
        this.subscription_type = subscription_type;

    }


    // Геттеры и сеттеры
    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }



}
