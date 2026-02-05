package com.example.foodplaner.network;

import com.example.foodplaner.model.AreaResponse;
import com.example.foodplaner.model.CategoryResponse;
import com.example.foodplaner.model.IngredientResponse;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiServices {

    // Search meal by name
    @GET("search.php")
    Observable<MealResponse> searchMealByName(@Query("s") String mealName);

    // List all meals by first letter
    @GET("search.php")
    Observable<MealResponse> listMealsByFirstLetter(@Query("f") String firstLetter);

    // Lookup full meal details by ID
    @GET("lookup.php")
    Observable<MealResponse> getMealById(@Query("i") String mealId);

    // Lookup a single random meal
    @GET("random.php")
    Observable<MealResponse> getRandomMeal();

     //List all meal categories
    @GET("categories.php")
    Observable<CategoryResponse> getAllCategories();

    // List all Categories, Area, Ingredients
    @GET("list.php?c=list")
    Observable<CategoryResponse> getCategoriesList();

    @GET("list.php?a=list")
    Observable<AreaResponse> getAreaList();

    @GET("list.php?i=list")
    Observable<IngredientResponse> getIngredientsList();

    // Filter by main ingredient
    @GET("filter.php")
    Observable<MealResponse> filterByIngredient(@Query("i") String ingredient);

    // Filter by category
    @GET("filter.php")
    Observable<MealResponse> filterByCategory(@Query("c") String category);

    // Filter by Area
    @GET("filter.php")
    Observable<MealResponse> filterByArea(@Query("a") String area);
}
