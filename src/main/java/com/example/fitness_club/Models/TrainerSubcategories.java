package com.example.fitness_club.Models;

import jakarta.persistence.*;

@Entity
public class TrainerSubcategories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainers trainer;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategories subcategory;

    public TrainerSubcategories() {
    }

    public TrainerSubcategories(int id, Trainers trainer, Subcategories subcategory) {
        this.id = id;
        this.trainer = trainer;
        this.subcategory = subcategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
