package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Competitions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    private String first_winner;

    private String first_winner_prize;

    private String second_winner;

    private String second_winner_prize;

    private String third_winner;

    private String third_winner_prize;
    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainers trainer;

    public Competitions() {
    }

    public Competitions(String name, String description, String first_winner, String second_winner, String third_winner, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.first_winner = first_winner;
        this.second_winner = second_winner;
        this.third_winner = third_winner;
        this.date = date;
    }

    public String getFirst_winner_prize() {
        return first_winner_prize;
    }

    public void setFirst_winner_prize(String first_winner_prize) {
        this.first_winner_prize = first_winner_prize;
    }

    public String getSecond_winner_prize() {
        return second_winner_prize;
    }

    public void setSecond_winner_prize(String second_winner_prize) {
        this.second_winner_prize = second_winner_prize;
    }

    public String getThird_winner_prize() {
        return third_winner_prize;
    }

    public void setThird_winner_prize(String third_winner_prize) {
        this.third_winner_prize = third_winner_prize;
    }

    public Trainers getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainers trainer) {
        this.trainer = trainer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirst_winner() {
        return first_winner;
    }

    public void setFirst_winner(String first_winner) {
        this.first_winner = first_winner;
    }

    public String getSecond_winner() {
        return second_winner;
    }

    public void setSecond_winner(String second_winner) {
        this.second_winner = second_winner;
    }

    public String getThird_winner() {
        return third_winner;
    }

    public void setThird_winner(String third_winner) {
        this.third_winner = third_winner;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Trainers getTrainers() {
        return trainer;
    }

    public void setTrainers(Trainers trainer) {
        this.trainer = trainer;
    }
}
