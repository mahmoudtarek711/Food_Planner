package com.example.foodplaner.Domain.UseCases;


/**
 * UseCase = ONE business action
 * (Login user)
 */
public class LoginUseCase {

    /**
     * Executes login logic
     * @return true if login success
     */
    public boolean execute(String email, String password) {
        // Fake login logic (replace with API later)
        return email.equals("test@email.com") && password.equals("123456");
    }
}
