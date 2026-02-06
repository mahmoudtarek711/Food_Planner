package com.example.foodplaner.searchscreen.presenter;

import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.RepositoryImp;
import com.example.foodplaner.searchscreen.view.SearchFragment;
import com.example.foodplaner.searchscreen.view.ViewInterface;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchScreenPresenter implements PresenterInterface{
    RepositoryImp repository;
    ViewInterface view;
    public SearchScreenPresenter(RepositoryImp repository, SearchFragment view)
    {
        this.repository = repository;
        this.view = view;
    }
    @Override
    public void filterByCountry(String areaName) {
        repository.filterByArea(areaName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showMeals(meals), // Success callback
                        error -> {/* Handle error, e.g., view.showError(error.getMessage()) */}
                );
    }

    @Override
    public void filterByCategory(String categoryName) {
        repository.filterByCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showMeals(meals), // Success callback
                        error -> {/* Handle error, e.g., view.showError(error.getMessage()) */}
                );
    }

    @Override
    public void filterByIngredient(String ingredientName) {
        repository.filterByMainIngredient(ingredientName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showMeals(meals), // Success callback
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
}
