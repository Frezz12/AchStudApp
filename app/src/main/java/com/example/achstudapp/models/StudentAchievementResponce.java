package com.example.achstudapp.models;

public class StudentAchievementResponce {
    private int id;
    private String uuid;
    private User student;
    private AchievementItem achievement;

    public int getId() {return id;}

    public User getUser() {return student;}
    public AchievementItem getAchievementItem() {return achievement;}

}
