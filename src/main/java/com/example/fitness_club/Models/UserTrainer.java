package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_trainer")
public class UserTrainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "use_id", nullable = false) // Внешний ключ на таблицу Users
    private Users user;

    @ManyToOne
    @JoinColumn(name = "trainers_id", nullable = false) // Внешний ключ на таблицу Trainers
    private Trainers trainer;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Дополнительное поле

    public UserTrainer() {}

    public UserTrainer(Users user, Trainers trainer) {
        this.user = user;
        this.trainer = trainer;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Trainers getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainers trainer) {
        this.trainer = trainer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
