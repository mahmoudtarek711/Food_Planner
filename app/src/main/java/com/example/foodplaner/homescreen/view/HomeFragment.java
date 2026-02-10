package com.example.foodplaner.homescreen.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplaner.homescreen.presenter.PresenterInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.FirebaseSyncManager;
import com.example.foodplaner.datasource.localdatasource.LocalRepositoryImp;
import com.example.foodplaner.datasource.localdatasource.LocalRepositoryInterface;
import com.example.foodplaner.repository.RepositoryImp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ViewInterface {

    private PresenterInterface presenter;
    private RecyclerView recyclerView;
    private MealsAdapter adapter;
    private List<MealDTO> mealList;
    private Button logout;
    private MealDTO randomMeal;
    private RepositoryImp repository;

    private View includeView;
    private ImageView mealImg;
    private TextView mealName;
    private TextView mealArea;
    private View contentLayout;
    private ProgressBar progressBar;
    private RecyclerView favRecyclerView;
    private TextView yourFavoriteMealstxt;
    private MealsAdapter favAdapter;

    private ConnectivityManager.NetworkCallback networkCallback;
    private Snackbar connectivitySnackbar;
    private boolean wasOffline = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupPresenter();

        connectivitySnackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);

        if (isNetworkAvailable()) {
            wasOffline = false;
            if (connectivitySnackbar.isShown()) connectivitySnackbar.dismiss();
            handleDataLoading(savedInstanceState); // This now handles EVERYTHING
        } else {
            wasOffline = true;
            connectivitySnackbar.show();
            showOfflineUI();
        }

        setupNetworkListener();
        setupUserData(view);

        // Setup the testing long-click here
        mealImg.setOnLongClickListener(v -> {
            android.content.SharedPreferences prefs = requireContext()
                    .getSharedPreferences("MealPrefs", android.content.Context.MODE_PRIVATE);

            prefs.edit().clear().apply(); // Clear timer
            presenter.logoutUser(); // This is a trick to clear the static cachedRandomMeal in your presenter
            // Or better yet, call your request method again
            presenter.requestRandomMeal(prefs);

            Toast.makeText(getContext(), "Test: Memory cleared. Fetching new meal...", Toast.LENGTH_SHORT).show();
            return true;
        });
        mealImg.setOnClickListener(v -> presenter.navigateToMealDetails(randomMeal));
    }

    private void initViews(View view) {
        contentLayout = view.findViewById(R.id.home_scroll_view);
        progressBar = view.findViewById(R.id.progressBar);
        yourFavoriteMealstxt = view.findViewById(R.id.your_favorite_meals_txt);
        favRecyclerView = view.findViewById(R.id.favorite_recycler_view);
        includeView = view.findViewById(R.id.mealOfTheDayLayout);
        mealImg = includeView.findViewById(R.id.big_meal_img);
        mealName = includeView.findViewById(R.id.big_meal_name);
        mealArea = includeView.findViewById(R.id.big_meal_og);
        recyclerView = view.findViewById(R.id.meals_recycler_view);
        logout = view.findViewById(R.id.logout_btn);

        favRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        includeView.setOnClickListener(v -> presenter.navigateToMealDetails(randomMeal));
        logout.setOnClickListener(v -> presenter.logoutUser());
    }

    private void setupPresenter() {
        repository = new RepositoryImp(getContext());
        LocalRepositoryInterface localRepo = LocalRepositoryImp.getInstance(requireContext());
        presenter = new HomeScreenPresenter(this, repository, localRepo);
    }

    private void handleDataLoading(Bundle savedInstanceState) {
        android.content.SharedPreferences prefs = requireContext()
                .getSharedPreferences("MealPrefs", android.content.Context.MODE_PRIVATE);

        // 1. Check if we already have data in memory (Restoring from backstack)
        if (mealList != null && randomMeal != null) {
            displayAllMeals(mealList);
            displayRandomMeal(randomMeal);
            onProcessingEnd(); // Ensure progress bar is hidden
            presenter.getFavoriteMeals();
            return; // STOP HERE - don't trigger more network/loading calls
        }

        // 2. Handle System-level restoration (Bundle)
        if (savedInstanceState != null && savedInstanceState.containsKey("MEALS_LIST")) {
            mealList = savedInstanceState.getParcelableArrayList("MEALS_LIST");
            randomMeal = savedInstanceState.getParcelable("RANDOM_MEAL");

            if (mealList != null) displayAllMeals(mealList);
            if (randomMeal != null) displayRandomMeal(randomMeal);

            onProcessingEnd();
        }
        // 3. Fresh Start logic
        else {
            presenter.getAllMeals(false);
            presenter.requestRandomMeal(prefs);
        }

        presenter.getFavoriteMeals();
        syncFirebaseData();
    }


    private void setupUserData(View view) {
        TextView welcomeText = view.findViewById(R.id.client_name_home_text);
        TextView dateTxt = view.findViewById(R.id.date_txt);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        welcomeText.setText((user != null && user.getDisplayName() != null) ? user.getDisplayName() : "Guest");
        dateTxt.setText(getCurrentDate());
    }

    private void setupNetworkListener() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    if (connectivitySnackbar.isShown()) connectivitySnackbar.dismiss();
                    if (wasOffline) {
                        // Only reload if we actually need data.
                        // If we are showing data, don't refresh to avoid flickering.
                        if (mealList == null || mealList.isEmpty()) {
                            reloadOnlineData();
                        } else {
                            // We have data, just make sure UI is visible
                            contentLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            includeView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Back Online!", Toast.LENGTH_SHORT).show();
                            syncFirebaseData();
                        }
                        wasOffline = false;
                    }
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    wasOffline = true;
                    connectivitySnackbar.show();
                    showOfflineUI();
                });
            }
        };
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    private void showOfflineUI() {
        onProcessingEnd(); // Ensure progress bar is gone
        includeView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        yourFavoriteMealstxt.setVisibility(View.VISIBLE);
        favRecyclerView.setVisibility(View.VISIBLE);
        presenter.getFavoriteMeals();
    }

    private void reloadOnlineData() {
        includeView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        presenter.getAllMeals(true);
        presenter.requestRandomMeal(true);
        syncFirebaseData();
        Toast.makeText(getContext(), "Back Online!", Toast.LENGTH_SHORT).show();
    }

    // In HomeFragment.java

    @Override
    public void displayRandomMeal(MealDTO meal) {
        randomMeal = meal;
        mealName.setText(meal.getStrMeal());
        mealArea.setText(meal.getStrArea());

        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.applogo)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL) // ADD THIS
                .into(mealImg);
    }

    @Override
    public void displayAllMeals(List<MealDTO> meals) {
        mealList = meals;
        adapter = new MealsAdapter(meals, meal -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("meal", meal);
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayFavoriteMeals(List<MealDTO> favorites) {
        if (favorites == null || favorites.isEmpty()) {
            favRecyclerView.setVisibility(View.GONE);
            yourFavoriteMealstxt.setVisibility(View.GONE);
        } else {
            favRecyclerView.setVisibility(View.VISIBLE);
            yourFavoriteMealstxt.setVisibility(View.VISIBLE);
            favAdapter = new MealsAdapter(favorites, meal -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("meal", meal);
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
            });
            favRecyclerView.setAdapter(favAdapter);
        }
    }

    @Override
    public void onProssessing() {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onProcessingEnd() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (contentLayout != null) contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(requireView(), "Update failed. Check connection.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLogoutSuccess() {
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment, null,
                new NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build());
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal", meal);
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mealList != null) outState.putParcelableArrayList("MEALS_LIST", new ArrayList<>(mealList));
        if (randomMeal != null) outState.putParcelable("RANDOM_MEAL", randomMeal);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void syncFirebaseData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.isAnonymous()) {
            String email = user.getEmail();
            if (email != null) {
                FirebaseSyncManager.getInstance(LocalRepositoryImp.getInstance(getContext())).startSync(email);
            }
        }
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.clearResources();
        if (networkCallback != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.unregisterNetworkCallback(networkCallback);
            } catch (Exception e) {
                Log.e("HomeFragment", "Callback unregister error", e);
            }
        }
    }

    @Override public void showMealDetails(String mealID) {}
}