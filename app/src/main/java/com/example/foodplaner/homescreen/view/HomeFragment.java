package com.example.foodplaner.homescreen.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.foodplaner.MainActivity;
import com.example.foodplaner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplaner.homescreen.presenter.PresenterInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.R;
import com.example.foodplaner.repository.LocalRepositoryImp;
import com.example.foodplaner.repository.LocalRepositoryInterface;
import com.example.foodplaner.repository.RepositoryImp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ViewInterface {
    PresenterInterface presenter;
    private RecyclerView recyclerView;
    private MealsAdapter adapter;
    private List<MealDTO> mealList;
    private Button logout;
    private MealDTO randomMeal;
    RepositoryImp repository;

    View includeView ;

    ImageView mealImg ;
    TextView mealName ;
    TextView mealArea;
    private View contentLayout;
    private ProgressBar progressBar;
    private RecyclerView favRecyclerView;
    TextView yourFavoriteMealstxt;
    private MealsAdapter favAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentLayout = view.findViewById(R.id.home_scroll_view);
        progressBar = view.findViewById(R.id.progressBar);

        repository = new RepositoryImp();
        LocalRepositoryInterface localRepo = LocalRepositoryImp.getInstance(requireContext());
        presenter = new HomeScreenPresenter(this,repository,localRepo);

        yourFavoriteMealstxt = view.findViewById(R.id.your_favorite_meals_txt);
        favRecyclerView = view.findViewById(R.id.favorite_recycler_view);
        favRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        // Request Favorites
        presenter.getFavoriteMeals();

        // 1. ALWAYS initialize your views first
        includeView = view.findViewById(R.id.mealOfTheDayLayout);
        mealImg = includeView.findViewById(R.id.big_meal_img);
        mealName = includeView.findViewById(R.id.big_meal_name);
        mealArea = includeView.findViewById(R.id.big_meal_og);
        recyclerView = view.findViewById(R.id.meals_recycler_view);
        logout = view.findViewById(R.id.logout_btn);

        TextView welcomeText = view.findViewById(R.id.client_name_home_text);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null) {
            welcomeText.setText(user.getDisplayName());
        } else {
            welcomeText.setText("Guest");
        }
        TextView date = view.findViewById(R.id.date_txt);
        date.setText(getCurrentDate());


        // Set up LayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // 2. Set up listeners
        includeView.setOnClickListener(v -> presenter.navigateToMealDetails(randomMeal));
        logout.setOnClickListener(v -> presenter.logoutUser());

        // 3. Now check if we already have data (Back Stack navigation)
        if (mealList != null && randomMeal != null) {
            displayAllMeals(mealList);
            bindRandomMeal(randomMeal);
            progressBar.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
            return; // Data restored, stop here
        }else
        {
            progressBar.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
        }

        // 4. Handle State Restoration (System kill) vs First Launch
        if (savedInstanceState != null) {
            mealList = savedInstanceState.getParcelableArrayList("MEALS_LIST");
            randomMeal = savedInstanceState.getParcelable("RANDOM_MEAL");
            if (mealList != null && randomMeal != null) {
                progressBar.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }

            if (mealList != null) displayAllMeals(mealList);
            else presenter.getAllMeals(false);

            if (randomMeal != null) bindRandomMeal(randomMeal);
            else presenter.requestRandomMeal(false);

        } else {
            // Fresh launch
            presenter.getAllMeals(false);
            presenter.requestRandomMeal(false);
        }
    }

    @Override
    public void displayRandomMeal(MealDTO meal) {
        randomMeal = meal;
        mealName.setText(meal.getStrMeal());
        mealArea.setText(meal.getStrArea());
        Glide.with(requireContext())
                .load(meal.getStrMealThumb()) // URL
                .placeholder(R.drawable.applogo) // optional
                .error(R.drawable.ic_launcher_background)       // optional
                .into(mealImg);
    }
    private void bindRandomMeal(MealDTO meal) {
        mealName.setText(meal.getStrMeal());
        mealArea.setText(meal.getStrArea());

        Glide.with(requireContext())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.applogo)
                .into(mealImg);
    }


    @Override
    public void displayAllMeals(List<MealDTO> meals) {
        mealList = meals;
        adapter = new MealsAdapter(meals , meal -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("meal", meal);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMealDetails(String mealID) {

    }

    @Override
    public void showLogoutSuccess() {
        NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_homeFragment_to_loginFragment, null,
                        new NavOptions.Builder()
                                .setPopUpTo(R.id.nav_graph, true)
                                .build());
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal", randomMeal);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(requireView(), "Error In Fetching Data...", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onProssessing() {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onProcessingEnd() {

        progressBar.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayFavoriteMeals(List<MealDTO> favorites) {
        // Initialize the TextView variable if it hasn't been initialized yet
        if (yourFavoriteMealstxt == null && getView() != null) {
            yourFavoriteMealstxt = getView().findViewById(R.id.your_favorite_meals_txt);
        }

        if (favorites == null || favorites.isEmpty()) {
            favRecyclerView.setVisibility(View.GONE);
            if (yourFavoriteMealstxt != null) {
                yourFavoriteMealstxt.setVisibility(View.GONE);
            }
        } else {
            favRecyclerView.setVisibility(View.VISIBLE);
            if (yourFavoriteMealstxt != null) {
                yourFavoriteMealstxt.setVisibility(View.VISIBLE);
            }

            favAdapter = new MealsAdapter(favorites, meal -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("meal", meal);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
            });
            favRecyclerView.setAdapter(favAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mealList != null) {
            outState.putParcelableArrayList(
                    "MEALS_LIST",
                    new ArrayList<>(mealList)
            );
        }

        if (randomMeal != null) {
            outState.putParcelable("RANDOM_MEAL", randomMeal);
        }
    }
    private String getCurrentDate() {
        // "MMMM" is full month name (January)
        // "d" is the day (6)
        // "yyyy" is the year (2026)
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
    @Override
    public void onResume() {
        super.onResume();
        // This ensures the favorite list refreshes when you come back from Details
        presenter.getFavoriteMeals();
    }


}