package com.example.fitness_club.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class GroupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainer_subcategory_id", nullable = false)
    @JsonIgnore
    private TrainerSubcategories trainerSubcategories;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalTime duration;

    @Column(nullable = false)
    private String hallNumber;

    @Column(nullable = false)
    private int maxParticipants;

    public GroupSchedule() {}

    public GroupSchedule(LocalDateTime startTime, String hallNumber, int maxParticipants, LocalTime duration, TrainerSubcategories trainerSubcategories) {
        this.startTime = startTime;
        this.duration = duration;
        this.hallNumber = hallNumber;
        this.maxParticipants = maxParticipants;
        this.trainerSubcategories = trainerSubcategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainerSubcategories getTrainerSubcategories() {
        return trainerSubcategories;
    }

    public void setTrainerSubcategories(TrainerSubcategories trainerSubcategories) {
        this.trainerSubcategories = trainerSubcategories;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(String hallNumber) {
        this.hallNumber = hallNumber;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
