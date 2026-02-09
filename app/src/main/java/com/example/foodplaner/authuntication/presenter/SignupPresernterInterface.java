package com.example.foodplaner.authuntication.presenter;

public interface SignupPresernterInterface {
    void RegisterAccountNormally(String email, String password, String username);

    void RegisterAccountNormally(String username , String password);
    void RegisterAccountGoogle(String idToken);
    void RegisterAccountFacebook(String username , String password);
}
