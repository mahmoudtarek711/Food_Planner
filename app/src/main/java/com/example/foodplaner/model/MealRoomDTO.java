package com.example.foodplaner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.foodplaner.db.DataConverter; // We will create this below
import java.util.List;

@Entity(tableName = "meals_table")
@TypeConverters(DataConverter.class)
public class MealRoomDTO {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    private String idMeal;
    @NonNull
    private String userEmail;

    private String strMeal;
    private String strMealThumb;
    private String strArea;
    private String strCategory;
    private String strInstructions;
    private String strYoutube;

    // The lists from MealDTO
    private List<String> ingredients;
    private List<String> measures;

    private String date;
    private boolean isFavorite;

    public MealRoomDTO() {}

    // --- New Getters and Setters for Lists ---
    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public List<String> getMeasures() { return measures; }
    public void setMeasures(List<String> measures) { this.measures = measures; }

    // --- Existing Getters and Setters ---
    @NonNull
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(@NonNull String idMeal) { this.idMeal = idMeal; }

    @NonNull
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(@NonNull String userEmail) { this.userEmail = userEmail; }

    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }

    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String strMealThumb) { this.strMealThumb = strMealThumb; }

    public String getStrArea() { return strArea; }
    public void setStrArea(String strArea) { this.strArea = strArea; }

    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }

    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }

    public String getStrYoutube() { return strYoutube; }
    public void setStrYoutube(String strYoutube) { this.strYoutube = strYoutube; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
