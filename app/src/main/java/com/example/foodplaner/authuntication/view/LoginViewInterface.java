package com.example.foodplaner.authuntication.view;

public interface LoginViewInterface {
    void navigateToSignup();
    void navigateToHome();
    void failLogin(String message);
    void showLoading();
    void hideLoading();
}
