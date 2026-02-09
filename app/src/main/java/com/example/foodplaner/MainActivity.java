package com.example.foodplaner;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private NavController navController;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase and Persistence
        com.google.firebase.FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            // Connect bottom nav
            NavigationUI.setupWithNavController(bottomNav, navController);

            // Listen for destination changes to show/hide the BottomNav bar itself
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.splashScreenFragment ||
                        destination.getId() == R.id.loginFragment ||
                        destination.getId() == R.id.signupFragment ||
                        destination.getId() == R.id.mealDetailsFragment) {
                    bottomNav.setVisibility(View.GONE);
                } else {
                    bottomNav.setVisibility(View.VISIBLE);
                }
            });
        }

        // Initialize the AuthStateListener to handle Calendar visibility in real-time
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            // A "Real User" is someone who is logged in and NOT an anonymous guest
            boolean isRealUser = (user != null && !user.isAnonymous());

            // Update the menu item visibility immediately whenever auth state changes
            if (bottomNav != null) {
                bottomNav.getMenu().findItem(R.id.calendarFragment).setVisible(isRealUser);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register the listener to start tracking login/logout/guest status
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Remove the listener to prevent memory leaks
        if (authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }
}