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
        MealRoomDTO roomDTO = meal.toRoomDTO(userEmail); // Ensure you added this mapper in MealDTO
        roomDTO.setFavorite(true);

        repo.insertMeal(roomDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onFavoriteStatusChanged(true),
                        e -> view.showMessage("Error: " + e.getMessage()));
    }

    @Override
    public void removeFromFavorite(MealDTO meal) {

        repo.getSelectedMeal(meal.getStrIdMeal(), userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(roomDTO -> {

                    roomDTO.setFavorite(false);

                    if (roomDTO.getDate() == null) {

                        // FIX: add subscribeOn and observeOn
                        repo.deleteMeal(roomDTO)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onFavoriteStatusChanged(false),
                                        throwable -> view.showMessage("Error: " + throwable.getMessage())
                                );

                    } else {

                        // FIX: add subscribeOn and observeOn
                        repo.insertMeal(roomDTO)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> view.onFavoriteStatusChanged(false),
                                        throwable -> view.showMessage("Error: " + throwable.getMessage())
                                );
                    }

                }, throwable -> view.showMessage("Error: " + throwable.getMessage()));
    }

    @Override
    public void checkIfFavorite(String mealId) {
        repo.getSelectedMeal(mealId, userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> view.onFavoriteStatusChanged(item.isFavorite()),
                        error -> view.onFavoriteStatusChanged(false)
                );
    }
}
