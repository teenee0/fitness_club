package com.example.fitness_club.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name; // Название клуба

    @Column(nullable = false, unique = true)
    private String location; // Местоположение клуба

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<UserSubscription> userSubscriptions;

    public Club() {}

    public Club(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
