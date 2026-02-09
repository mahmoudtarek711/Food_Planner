package com.example.foodplaner.searchscreen.presenter;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.RepositoryImp;
import com.example.foodplaner.searchscreen.view.SearchFragment;
import com.example.foodplaner.searchscreen.view.ViewInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable; // Import this
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchScreenPresenter implements PresenterInterface {
    private String currentQuery = "";
    private String currentArea = null;
    private String currentCategory = null;
    private String currentIngredient = null;

    RepositoryImp repository;
    ViewInterface view;
    private List<MealDTO> originalList = new ArrayList<>();

    // 1. Add CompositeDisposable to manage RxJava calls
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SearchScreenPresenter(RepositoryImp repository, SearchFragment view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void filterByCountry(String areaName) {
        currentArea = areaName;
        currentCategory = null;
        currentIngredient = null;

        // 2. Wrap call in disposables.add()
        disposables.add(
                repository.filterByArea(areaName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    originalList = meals;
                                    applySearchFilter();
                                },
                                error -> {} // Handle error appropriately
                        )
        );
    }

    @Override
    public void filterByCategory(String categoryName) {
        currentArea = null;
        currentCategory = categoryName;
        currentIngredient = null;

        disposables.add(
                repository.filterByCategory(categoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    originalList = meals;
                                    applySearchFilter();
                                },
                                error -> {}
                        )
        );
    }

    @Override
    public void filterByIngredient(String ingredientName) {
        currentArea = null;
        currentCategory = null;
        currentIngredient = ingredientName;

        disposables.add(
                repository.filterByMainIngredient(ingredientName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    originalList = meals;
                                    applySearchFilter();
                                },
                                error -> {}
                        )
        );
    }

    @Override
    public void getListOfArea() {
        disposables.add(
                repository.getCountries()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                areas -> view.showAreas(areas),
                                error -> {}
                        )
        );
    }

    @Override
    public void getListOfCatgories() {
        disposables.add(
                repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                categories -> view.showCategories(categories),
                                error -> {}
                        )
        );
    }

    @Override
    public void getListOfIngredients() {
        disposables.add(
                repository.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ingredients -> view.showIngredients(ingredients),
                                error -> {}
                        )
        );
    }

    @Override
    public void getFullMeal(String mealName) {
        disposables.add(
                repository.getFullMeal(mealName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meal -> view.showFullMeal(meal),
                                error -> {}
                        )
        );
    }

    @Override
    public void searchMealsLocally(String query) {
        currentQuery = query;
        applySearchFilter();
    }

    @Override
    public void getLocalSavedMeals(String query) {
        currentQuery = query;
        applySearchFilter();
    }

    private void applySearchFilter() {
        if (currentQuery == null || currentQuery.isEmpty()) {
            view.showMeals(originalList);
            return;
        }

        List<MealDTO> filteredList = new ArrayList<>();
        // Simple contains check
        for (MealDTO meal : originalList) {
            if (meal.getStrMeal().toLowerCase().contains(currentQuery.toLowerCase())) {
                filteredList.add(meal);
            }
        }
        view.showMeals(filteredList);
    }

    public void restoreState() {
        if (currentArea != null) filterByCountry(currentArea);
        else if (currentCategory != null) filterByCategory(currentCategory);
        else if (currentIngredient != null) filterByIngredient(currentIngredient);
        else getListOfArea(); // Default load if nothing selected
    }

    // 3. Add cleanup method
    public void clearResources() {
        disposables.clear();
    }
}