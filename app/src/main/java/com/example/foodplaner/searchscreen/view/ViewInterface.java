package com.example.foodplaner.searchscreen.view;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;

import java.util.List;

public interface ViewInterface {
    void showMeals(List<MealDTO> meals);
    void showCategories(List<Category> categories);
    void showIngredients(List<Ingredient> ingredients);
    void showAreas(List<Area> areas);
}
