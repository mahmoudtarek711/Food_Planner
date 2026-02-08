package com.example.foodplaner.detailscreen.presenter;

import com.example.foodplaner.model.MealDTO;

public interface MealDetailsPresenterInterface {
    void addToFavorite(MealDTO meal);
    void removeFromFavorite(MealDTO meal);
    void checkIfFavorite(String mealId);
    void addToCalendar(MealDTO meal, String date);
    void removeFromCalendar(MealDTO meal);
    void checkIfPlanned(String mealId);

}
