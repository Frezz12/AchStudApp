package com.example.achstudapp.models;

public class AchievementWrapper {
    private int id;
    private String uuid;
    private String status;
    private AchievementItem achievement;

    public AchievementItem getAchievement() { return achievement; }
    public String getStatus() { return status; }
}
