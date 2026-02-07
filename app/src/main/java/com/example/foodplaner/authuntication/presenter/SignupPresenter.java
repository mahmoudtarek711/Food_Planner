package com.example.foodplaner.authuntication.presenter;

import com.example.foodplaner.authuntication.view.SignupViewInterface;
import com.example.foodplaner.repository.AuthRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignupPresenter implements SignupPresernterInterface{
    private SignupViewInterface view;
    private final AuthRepository repo;
    private final CompositeDisposable disposable = new CompositeDisposable();
    public SignupPresenter(SignupViewInterface view) {
        this.view = view;
        this.repo = new AuthRepository();
    }

    @Override
    public void RegisterAccountNormally(String email, String password, String username) {
        view.ShowLoading();

        disposable.add(
                repo.register(email, password, username)
                        .subscribeOn(Schedulers.io()) // Run on background thread
                        .observeOn(AndroidSchedulers.mainThread()) // Deliver results to UI thread
                        .subscribe(
                                () -> {
                                    view.HideLoading();
                                    view.NavigateToHome();
                                },
                                throwable -> {
                                    view.HideLoading();
                                    view.ShowError(throwable.getMessage());
                                }
                        )
        );
    }
    public void clearDisposables() {
        disposable.clear();
    }

    @Override
    public void RegisterAccountNormally(String username, String password) {

    }

    @Override
    public void RegisterAccountGoogle(String username, String password) {

    }

    @Override
    public void RegisterAccountFacebook(String username, String password) {

    }
}
