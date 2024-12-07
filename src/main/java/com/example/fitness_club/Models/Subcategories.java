package com.example.fitness_club.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Subcategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    @JsonIgnore
    private Specializations specialization;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrainerSubcategories> trainerSubcategories;


    public Subcategories() {}


    public Subcategories(Long id, String name, Specializations specialization, List<Trainers> trainers, List<GroupSchedule> groupSchedules) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.trainerSubcategories = new ArrayList<>();

    }

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

    public Specializations getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specializations specialization) {
        this.specialization = specialization;
    }

    public List<TrainerSubcategories> getTrainerSubcategories() {
        return trainerSubcategories;
    }

    public void setTrainerSubcategories(List<TrainerSubcategories> trainerSubcategories) {
        this.trainerSubcategories = trainerSubcategories;
    }

    // Getters and Setters
}
