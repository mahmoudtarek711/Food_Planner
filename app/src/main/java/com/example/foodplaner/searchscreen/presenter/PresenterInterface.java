package com.example.foodplaner.searchscreen.presenter;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;

import java.util.List;

public interface PresenterInterface {
    void filterByCountry(String areaName);
    void filterByCategory(String categoryName);
    void filterByIngredient(String ingredientName);
    void getListOfArea();
    void getListOfCatgories();
    void getListOfIngredients();
}
