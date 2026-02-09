package com.example.foodplaner.searchscreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import com.example.foodplaner.R;
import com.example.foodplaner.homescreen.view.MealsAdapter;
import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.RepositoryImp;
import com.example.foodplaner.searchscreen.presenter.PresenterInterface;
import com.example.foodplaner.searchscreen.presenter.SearchScreenPresenter;
import com.google.android.material.chip.ChipGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements ViewInterface{
    SearchView searchView;
    AdapterSelection adapterSelection;
    private RecyclerView selectionRv, filteredMealRv;
    private MealsAdapter mealAdapter;
    RepositoryImp repository;
    private ChipGroup chipGroup;
    private PresenterInterface presenter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        repository = new RepositoryImp(getContext());
        presenter = new SearchScreenPresenter(repository,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectionRv = view.findViewById(R.id.selection_rv);
        filteredMealRv = view.findViewById(R.id.filtered_meal_rv);
        searchView = view.findViewById(R.id.search_view);
        chipGroup = view.findViewById(R.id.selectors_chip_group); // Make sure you have this ID in XML

        // 1. Setup Selection Adapter (First RV)
        adapterSelection = new AdapterSelection(new ArrayList<>(), item -> {
            if (item instanceof Area) {
                presenter.filterByCountry(((Area) item).getStrArea());
            } else if (item instanceof Category) {
                presenter.filterByCategory(((Category) item).getStrCategory());
            } else if (item instanceof Ingredient) {
                presenter.filterByIngredient(((Ingredient) item).getStrIngredient());
            }
        });
        selectionRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        selectionRv.setAdapter(adapterSelection);

        // 2. Setup Meals Adapter (Second RV)
        mealAdapter = new MealsAdapter(new ArrayList<>(), meal -> {presenter.getFullMeal(meal.getStrMeal());

        });
        filteredMealRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        filteredMealRv.setAdapter(mealAdapter);

        // 3. Chip Selection Listener
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int id = checkedIds.get(0);
            if (id == R.id.country_chip) presenter.getListOfArea();
            else if (id == R.id.category_chip) presenter.getListOfCatgories();
            else if (id == R.id.ingredients_chip) presenter.getListOfIngredients();
        });

        // Default selection
        presenter.getListOfArea();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                presenter.searchMealsLocally(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                presenter.searchMealsLocally(newText);
                return true;
            }
        });


    }


    @Override
    public void showMeals(List<MealDTO> meals) {
        mealAdapter.setList(meals); // You need a setList method in your MealsAdapter
        mealAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCategories(List<Category> categories) {
        adapterSelection.setData(categories);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        adapterSelection.setData(ingredients);
    }

    @Override
    public void showAreas(List<Area> areas) {
        adapterSelection.setData(areas);
    }
    @Override
    public void showFullMeal(MealDTO meal) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("meal", meal);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
    }
    @Override
    public void onResume() {
        super.onResume();

        presenter.restoreState();

    }


}


