package com.example.foodplaner.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplaner.R;


public class LoginActivity extends AppCompatActivity
        implements LoginContract.View {

    private LoginContract.Presenter presenter;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI references
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Create Presenter
        presenter = new LoginPresenter(this);

        // User clicks login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // Delegate logic to presenter
            presenter.login(email, password);
        });
    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        // Could hide a progress bar here
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Toast.makeText(this, "Login Successful! Navigating...", Toast.LENGTH_SHORT).show();
        // Navigate to another activity
        Log.i("TAG", "navigateToHome: Success");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach(); // prevent memory leaks
    }
}
