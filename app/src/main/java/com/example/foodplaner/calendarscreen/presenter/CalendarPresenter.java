package com.example.foodplaner.calendarscreen.presenter;

import android.util.Log; // Import Log

import com.example.foodplaner.calendarscreen.view.CalendarViewInterface;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.repository.LocalRepositoryInterface;
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

    // 1. Pass the email in the constructor
    public CalendarPresenter(CalendarViewInterface view, LocalRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
    }

    @Override
    public void getMealsByDate(String date) {
        // 2. Add this Log to see exactly what is being requested
        Log.i("CalendarDebug", "Requesting Plan -> Email: " + userEmail + " | Date: " + date);

        disposable.add(
                repository.getStoredPlanByDate(userEmail, date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (List<MealRoomDTO> meals) -> {
                                    Log.i("CalendarDebug", "Meals Found: " + meals.size());
                                    view.showMeals(meals);
                                },
                                (Throwable error) -> {
                                    Log.e("CalendarDebug", "Error: " + error.getMessage());
                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void removeMeal(MealRoomDTO meal) {
        if (meal.isFavorite()) {
            // Just clear the date fields, DO NOT delete
            meal.setDate(null);
            // Optional: if you have a specific isPlanned boolean, set it to false here

            disposable.add(repository.insertMeal(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onRemoveMealSuccess(meal),
                            e -> view.showError(e.getMessage())
                    ));
        } else {
            disposable.add(repository.deleteMeal(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onRemoveMealSuccess(meal),
                            e -> view.showError(e.getMessage())
                    ));
        }
    }

    public void clearDisposables() {
        disposable.clear();
    }
}