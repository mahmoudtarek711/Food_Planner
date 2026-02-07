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
        // First check if it has a date. If no date and unfavorited, we delete.
        repo.getSelectedMeal(meal.getStrIdMeal(), userEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(roomDTO -> {
                    roomDTO.setFavorite(false);
                    if (roomDTO.getDate() == null) {
                        repo.deleteMeal(roomDTO).subscribe(() -> view.onFavoriteStatusChanged(false));
                    } else {
                        repo.insertMeal(roomDTO).subscribe(() -> view.onFavoriteStatusChanged(false));
                    }
                }, throwable -> view.onFavoriteStatusChanged(false));
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
