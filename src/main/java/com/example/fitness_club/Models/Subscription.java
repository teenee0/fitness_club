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
    private double price;
    @Column(nullable = false)
    private boolean unlimited_use;
    @Column(nullable = false)
    private boolean mystery_box;
    @Column(nullable = false)
    private boolean sauna;
    @Column(nullable = false)
    private boolean inbody;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean martial_arts;
    @Column(nullable = false)
    private boolean group_programs;
    @Column(nullable = false)
    private int durationDays;
    @Column(nullable = false)
    private boolean is_on_main;
    @Column(nullable = false)
    private boolean can_buy;

    public List<UserSubscription> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscription> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<UserSubscription> userSubscriptions;

    public Subscription() {
    }

    public Subscription(String subscription_type) {
        this.subscription_type = subscription_type;

    }


    // Геттеры и сеттеры


    public boolean isCan_buy() {
        return can_buy;
    }

    public void setCan_buy(boolean can_buy) {
        this.can_buy = can_buy;
    }

    public boolean isIs_on_main() {
        return is_on_main;
    }

    public void setIs_on_main(boolean is_on_main) {
        this.is_on_main = is_on_main;
    }

    public boolean isMartial_arts() {
        return martial_arts;
    }

    public void setMartial_arts(boolean martial_arts) {
        this.martial_arts = martial_arts;
    }

    public boolean isGroup_programs() {
        return group_programs;
    }

    public void setGroup_programs(boolean group_programs) {
        this.group_programs = group_programs;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isUnlimited_use() {
        return unlimited_use;
    }

    public void setUnlimited_use(boolean unlimited_use) {
        this.unlimited_use = unlimited_use;
    }

    public boolean isMystery_box() {
        return mystery_box;
    }

    public void setMystery_box(boolean mystery_box) {
        this.mystery_box = mystery_box;
    }

    public boolean isSauna() {
        return sauna;
    }

    public void setSauna(boolean sauna) {
        this.sauna = sauna;
    }

    public boolean isInbody() {
        return inbody;
    }

    public void setInbody(boolean inbody) {
        this.inbody = inbody;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public double getPrice() {
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
