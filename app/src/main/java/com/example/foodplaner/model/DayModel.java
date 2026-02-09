package com.example.foodplaner.model;

public class DayModel {
    private String dayName;
    private String dayNumber;
    private String fullDate; // Add this

    public DayModel(String dayName, String dayNumber, String fullDate) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.fullDate = fullDate;
    }

    public String getDayName() { return dayName; }
    public String getDayNumber() { return dayNumber; }
    public String getFullDate() { return fullDate; } // This is what the Fragment calls
}