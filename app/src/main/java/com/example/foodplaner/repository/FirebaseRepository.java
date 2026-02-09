package com.example.foodplaner.repository;

import com.example.foodplaner.model.MealRoomDTO;
import com.google.firebase.database.*;

import android.util.Log;

public class FirebaseRepository {

    private final DatabaseReference mealsRef;

    public FirebaseRepository() {
        // Root reference in Firebase Realtime Database
        mealsRef = FirebaseDatabase.getInstance().getReference("meals");
    }

    /**
     * Replaces '.' with ',' because Firebase does not allow dots in keys.
     */
    private String sanitizeEmail(String email) {
        if (email == null) return "unknown_user";
        return email.replace(".", ",");
    }

    /**
     * Generates a unique key based on the meal ID and the date.
     * This matches our Room Primary Key logic.
     * Example: "52772_favorite" or "52772_2026-02-10"
     */
    private String getMealKey(MealRoomDTO meal) {
        // This creates keys like "52772_favorite" or "52772_2026-02-10"
        return meal.getIdMeal() + "_" + meal.getDate();
    }

    // ---------------- INSERT / UPDATE ----------------

    public void insertMeal(MealRoomDTO meal) {
        String emailKey = sanitizeEmail(meal.getUserEmail());
        String mealKey = getMealKey(meal);

        // Using the mealKey as the child ID prevents duplicates in Firebase
        mealsRef.child(emailKey).child(mealKey).setValue(meal)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Log.i("Firebase", "Meal synced: " + mealKey);
                });
    }

    // ---------------- DELETE ----------------

    /**
     * Deletes the meal from Firebase using the unique MealKey (ID_Date).
     */
    public void deleteMeal(MealRoomDTO meal) {
        if (meal.getUserEmail() == null || meal.getIdMeal() == null) {
            Log.e("FirebaseRepo", "Cannot delete: Missing email or meal ID");
            return;
        }

        String emailKey = sanitizeEmail(meal.getUserEmail());
        String mealKey = getMealKey(meal);

        mealsRef.child(emailKey).child(mealKey).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("FirebaseRepo", "Meal removed from Firebase: " + mealKey);
                    } else {
                        Log.e("FirebaseRepo", "Failed to remove from Firebase", task.getException());
                    }
                });
    }

    // ---------------- REAL-TIME LISTENERS ----------------

    public void listenForMeals(String email, ValueEventListener listener) {
        if (email == null) return;
        String emailKey = sanitizeEmail(email);
        mealsRef.child(emailKey).addValueEventListener(listener);
    }

    public void removeListener(String email, ValueEventListener listener) {
        if (email == null || listener == null) return;
        String emailKey = sanitizeEmail(email);
        mealsRef.child(emailKey).removeEventListener(listener);
    }
}