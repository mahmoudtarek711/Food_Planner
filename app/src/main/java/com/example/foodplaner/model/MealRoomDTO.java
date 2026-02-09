package com.example.foodplaner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.foodplaner.db.DataConverter;
import java.util.List;

/**
 * Composite Primary Key: {idMeal, userEmail, date}
 * This ensures that a user cannot have duplicate rows for the same meal on the same day.
 * For favorites that aren't on the calendar, 'date' defaults to "favorite".
 */
@Entity(tableName = "meals_table", primaryKeys = {"idMeal", "userEmail", "date"})
@TypeConverters(DataConverter.class)
public class MealRoomDTO {

    @NonNull
    private String idMeal;

    @NonNull
    private String userEmail;

    @NonNull
    private String date = "favorite"; // Default value to prevent Null Primary Key

    private String strMeal;
    private String strMealThumb;
    private String strArea;
    private String strCategory;
    private String strInstructions;
    private String strYoutube;

    private List<String> ingredients;
    private List<String> measures;

    private boolean isFavorite;
    private String firebaseKey;

    // Required Empty Constructor for Firebase and Room
    public MealRoomDTO() {}

    // --- Getters and Setters ---

    @NonNull
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(@NonNull String idMeal) { this.idMeal = idMeal; }

    @NonNull
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(@NonNull String userEmail) { this.userEmail = userEmail; }

    @NonNull
    public String getDate() {
        return (date == null || date.isEmpty()) ? "favorite" : date;
    }
    public void setDate(@NonNull String date) {
        if (date == null || date.isEmpty()) {
            this.date = "favorite";
        } else {
            this.date = date;
        }
    }

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

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public List<String> getMeasures() { return measures; }
    public void setMeasures(List<String> measures) { this.measures = measures; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getFirebaseKey() { return firebaseKey; }
    public void setFirebaseKey(String firebaseKey) { this.firebaseKey = firebaseKey; }
}