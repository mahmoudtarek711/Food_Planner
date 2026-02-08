package com.example.foodplaner.detailscreen.presenter;

import com.example.foodplaner.detailscreen.view.MealDetailsViewInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.repository.LocalRepositoryInterface;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsPresenterInterface {
    private MealDetailsViewInterface view;
    private LocalRepositoryInterface repo;
    private String userEmail;

    public MealDetailsPresenter(MealDetailsViewInterface view, LocalRepositoryInterface repo) {
        this.view = view;
        this.repo = repo;
        this.userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    public void addToFavorite(MealDTO meal) {

        repo.getSelectedMeal(meal.getStrIdMeal(), userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(existingMeal -> {
                    // Meal exists → update favorite
                    existingMeal.setFavorite(true);
                    repo.insertMeal(existingMeal)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> view.onFavoriteStatusChanged(true),
                                    e -> view.showMessage(e.getMessage())
                            );

                }, throwable -> {
                    // Meal does NOT exist → create new row
                    MealRoomDTO roomDTO = meal.toRoomDTO(userEmail);
                    roomDTO.setFavorite(true);
                    roomDTO.setDate(null); // no calendar date
                    repo.insertMeal(roomDTO)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> view.onFavoriteStatusChanged(true),
                                    e -> view.showMessage(e.getMessage())
                            );
                });
    }


    @Override
    public void removeFromFavorite(MealDTO meal) {

        repo.getSelectedMeal(meal.getStrIdMeal(), userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(existingMeal -> {
                    existingMeal.setFavorite(false);

                    if (existingMeal.getDate() == null) {
                        // No date → safe to delete
                        repo.deleteMeal(existingMeal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onFavoriteStatusChanged(false),
                                        e -> view.showMessage(e.getMessage())
                                );
                    } else {
                        // Planned → just update favorite
                        repo.insertMeal(existingMeal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onFavoriteStatusChanged(false),
                                        e -> view.showMessage(e.getMessage())
                                );
                    }

                }, e -> view.showMessage(e.getMessage()));
    }


    @Override
    public void checkIfFavorite(String mealId) {

        repo.isMealFavorite(mealId, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFav -> view.onFavoriteStatusChanged(isFav),
                        e -> view.onFavoriteStatusChanged(false)
                );
    }


    @Override
    public void addToCalendar(MealDTO meal, String date) {
        // Always create a new MealRoomDTO for calendar, even if it exists
        MealRoomDTO newMeal = meal.toRoomDTO(userEmail);

        newMeal.setDate(date);          // set the calendar date
        newMeal.setFavorite(false);     // or keep favorite false for calendar duplicates

        repo.insertMeal(newMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onCalendarStatusChanged(true),
                        e -> view.showMessage(e.getMessage())
                );
    }



    @Override
    public void removeFromCalendar(MealDTO meal) {

        repo.getSelectedMeal(meal.getStrIdMeal(), userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(roomDTO -> {

                    roomDTO.setDate(null);

                    if (!roomDTO.isFavorite()) {

                        repo.deleteMeal(roomDTO)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onCalendarStatusChanged(false),
                                        e -> view.showMessage(e.getMessage())
                                );

                    } else {

                        repo.insertMeal(roomDTO)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onCalendarStatusChanged(false),
                                        e -> view.showMessage(e.getMessage())
                                );
                    }

                }, e -> view.showMessage(e.getMessage()));
    }

    @Override
    public void checkIfPlanned(String mealId) {

        repo.isMealPlanned(mealId, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isPlanned -> view.onCalendarStatusChanged(isPlanned), // Boolean directly
                        e -> view.onCalendarStatusChanged(false)
                );
    }


}
