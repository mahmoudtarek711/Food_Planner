package com.example.foodplaner.network;

import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiServices {

    // Search meal by name
    @GET("search.php")
    Call<MealDTO> searchMealByName(@Query("s") String mealName);

    // List all meals by first letter
    @GET("search.php")
    Call<MealDTO> listMealsByFirstLetter(@Query("f") String firstLetter);

    // Lookup full meal details by ID
    @GET("lookup.php")
    Call<MealDTO> getMealById(@Query("i") String mealId);

    // Lookup a single random meal
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    // List all meal categories
//    @GET("categories.php")
//    Call<CategoryResponse> getAllCategories();
//
//    // List all Categories, Area, Ingredients
//    @GET("list.php?c=list")
//    Call<CategoryResponse> getCategoriesList();
//
//    @GET("list.php?a=list")
//    Call<CategoryResponse> getAreaList();
//
//    @GET("list.php?i=list")
//    Call<CategoryResponse> getIngredientsList();

    // Filter by main ingredient
    @GET("filter.php")
    Call<MealDTO> filterByIngredient(@Query("i") String ingredient);

    // Filter by category
    @GET("filter.php")
    Call<MealDTO> filterByCategory(@Query("c") String category);

    // Filter by Area
    @GET("filter.php")
    Call<MealDTO> filterByArea(@Query("a") String area);
}
