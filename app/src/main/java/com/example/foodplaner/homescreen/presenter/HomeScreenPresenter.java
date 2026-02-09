package com.example.foodplaner.homescreen.presenter;

import com.example.foodplaner.homescreen.view.HomeFragment;
import com.example.foodplaner.homescreen.view.ViewInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.repository.AuthRepository;
import com.example.foodplaner.repository.LocalRepositoryInterface;
import com.example.foodplaner.repository.RepositoryImp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeScreenPresenter implements PresenterInterface {
    private static final long REFRESH_INTERVAL = 60000;
    ViewInterface view;
    RepositoryImp repository;
    AuthRepository AuthRepository;
    LocalRepositoryInterface localRepo;

    private static List<MealDTO> cachedMeals = null;
    private static MealDTO cachedRandomMeal = null;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public HomeScreenPresenter(HomeFragment view, RepositoryImp repository, LocalRepositoryInterface localRepo) {
        this.view = view;
        this.repository = repository;
        this.localRepo = localRepo;
        this.AuthRepository = new AuthRepository();
    }

    @Override
    public void requestRandomMeal(boolean forceRefresh) {
        // 1. If we have a cache and we DO NOT want to force refresh, show cache
        if (cachedRandomMeal != null && !forceRefresh){
            view.displayRandomMeal(cachedRandomMeal);
            view.onProcessingEnd();
            return;
        }

        // 2. FIX: If forcing refresh, clear the cache immediately
        if (forceRefresh) {
            cachedRandomMeal = null;
        }

        disposables.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meal -> {
                                    cachedRandomMeal = meal;
                                    view.displayRandomMeal(meal);
                                },
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void getAllMeals(boolean forceRefresh) {
        // 1. Check Cache
        if (cachedMeals != null && !forceRefresh){
            view.displayAllMeals(cachedMeals);
            view.onProcessingEnd();
            return;
        }

        // 2. Show Loading UI
        view.onProssessing();

        // 3. Fetch 10 Random Meals
        disposables.add(
                Observable.range(1, 10)
                        .concatMap(i -> repository.getRandomMeal()) // sequential calls
                        .toList() // converts Observable to Single<List<MealDTO>>
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mealDTOS -> {
                                    // Success Handler
                                    cachedMeals = mealDTOS;
                                    view.displayAllMeals(mealDTOS);
                                    view.onProcessingEnd(); // Ensure loading stops
                                },
                                throwable -> {
                                    // Error Handler
                                    view.onProcessingEnd(); // Ensure loading stops even on error
                                    view.showError(throwable.getMessage());
                                }
                        )
        );
    }

    public void getFavoriteMeals() {
        String email = AuthRepository.getCurrentUserEmail();

        disposables.add(
                localRepo.getStoredFavorites(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                roomList -> {
                                    List<MealDTO> uiList = new ArrayList<>();
                                    for (MealRoomDTO room : roomList) {
                                        uiList.add(MealDTO.fromRoomDTO(room));
                                    }
                                    view.displayFavoriteMeals(uiList);
                                },
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void navigateToMealDetails(String mealID){
        view.showMealDetails(mealID);
    }

    @Override
    public void logoutUser() {
        AuthRepository.logout();
        cachedMeals = null;
        cachedRandomMeal = null;
        view.showLogoutSuccess();
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        view.navigateToMealDetails(meal);
    }

    public void clearResources() {
        disposables.clear();
    }
    @Override
    public void requestRandomMeal(android.content.SharedPreferences prefs) {
        long lastFetchTime = prefs.getLong("last_fetch_time", 0);
        long currentTime = System.currentTimeMillis();
        String cachedMealJson = prefs.getString("meal_of_the_day", null);

        // 1. Check if the time has expired OR we don't have a meal saved yet
        if (currentTime - lastFetchTime > REFRESH_INTERVAL || cachedMealJson == null) {
            fetchNewRandomMealFromNetwork(prefs);
        } else {
            // 2. Load from SharedPreferences
            com.google.gson.Gson gson = new com.google.gson.Gson();
            MealDTO meal = gson.fromJson(cachedMealJson, MealDTO.class);
            view.displayRandomMeal(meal);
        }
    }

    private void fetchNewRandomMealFromNetwork(android.content.SharedPreferences prefs) {
        disposables.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meal -> {
                                    // 3. Save the new meal and the current time
                                    com.google.gson.Gson gson = new com.google.gson.Gson();
                                    prefs.edit()
                                            .putString("meal_of_the_day", gson.toJson(meal))
                                            .putLong("last_fetch_time", System.currentTimeMillis())
                                            .apply();

                                    view.displayRandomMeal(meal);
                                },
                                error -> view.showError("Network Error: " + error.getMessage())
                        )
        );
    }
}