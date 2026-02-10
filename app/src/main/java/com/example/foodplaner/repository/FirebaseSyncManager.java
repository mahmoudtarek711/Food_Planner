package com.example.foodplaner.repository;

import android.util.Log;

import com.example.foodplaner.datasource.localdatasource.LocalRepositoryInterface;
import com.example.foodplaner.model.MealRoomDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FirebaseSyncManager {

    private static FirebaseSyncManager instance;
    private final LocalRepositoryInterface localRepo;
    private ChildEventListener childListener;
    private DatabaseReference userRef;

    private FirebaseSyncManager(LocalRepositoryInterface localRepo) {
        this.localRepo = localRepo;
    }

    public static synchronized FirebaseSyncManager getInstance(LocalRepositoryInterface localRepo) {
        if (instance == null) {
            instance = new FirebaseSyncManager(localRepo);
        }
        return instance;
    }

    public void startSync(String email) {
        if (email == null) return;

        // Stop existing sync if any
        stopSync();

        String emailKey = email.replace(".", ",");
        userRef = FirebaseDatabase.getInstance().getReference("meals").child(emailKey);

        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                MealRoomDTO meal = snapshot.getValue(MealRoomDTO.class);
                if (meal != null) {
                    // Room OnConflictStrategy.REPLACE handles "add" and "update"
                    localRepo.insertMealLocalOnly(meal)
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> Log.i("Sync", "Added/Updated: " + meal.getStrMeal()),
                                    e -> Log.e("Sync", "Add error", e));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                // Same logic as added: Replace the local version with the new Firebase version
                onChildAdded(snapshot, previousChildName);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                MealRoomDTO meal = snapshot.getValue(MealRoomDTO.class);
                if (meal != null) {
                    // THIS IS THE FIX: When it's gone from Firebase, delete it from Room
                    localRepo.deleteMealLocalOnly(meal)
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> Log.i("Sync", "Removed locally: " + meal.getStrMeal()),
                                    e -> Log.e("Sync", "Remove error", e));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Sync", "Sync cancelled: " + error.getMessage());
            }
        };

        userRef.addChildEventListener(childListener);
    }

    public void stopSync() {
        if (userRef != null && childListener != null) {
            userRef.removeEventListener(childListener);
            childListener = null;
        }
    }
}