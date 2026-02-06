package com.example.foodplaner.repository;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.network.MealApiServices;
import com.example.foodplaner.network.Network;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class RepositoryImp implements RepositoryInterface{
    MealApiServices api;

    public RepositoryImp() {
        api = Network.getInstance();
    }
    @Override
    public Observable<List<MealDTO>> getSomeMeals() {
        return null;
    }

    @Override
    public Observable<MealDTO> getRandomMeal() {
        return api.getRandomMeal().map(mealResponse -> mealResponse.getMeals().get(0));
    }

    @Override
    public Observable<List<Category>> getCategories() {
        return api.getAllCategories().map(categoryResponse -> categoryResponse.getCategories());
    }

    @Override
    public Observable<List<Area>> getCountries() {
        return api.getAreaList().map(areaResponse -> areaResponse.getAreas());
    }

    @Override
    public Observable<List<Ingredient>> getIngredients() {
        return api.getIngredientsList().map(ingredientResponse -> ingredientResponse.getIngredients());
    }

    @Override
    public Observable<List<MealDTO>> filterByMainIngredient(String ingredientName) {
        return api.filterByIngredient(ingredientName).map(mealResponse -> mealResponse.getMeals());
    }

    @Override
    public Observable<List<MealDTO>> filterByCategory(String categoryName) {
        return api.filterByCategory(categoryName).map(mealResponse -> mealResponse.getMeals());
    }

    @Override
    public Observable<List<MealDTO>> filterByArea(String areaName) {
        return api.filterByArea(areaName).map(mealResponse -> mealResponse.getMeals());
    }

    @Override
    public Observable<MealDTO> getFullMeal(String mealName) {
        return api.searchMealByName(mealName).map(mealResponse -> mealResponse.getMeals().get(0));
    }
}
