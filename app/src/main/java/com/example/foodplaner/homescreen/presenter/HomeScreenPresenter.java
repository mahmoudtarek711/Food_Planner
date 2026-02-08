package com.example.foodplaner.homescreen.presenter;

import com.example.foodplaner.homescreen.view.HomeFragment;
import com.example.foodplaner.homescreen.view.ViewInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealResponse;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.network.Network;
import com.example.foodplaner.repository.AuthRepository;
import com.example.foodplaner.repository.LocalRepositoryInterface;
import com.example.foodplaner.repository.RepositoryImp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenPresenter implements PresenterInterface {
    ViewInterface view;
    RepositoryImp repository;
    AuthRepository AuthRepository;
    LocalRepositoryInterface localRepo;

    private static List<MealDTO> cachedMeals = null;
    private static MealDTO cachedRandomMeal = null;

    public HomeScreenPresenter(HomeFragment view,RepositoryImp repository,LocalRepositoryInterface localRepo) {
        this.view = view;
        this.repository = repository;
        this.localRepo = localRepo;
        this.AuthRepository = new AuthRepository();
    }

    @Override
    public void requestRandomMeal(boolean forceRefresh) {
        if (cachedRandomMeal != null && !forceRefresh){
            view.displayRandomMeal(cachedRandomMeal);
            view.onProcessingEnd();
            return;
        }
        repository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> {cachedRandomMeal=meal;view.displayRandomMeal(meal);},

                        error -> view.showError(error.getMessage())
                );
    }

    @Override
    public void getAllMeals(boolean forceRefresh) {
        if (cachedMeals != null && !forceRefresh){
            view.displayAllMeals(cachedMeals);
            view.onProcessingEnd();
            return;
        }
        Observable.range(1, 10)
                .concatMap(i -> repository.getRandomMeal()) // call random meal 10 times
                .toList() // collect all results into a List<MealDTO>
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MealDTO>>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {
                                   view.onProssessing();
                               }

                               @Override
                               public void onSuccess(@NonNull List<MealDTO> mealDTOS) {
                                   view.onProcessingEnd();
                                   cachedMeals = mealDTOS;
                                   view.displayAllMeals(mealDTOS);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {

                               }
                           }
                );

    }
    public void getFavoriteMeals() {
        String email = AuthRepository.getCurrentUserEmail(); // Or FirebaseAuth.getInstance().getCurrentUser().getEmail()

        localRepo.getStoredFavorites(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        roomList -> {
                            // Convert MealRoomDTO list back to MealDTO list for the adapter
                            List<MealDTO> uiList = new ArrayList<>();
                            for (MealRoomDTO room : roomList) {
                                uiList.add(MealDTO.fromRoomDTO(room));
                            }
                            view.displayFavoriteMeals(uiList);
                        },
                        error -> view.showError(error.getMessage())
                );
    }
    @Override
    public void navigateToMealDetails (String mealID){
        view.showMealDetails(mealID);
    }

    @Override
    public void logoutUser () {
        AuthRepository.logout();
        cachedMeals = null;
        cachedRandomMeal = null;
        view.showLogoutSuccess();
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        view.navigateToMealDetails(meal);
    }

}
