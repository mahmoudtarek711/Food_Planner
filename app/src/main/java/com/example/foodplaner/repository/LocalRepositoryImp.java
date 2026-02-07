package com.example.foodplaner.repository;

import android.content.Context;
import com.example.foodplaner.db.AppDatabase;
import com.example.foodplaner.db.MealDao;
import com.example.foodplaner.model.MealRoomDTO;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class LocalRepositoryImp implements LocalRepositoryInterface {

    private final MealDao dao;
    private static LocalRepositoryImp instance = null;

    private LocalRepositoryImp(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.dao = db.mealDao();
    }

    public static synchronized LocalRepositoryImp getInstance(Context context) {
        if (instance == null) {
            instance = new LocalRepositoryImp(context);
        }
        return instance;
    }

    @Override
    public Completable insertMeal(MealRoomDTO meal) {
        return dao.insertOrUpdate(meal);
    }

    @Override
    public Completable deleteMeal(MealRoomDTO meal) {
        return dao.delete(meal);
    }

    @Override
    public Single<MealRoomDTO> getSelectedMeal(String id, String email) {
        return dao.getMealById(id, email);
    }

    @Override
    public Observable<List<MealRoomDTO>> getStoredFavorites(String email) {
        return dao.getFavorites(email);
    }

    @Override
    public Observable<List<MealRoomDTO>> getStoredPlanByDate(String email, String date) {
        return dao.getPlanByDate(email, date);
    }
}
