package com.example.foodplaner.authuntication.presenter;

import com.example.foodplaner.authuntication.view.LoginFragment;
import com.example.foodplaner.authuntication.view.LoginViewInterface;
import com.example.foodplaner.network.Network;

public class LoginPresenter implements LoginPresenterInterface{
    LoginViewInterface view;
    public LoginPresenter(LoginViewInterface view) {
        this.view = view;
    }
    @Override
    public void login(String email, String password) {
        if (email.equals("email") && password.equals("password")){
            view.navigateToHome();
        }else
        {
            view.failLogin("Invalid email or password");
        }
    }

    @Override
    public void loginAsGuest() {
        view.navigateToHome();
    }

    @Override
    public void navigateToSignup() {
        view.navigateToSignup();
    }
}
