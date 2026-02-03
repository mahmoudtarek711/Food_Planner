package com.example.foodplaner.authuntication.presenter;

public interface LoginPresenterInterface {
    void login(String email, String password);
    void loginAsGuest();

    void navigateToSignup();
}
