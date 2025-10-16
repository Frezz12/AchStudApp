package com.example.achstudapp.models;


import java.util.List;

public class User {
    private int id;
    private String uuid, email, firstname, lastname, surname, college, role;
    private List<AchievementWrapper> achievements;

    public User(String email, String firstname, String lastname, String surname, String college) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.college = college;
    }


    public String getFirstname() { return firstname; }

    public int getId() {return id;}
    public String getEmail() {return email;}
    public String getLastname() {return lastname;}
    public String getSurname() {return surname;}
    public String getCollege() {return college;}
    public String getRole() {return role;}
    public List<AchievementWrapper> getAchievements() { return achievements; }
}