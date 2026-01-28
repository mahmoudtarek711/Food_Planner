package com.example.foodplaner.UI.Login;


import com.example.foodplaner.Domain.UseCases.LoginUseCase;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginUseCase loginUseCase;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.loginUseCase = new LoginUseCase();
    }

    @Override
    public void login(String email, String password) {

        // 1️⃣ Validate input
        if (email.isEmpty()) {
            view.showError("Email is required");
            return;
        }

        if (password.length() < 6) {
            view.showError("Password must be at least 6 characters");
            return;
        }

        // 2️⃣ Show loading
        view.showLoading();

        // 3️⃣ Execute use case
        boolean success = loginUseCase.execute(email, password);

        // 4️⃣ Hide loading
        view.hideLoading();

        // 5️⃣ Handle result
        if (success) {
            view.navigateToHome();
        } else {
            view.showError("Invalid credentials");
        }
    }

    @Override
    public void detach() {
        view = null; // avoid memory leaks
    }
}
