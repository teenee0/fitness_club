package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GroupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_info_id", nullable = false) // Связь с GroupInfo
    private GroupInfo groupInfo;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false) // Связь с Trainer
    private Trainers trainer;

    @Column(nullable = false)
    private LocalDateTime startTime; // Время начала занятия

    @Column(nullable = false)
    private LocalDateTime endTime; // Время окончания занятия

    @Column(nullable = false)
    private String hallNumber; // Номер зала

    @Column(nullable = false)
    private int maxParticipants; // Максимальное количество участников

    public GroupSchedule() {}

    public GroupSchedule(GroupInfo groupInfo, Trainers trainer, LocalDateTime startTime, LocalDateTime endTime, String hallNumber, int maxParticipants) {
        this.groupInfo = groupInfo;
        this.trainer = trainer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hallNumber = hallNumber;
        this.maxParticipants = maxParticipants;
    }

    public Long getId() {
        return id;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public Trainers getTrainer() {
        return trainer;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public void setTrainer(Trainers trainer) {
        this.trainer = trainer;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setHallNumber(String hallNumber) {
        this.hallNumber = hallNumber;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
