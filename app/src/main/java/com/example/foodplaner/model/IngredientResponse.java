package com.example.foodplaner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IngredientResponse implements Serializable {

    @SerializedName("meals")
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setMeals(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
