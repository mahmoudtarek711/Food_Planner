package com.example.foodplaner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealResponse {

    @SerializedName("meals")
    private List<MealDTO> meals;

    public List<MealDTO> getMeals() {
        return meals;
    }
}

