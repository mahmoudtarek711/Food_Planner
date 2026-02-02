package com.example.foodplaner;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MealsAdapter adapter;
    private List<MealResponse> mealList;
    private Button logout;

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
        recyclerView = view.findViewById(R.id.meals_recycler_view);

        // 2-column GridLayout
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Dummy data
        mealList = new ArrayList<>();

        mealList.add(new MealResponse(
                "Smash Burger",
                "https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg",
                "USA",
                Arrays.asList("Ground Beef", "Cheddar Cheese", "Buns", "Lettuce", "Tomato"),
                Arrays.asList("200g", "2 slices", "2", "2 leaves", "2 slices"),
                "Form beef into patties, cook on hot skillet. Assemble with cheese, lettuce, tomato, and buns.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));

        mealList.add(new MealResponse(
                "Veggie Pizza",
                "https://www.themealdb.com/images/media/meals/x0lk931587671540.jpg",
                "Italy",
                Arrays.asList("Pizza Dough", "Tomato Sauce", "Mozzarella", "Bell Peppers", "Olives"),
                Arrays.asList("1 base", "3 tbsp", "150g", "1 sliced", "10"),
                "Spread tomato sauce on dough, add veggies and cheese, bake at 220°C for 12-15 mins.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));

        mealList.add(new MealResponse(
                "Sushi Platter",
                "https://www.themealdb.com/images/media/meals/g046bb1663960946.jpg",
                "Japan",
                Arrays.asList("Sushi Rice", "Nori", "Salmon", "Avocado", "Cucumber"),
                Arrays.asList("300g", "5 sheets", "200g", "1 sliced", "1 sliced"),
                "Prepare sushi rice, cut fish and veggies, roll with nori, slice and serve.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));

        mealList.add(new MealResponse(
                "Pasta Carbonara",
                "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
                "Italy",
                Arrays.asList("Spaghetti", "Eggs", "Parmesan", "Pancetta", "Black Pepper"),
                Arrays.asList("200g", "2", "50g", "100g", "to taste"),
                "Cook spaghetti. Fry pancetta. Mix eggs and parmesan. Combine with pasta off heat.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));

        mealList.add(new MealResponse(
                "Caesar Salad",
                "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
                "USA",
                Arrays.asList("Romaine Lettuce", "Croutons", "Parmesan", "Caesar Dressing", "Chicken"),
                Arrays.asList("1 head", "1 cup", "50g", "3 tbsp", "200g"),
                "Toss lettuce with dressing, add croutons, parmesan, and grilled chicken.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));

        mealList.add(new MealResponse(
                "Tacos",
                "https://www.themealdb.com/images/media/meals/qtuwxu1468233098.jpg",
                "Mexico",
                Arrays.asList("Taco Shells", "Ground Beef", "Lettuce", "Cheese", "Salsa"),
                Arrays.asList("6", "250g", "1 cup", "100g", "3 tbsp"),
                "Cook beef with spices, fill taco shells with beef, lettuce, cheese, salsa.",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ));


        adapter = new MealsAdapter(mealList);
        recyclerView.setAdapter(adapter);


        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.action_homeFragment_to_loginFragment);
            }
        });

    }
}