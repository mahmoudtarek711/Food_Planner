package com.example.foodplaner.repository;


import com.example.foodplaner.model.MealRoomDTO;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface LocalRepositoryInterface {
    // Save or Update (Handles both Favorite and Plan)
    Completable insertMeal(MealRoomDTO meal);

    // Delete completely
    Completable deleteMeal(MealRoomDTO meal);

    // Get specific meal to check status
    Single<MealRoomDTO> getSelectedMeal(String id, String email);

    // Get all Favorites
    Observable<List<MealRoomDTO>> getStoredFavorites(String email);

    // Get Plan for specific date
    Observable<List<MealRoomDTO>> getStoredPlanByDate(String email, String date);
}
