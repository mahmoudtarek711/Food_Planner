package com.example.foodplaner.calendarscreen.view;

import com.example.foodplaner.model.MealRoomDTO;

import java.util.List;


public interface CalendarViewInterface {
    void showMeals(List<MealRoomDTO> meals);
    void showRemoveSuccess();
    void showError(String msg);
    void onRemoveMealSuccess(MealRoomDTO meal);
}
