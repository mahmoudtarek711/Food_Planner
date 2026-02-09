package com.example.foodplaner.repository;

import android.content.Context;
import com.example.foodplaner.db.AppDatabase;
import com.example.foodplaner.db.MealDao;
import com.example.foodplaner.model.MealRoomDTO;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class LocalRepositoryImp implements LocalRepositoryInterface {

    private final MealDao dao;
    private static LocalRepositoryImp instance = null;
    FirebaseRepository firebaseRepo;

    private LocalRepositoryImp(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.mealDao();
        firebaseRepo = new FirebaseRepository();
    }

    public static synchronized LocalRepositoryImp getInstance(Context context) {
        if (instance == null) {
            instance = new LocalRepositoryImp(context);
        }
        return instance;
    }

    @Override
    public Completable insertMeal(MealRoomDTO meal) {

        return dao.insertOrUpdate(meal)
                .doOnComplete(() -> firebaseRepo.insertMeal(meal));
    }
    @Override
    public Completable insertMealLocalOnly(MealRoomDTO meal) {

        return dao.insertOrUpdate(meal);
    }


    @Override
    public Completable deleteMeal(MealRoomDTO meal) {
        return dao.delete(meal)
                .andThen(Completable.fromAction(() -> {
                    firebaseRepo.deleteMeal(meal);
                }));
    }

    @Override
    public Single<MealRoomDTO> getSelectedMeal(String id, String email) {
        return dao.getFavoriteMeal(id, email);
    }


    @Override
    public Observable<List<MealRoomDTO>> getStoredFavorites(String email) {
        return dao.getFavorites(email);
    }

    @Override
    public Observable<List<MealRoomDTO>> getStoredPlanByDate(String email, String date) {
        return dao.getPlanByDate(email, date);
    }
    @Override
    public Single<Boolean> isMealFavorite(String mealId, String userEmail) {
        return dao.isMealFavorite(mealId, userEmail);
    }

    @Override
    public Single<Boolean> isMealPlanned(String mealId, String userEmail) {
        return dao.isMealPlanned(mealId, userEmail);
    }
    @Override
    public Single<Boolean> exists(String mealId, String userEmail, String date) {
        return dao.exists(mealId, userEmail, date); // you'll implement this in MealDao
    }
    @Override
    public Completable deleteMealLocalOnly(MealRoomDTO meal) {
        return dao.delete(meal); // Only calls Room Dao, not FirebaseRepo
    }



}
