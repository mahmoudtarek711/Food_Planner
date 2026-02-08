package com.example.foodplaner.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodplaner.model.MealRoomDTO;

// 1. Define the entities and version
@Database(entities = {MealRoomDTO.class}, version = 2, exportSchema = false)
// 2. Register the TypeConverter for your Ingredient/Measure lists
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;

    // 3. Define the DAO
    public abstract MealDao mealDao();

    // 4. Singleton getInstance method
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "meals_database" // The name of the file on the phone
                    )
                    .fallbackToDestructiveMigration() // Recreates DB if you change the schema later
                    .build();
        }
        return instance;
    }
}
