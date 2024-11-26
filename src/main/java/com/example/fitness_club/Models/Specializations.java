package com.example.fitness_club.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Specializations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Specializations() {}

    public Specializations(Long id, String name, List<Subcategories> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public List<Subcategories> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategories> subcategories) {
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "specialization")
    private List<Subcategories> subcategories;

    // Getters and Setters
}