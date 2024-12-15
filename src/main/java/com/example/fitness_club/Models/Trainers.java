package com.example.fitness_club.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String Description;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean is_on_main;


    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Integer yearsOfExperience;

    @Column(nullable = false) // Необязательное поле
    private String photoPath;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserTrainer> userTrainers; // Связь с таблицей UserTrainer


    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrainerSubcategories> trainerSubcategories;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Competitions> competitions;



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
        this.phoneNumber = number;
        this.password = password;
        this.Description = description;
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIs_on_main() {
        return is_on_main;
    }

    public void setIs_on_main(boolean is_on_main) {
        this.is_on_main = is_on_main;
    }

    public List<Competitions> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<Competitions> competitions) {
        this.competitions = competitions;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setNumber(String number) {
        this.phoneNumber = number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

