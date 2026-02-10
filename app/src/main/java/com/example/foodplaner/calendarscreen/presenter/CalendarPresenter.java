package com.example.foodplaner.calendarscreen.presenter;

import android.util.Log;

import com.example.foodplaner.calendarscreen.view.CalendarViewInterface;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.datasource.localdatasource.LocalRepositoryInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPresenter implements CalendarPresenterInterface {

    private CalendarViewInterface view;
    private LocalRepositoryInterface repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String userEmail;

    public CalendarPresenter(CalendarViewInterface view, LocalRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
    }

    @Override
    public void getMealsByDate(String date) {
        Log.i("CalendarDebug", "Requesting Plan -> Email: " + userEmail + " | Date: " + date);

        disposable.add(
                repository.getStoredPlanByDate(userEmail, date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (List<MealRoomDTO> meals) -> {
                                    Log.i("CalendarDebug", "Meals Found for " + date + ": " + meals.size());
                                    view.showMeals(meals);
                                },
                                (Throwable error) -> {
                                    Log.e("CalendarDebug", "Error fetching meals: " + error.getMessage());
                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void removeMeal(MealRoomDTO meal) {
        // With the Composite Primary Key {idMeal, userEmail, date},
        // this 'meal' object contains the specific date it was planned for.
        // Deleting it will remove only this specific day's entry from Room and Firebase.

        disposable.add(repository.deleteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i("CalendarDebug", "Meal deleted from Calendar: " + meal.getStrMeal());
                            view.onRemoveMealSuccess(meal);
                        },
                        e -> {
                            Log.e("CalendarDebug", "Delete failed: " + e.getMessage());
                            view.showError(e.getMessage());
                        }
                ));
    }

    public void clearDisposables() {
        disposable.clear();
    }
}