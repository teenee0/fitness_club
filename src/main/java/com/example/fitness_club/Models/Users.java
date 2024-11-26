package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;


    @Column(nullable = false)
    private String password; // Пароль пользователя

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Роль пользователя

    public enum Role {
        CLIENT, TRAINER, ADMIN
    }

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSubscription> userSubscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTrainer> userTrainers;

    public Users() {}


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<UserTrainer> getUserTrainers() {
        return userTrainers;
    }

    public void setUserTrainers(List<UserTrainer> userTrainers) {
        this.userTrainers = userTrainers;
    }

    public Users(String name,
                 String surname,
                 String phone_number,
                 Role role,
                 String password
                 ) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phone_number;
        this.role = role;
        this.password = password;
        this.registrationDate = LocalDateTime.now();
    }


    // Геттеры и сеттеры
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

    public String getPhone_number() {
        return phoneNumber;
    }

    public void setPhone_number(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserSubscription> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscription> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }
}
