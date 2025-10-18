package com.example.achstudapp.models;

public class AchievementItem {
    private int id;
    private String uuid;
    private String title;
    private String description;
    private int starPoints;

    public AchievementItem(String title, String description, int starPoints) {
        this.title = title;
        this.description = description;
        this.starPoints = starPoints;
    }

    public int getId() {return id;}

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStarPoints() { return starPoints; }
}
