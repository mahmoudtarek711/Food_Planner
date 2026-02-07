package com.example.foodplaner.authuntication.presenter;

import com.example.foodplaner.authuntication.view.LoginFragment;
import com.example.foodplaner.authuntication.view.LoginViewInterface;
import com.example.foodplaner.network.Network;
import com.example.foodplaner.repository.AuthRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements LoginPresenterInterface{
    LoginViewInterface view;
    private final AuthRepository repo;
    private final CompositeDisposable disposable = new CompositeDisposable();
    public LoginPresenter(LoginViewInterface view) {
        this.view = view;
        this.repo = new AuthRepository();
    }
    @Override
    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.failLogin("Email and Password cannot be empty");
            return;
        }

        view.showLoading(); // You'll need to add this to your interface
        disposable.add(
                repo.login(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    view.hideLoading();
                                    view.navigateToHome();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.failLogin(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loginAsGuest() {
        view.showLoading();
        disposable.add(
                repo.loginGuest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    view.hideLoading();
                                    view.navigateToHome();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.failLogin(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void navigateToSignup() {
        view.navigateToSignup();
    }
    public void dispose() {
        disposable.clear();
    }
}
