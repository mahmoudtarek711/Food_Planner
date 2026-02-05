package com.example.foodplaner.model;

public class DayModel {
    private String dayName;
    private String dayNumber;

    public DayModel(String dayName, String dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
    }
    public String getDayName() { return dayName; }
    public String getDayNumber() { return dayNumber; }
}