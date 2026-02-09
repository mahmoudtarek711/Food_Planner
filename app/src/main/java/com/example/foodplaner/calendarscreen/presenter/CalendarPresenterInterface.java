package com.example.foodplaner.calendarscreen.presenter;

import com.example.foodplaner.model.MealRoomDTO;

public interface CalendarPresenterInterface {
    void getMealsByDate(String date);
    void removeMeal(MealRoomDTO meal);
}
