package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainerSubcategories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Изменено на Long

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainers trainer;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategories subcategory;

    @OneToMany(mappedBy = "trainerSubcategories", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupSchedule> groupSchedules = new ArrayList<>();

    public TrainerSubcategories() {}

    public TrainerSubcategories(Trainers trainer, Subcategories subcategory) {
        this.trainer = trainer;
        this.subcategory = subcategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trainers getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainers trainer) {
        this.trainer = trainer;
    }

    public Subcategories getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategories subcategory) {
        this.subcategory = subcategory;
    }

    public List<GroupSchedule> getGroupSchedules() {
        return groupSchedules;
    }

    public void setGroupSchedules(List<GroupSchedule> groupSchedules) {
        this.groupSchedules = groupSchedules;
    }

    // Метод для добавления GroupSchedule
    public void addGroupSchedule(GroupSchedule groupSchedule) {
        groupSchedules.add(groupSchedule);
        groupSchedule.setTrainerSubcategories(this);
    }

    // Метод для удаления GroupSchedule
    public void removeGroupSchedule(GroupSchedule groupSchedule) {
        groupSchedules.remove(groupSchedule);
        groupSchedule.setTrainerSubcategories(null);
    }
}
