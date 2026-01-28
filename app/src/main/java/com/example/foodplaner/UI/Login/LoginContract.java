package com.example.foodplaner.UI.Login;

/**
 * CONTRACT = rules of communication
 * View and Presenter ONLY talk through this
 */
public interface LoginContract {

    /**
     * VIEW responsibilities
     * (UI actions ONLY)
     */
    interface View {

        // Show loading indicator (ProgressBar)
        void showLoading();

        // Hide loading indicator
        void hideLoading();

        // Show error message to user
        void showError(String message);

        // Navigate to next screen
        void navigateToHome();
    }

    /**
     * PRESENTER responsibilities
     * (logic + decisions)
     */
    interface Presenter {

        // Called when user clicks login
        void login(String email, String password);

        // Clean reference when view is destroyed
        void detach();
    }
}
