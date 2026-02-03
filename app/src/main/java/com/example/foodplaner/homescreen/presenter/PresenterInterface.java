package com.example.foodplaner.homescreen.presenter;

import com.example.foodplaner.model.MealDTO;

public interface PresenterInterface {
    void requestRandomMeal();
    void getAllMeals();
    void navigateToMealDetails(String mealID);
    void logoutUser();
    void navigateToMealDetails(MealDTO meal);
}
