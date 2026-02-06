package com.example.foodplaner.searchscreen.presenter;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.RepositoryImp;
import com.example.foodplaner.searchscreen.view.SearchFragment;
import com.example.foodplaner.searchscreen.view.ViewInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchScreenPresenter implements PresenterInterface{
    private String currentQuery = "";
    private String currentArea = null;
    private String currentCategory = null;
    private String currentIngredient = null;

    RepositoryImp repository;
    ViewInterface view;
    private List<MealDTO> originalList = new ArrayList<>();
    public SearchScreenPresenter(RepositoryImp repository, SearchFragment view)
    {
        this.repository = repository;
        this.view = view;
    }
    @Override
    public void filterByCountry(String areaName) {
        currentArea = areaName;
        currentCategory = null;
        currentIngredient = null;
        repository.filterByArea(areaName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {view.showMeals(meals);originalList = meals;applySearchFilter();}, // Success callback
                        error -> {/* Handle error, e.g., view.showError(error.getMessage()) */}
                );
    }

    @Override
    public void filterByCategory(String categoryName) {
        currentArea = null;
        currentCategory = categoryName;
        currentIngredient = null;
        repository.filterByCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {view.showMeals(meals);originalList = meals;applySearchFilter();}, // Success callback
                        error -> {/* Handle error, e.g., view.showError(error.getMessage()) */}
                );
    }

    @Override
    public void filterByIngredient(String ingredientName) {
        currentArea = null;
        currentCategory = null;
        currentIngredient = ingredientName;
        repository.filterByMainIngredient(ingredientName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {view.showMeals(meals);originalList = meals;applySearchFilter();}, // Success callback
                        error -> {/* Handle error, e.g., view.showError(error.getMessage()) */}
                );
    }

    @Override
    public void getListOfArea() {
        repository.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        areas -> view.showAreas(areas),
                        error -> {}
                );
    }

    @Override
    public void getListOfCatgories() {
        repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.showCategories(categories),
                        error -> {}
                );
    }

    @Override
    public void getListOfIngredients() {
        repository.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients -> view.showIngredients(ingredients),
                        error -> {}
                );
    }

    @Override
    public void getFullMeal(String mealName) {
        repository.getFullMeal(mealName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        meal -> view.showFullMeal(meal),

                        error -> {
                            // handle error
                        }
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

        if(currentQuery == null || currentQuery.isEmpty()){
            view.showMeals(originalList);
            return;
        }

        List<MealDTO> filteredList = new ArrayList<>();

        for(MealDTO meal : originalList){

            if(meal.getStrMeal()
                    .toLowerCase()
                    .contains(currentQuery.toLowerCase())){

                filteredList.add(meal);
            }
        }
        for(MealDTO meal : originalList){

            if(meal.getStrMeal().toLowerCase()
                    .contains(currentQuery.toLowerCase()) && !filteredList.contains(meal)){

                filteredList.add(meal);
            }
        }

        view.showMeals(filteredList);
    }
    public void restoreState() {

        if(currentArea != null)
            filterByCountry(currentArea);

        else if(currentCategory != null)
            filterByCategory(currentCategory);

        else if(currentIngredient != null)
            filterByIngredient(currentIngredient);
        applySearchFilter();
    }



}
