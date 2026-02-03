package com.example.foodplaner.homescreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.homescreen.presenter.HomeScreenPresenter;
import com.example.foodplaner.homescreen.presenter.PresenterInterface;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.R;

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

    View includeView ;

    ImageView mealImg ;
    TextView mealName ;
    TextView mealArea;

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
        presenter = new HomeScreenPresenter(this);

        getParentFragmentManager().setFragmentResultListener(
                "random_meal_key",
                getViewLifecycleOwner(),
                (key, bundle) -> {
                    randomMeal = bundle.getParcelable("random_meal");
                    if (randomMeal != null) {
                        bindRandomMeal(randomMeal);
                    }
                }
        );
        if(randomMeal == null){
            presenter.requestRandomMeal();
        } else {
            bindRandomMeal(randomMeal);
        }
        presenter.getAllMeals();

        includeView = view.findViewById(R.id.mealOfTheDayLayout);

        mealImg = includeView.findViewById(R.id.big_meal_img);
        mealName = includeView.findViewById(R.id.big_meal_name);
        mealArea = includeView.findViewById(R.id.big_meal_og);

        includeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.navigateToMealDetails(randomMeal);
            }
        });

        recyclerView = view.findViewById(R.id.meals_recycler_view);

        // 2-column GridLayout
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));



        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        presenter.logoutUser();
            }
        });

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
        Bundle bundle = new Bundle();
        bundle.putParcelable("random_meal", meal);

        getParentFragmentManager().setFragmentResult("random_meal_key", bundle);


        bindRandomMeal(meal);
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
                .navigate(R.id.action_homeFragment_to_loginFragment);
    }

    @Override
    public void navigateToMealDetails(MealDTO meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal", randomMeal);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
    }
}