package com.example.foodplaner.homescreen.presenter;

import com.example.foodplaner.model.MealDTO;

public interface PresenterInterface {
    void requestRandomMeal(boolean forceRefresh);
    void getAllMeals(boolean forceRefresh);
    void navigateToMealDetails(String mealID);
    void logoutUser();
    void navigateToMealDetails(MealDTO meal);
}
