package com.example.foodplaner.detailscreen.presenter;

import com.example.foodplaner.detailscreen.view.MealDetailsViewInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.repository.LocalRepositoryInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsPresenterInterface {
    private MealDetailsViewInterface view;
    private LocalRepositoryInterface repo;
    private String userEmail;

    public MealDetailsPresenter(MealDetailsViewInterface view, LocalRepositoryInterface repo) {
        this.view = view;
        this.repo = repo;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Handle Guest Case
        if (currentUser != null && !currentUser.isAnonymous()) {
            this.userEmail = currentUser.getEmail();
        } else {
            this.userEmail = null; // Guest user has no email
        }
    }

    @Override
    public void addToFavorite(MealDTO meal) {
        if (userEmail == null) {
            view.showMessage("Guest cannot add to favorites. Please Login.");
            return;
        }
        MealRoomDTO roomDTO = meal.toRoomDTO(userEmail);
        roomDTO.setFavorite(true);
        roomDTO.setDate("favorite"); // Explicitly set "favorite" as the date

        repo.insertMeal(roomDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onFavoriteStatusChanged(true),
                        e -> view.showMessage(e.getMessage())
                );
    }

    @Override
    public void removeFromFavorite(MealDTO meal) {
        // To delete from Room/Firebase with composite key, we need the exact PK
        MealRoomDTO roomDTO = new MealRoomDTO();
        roomDTO.setIdMeal(meal.getStrIdMeal());
        roomDTO.setUserEmail(userEmail);
        roomDTO.setDate("favorite"); // This identifies the "Favorite" version of the meal

        repo.deleteMeal(roomDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onFavoriteStatusChanged(false),
                        e -> view.showMessage("Error removing favorite: " + e.getMessage())
                );
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
        if (userEmail == null) {
            view.showMessage("Guest cannot add to favorites. Please Login.");
            return;
        }
        MealRoomDTO roomDTO = meal.toRoomDTO(userEmail);
        roomDTO.setDate(date); // The specific date (e.g., "2026-02-10")
        roomDTO.setFavorite(false);

        repo.insertMeal(roomDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onCalendarStatusChanged(true),
                        e -> view.showMessage(e.getMessage())
                );
    }

    @Override
    public void removeFromCalendar(MealDTO meal) {
        // Note: Details screen usually doesn't know 'which' date to remove
        // if the meal is scheduled multiple times.
        // This usually happens inside CalendarFragment/Presenter.
        // If needed here, you would need to pass the date string to this method.
        view.showMessage("Please remove meals via the Calendar screen");
    }

    @Override
    public void checkIfPlanned(String mealId) {
        repo.isMealPlanned(mealId, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isPlanned -> view.onCalendarStatusChanged(isPlanned),
                        e -> view.onCalendarStatusChanged(false)
                );
    }
}