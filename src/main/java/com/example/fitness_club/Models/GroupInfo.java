package com.example.fitness_club.Models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GroupInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name; // Название занятия

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String Description; // Описание занятия

    @OneToMany(mappedBy = "groupInfo", cascade = CascadeType.ALL)
    private List<GroupSchedule> schedules; // Связь с расписанием

    public GroupInfo() {}
    public GroupInfo(String name, String description, List<GroupSchedule> schedules) {
        this.name = name;
        this.type = description;
        this.schedules = schedules;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSchedules(List<GroupSchedule> schedules) {
        this.schedules = schedules;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<GroupSchedule> getSchedules() {
        return schedules;
    }

    public GroupInfo(String name, String description) {
        this.name = name;
        this.type = description;
    }
}
