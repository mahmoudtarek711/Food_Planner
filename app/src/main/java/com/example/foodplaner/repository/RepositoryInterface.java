package com.example.foodplaner.repository;


import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface RepositoryInterface {
    Observable<List<MealDTO>> getSomeMeals();
    Observable<MealDTO> getRandomMeal();
    Observable<List<Category>> getCategories();

    Observable<List<Area>> getCountries();
    Observable<List<Ingredient>> getIngredients();

    Observable<List<MealDTO>> filterByMainIngredient(String ingredientName);

    Observable<List<MealDTO>> filterByCategory(String categoryName);

    Observable<List<MealDTO>> filterByArea(String areaName);

    Observable<MealDTO> getFullMeal(String mealName);


}
