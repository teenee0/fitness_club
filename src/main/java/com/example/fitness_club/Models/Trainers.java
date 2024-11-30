package com.example.fitness_club.Models;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Entity
public class Trainers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String Description;


    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Integer yearsOfExperience;

    @Column(nullable = false) // Необязательное поле
    private String photoPath;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<UserTrainer> userTrainers; // Связь с таблицей UserTrainer


    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<TrainerSubcategories> trainerSubcategories;



    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }



    public Trainers() {}

    // Конструкторы, геттеры, сеттер
    public Trainers(String name, String surname, String number, String password, String description, Integer yearsOfExperience) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.password = password;
        this.Description = description;
        this.yearsOfExperience = yearsOfExperience;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public List<UserTrainer> getUserTrainers() {
        return userTrainers;
    }

    public void setUserTrainers(List<UserTrainer> userTrainers) {
        this.userTrainers = userTrainers;
    }


    public List<TrainerSubcategories> getTrainerSubcategories() {
        return trainerSubcategories;
    }

    public void setTrainerSubcategories(List<TrainerSubcategories> trainerSubcategories) {
        this.trainerSubcategories = trainerSubcategories;
    }
}

