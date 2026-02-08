package com.example.foodplaner.homescreen.view;

import com.example.foodplaner.model.MealDTO;

import java.util.List;

public interface ViewInterface {
    void displayRandomMeal(MealDTO meal);
    void displayAllMeals(List<MealDTO> meals);
    void showMealDetails(String mealID);
    void showLogoutSuccess();
    void navigateToMealDetails(MealDTO meal);
    void showError(String error);
    void onProssessing();
    void onProcessingEnd();
    void displayFavoriteMeals(List<MealDTO> favorites);
}
