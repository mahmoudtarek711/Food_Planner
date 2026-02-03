package com.example.foodplaner.homescreen.presenter;

import com.example.foodplaner.homescreen.view.HomeFragment;
import com.example.foodplaner.homescreen.view.ViewInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealResponse;
import com.example.foodplaner.network.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenPresenter implements PresenterInterface {
    ViewInterface view;

    public HomeScreenPresenter(HomeFragment view) {
        this.view = view;
    }

    @Override
    public void requestRandomMeal() {
        Network.getInstance()
                .getRandomMeal()
                .enqueue(new Callback<MealResponse>() {

                    @Override
                    public void onResponse(Call<MealResponse> call,
                                           Response<MealResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getMeals() != null
                                && !response.body().getMeals().isEmpty()) {

                            MealDTO meal = response.body()
                                    .getMeals()
                                    .get(0);

                            view.displayRandomMeal(meal);
                        }
                    }

                    @Override
                    public void onFailure(Call<MealResponse> call, Throwable t) {
                        // Handle failure
                    }
                });
    }

    @Override
    public void getAllMeals() {
        List<MealDTO> randomMeals = new ArrayList<>();
        int TOTAL_MEALS = 6;

        for (int i = 0; i < TOTAL_MEALS; i++) {

            Network.getInstance()
                    .getRandomMeal()
                    .enqueue(new Callback<MealResponse>() {

                        @Override
                        public void onResponse(Call<MealResponse> call,
                                               Response<MealResponse> response) {

                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body() != null) {

                                MealDTO meal =
                                        response.body().getMeals().get(0);

                                randomMeals.add(meal);

                                // Once we collected 6 meals → update UI
                                if (randomMeals.size() == TOTAL_MEALS) {
                                    view.displayAllMeals(randomMeals);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            // Handle failure
                        }
                    });
        }
    }
    @Override
    public void navigateToMealDetails (String mealID){
        view.showMealDetails(mealID);
    }

    @Override
    public void logoutUser () {
        // Perform logout logic here (e.g., clear user session, tokens, etc.)
        // For demonstration, we'll just call the view method directly.
        view.showLogoutSuccess();
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        view.navigateToMealDetails(meal);
    }

}
