package com.example.achstudapp.models;

public class AchievementsItemRequest {
    private String title;
    private String description;
    private int starPoints;

    public AchievementsItemRequest(String title, String description, int starPoints) {
        this.title = title;
        this.description = description;
        this.starPoints = starPoints;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStarPoints() { return starPoints; }
}
