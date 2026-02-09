package com.example.foodplaner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplaner.model.MealRoomDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdate(MealRoomDTO meal);
    @Insert
    Completable insertMeal(MealRoomDTO meal);

    @Delete
    Completable delete(MealRoomDTO meal);


    @Query("SELECT * FROM meals_table WHERE userEmail = :email AND date = :date")
    Observable<List<MealRoomDTO>> getPlanByDate(String email, String date);

    @Query("SELECT * FROM meals_table WHERE idMeal = :id AND userEmail = :email LIMIT 1")
    Single<MealRoomDTO> getMealById(String id, String email);

    @Query("SELECT EXISTS(SELECT 1 FROM meals_table WHERE idMeal = :mealId AND userEmail = :userEmail AND date = :date)")
    Single<Boolean> exists(String mealId, String userEmail, String date);

    // Change: Look for date = 'favorite' instead of date IS NULL
    @Query("SELECT * FROM meals_table WHERE idMeal = :id AND userEmail = :email AND date = 'favorite' LIMIT 1")
    Single<MealRoomDTO> getFavoriteMeal(String id, String email);

    // Change: Ensure planned meals are ones where date is NOT 'favorite'
    @Query("SELECT EXISTS(SELECT 1 FROM meals_table WHERE idMeal = :mealId AND userEmail = :userEmail AND date != 'favorite')")
    Single<Boolean> isMealPlanned(String mealId, String userEmail);

    @Query("SELECT * FROM meals_table WHERE userEmail = :email AND date = 'favorite'")
    Observable<List<MealRoomDTO>> getFavorites(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM meals_table WHERE idMeal = :mealId AND userEmail = :userEmail AND date = 'favorite')")
    Single<Boolean> isMealFavorite(String mealId, String userEmail);


}
