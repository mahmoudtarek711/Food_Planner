package com.example.foodplaner.authuntication.view;

import com.example.foodplaner.model.MealDTO;

public interface SignupViewInterface {
    void ShowError(String ErrorMessage);
    void CorrectCredentials(MealDTO user); //should be user
    void ShowLoading();
    void HideLoading();
    void NavigateToHome();
    void RegisterAccountGoogle(String idToken);
}
