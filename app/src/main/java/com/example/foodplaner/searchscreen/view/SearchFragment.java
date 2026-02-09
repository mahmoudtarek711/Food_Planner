package com.example.foodplaner.searchscreen.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplaner.R;
import com.example.foodplaner.homescreen.view.MealsAdapter;
import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.RepositoryImp;
import com.example.foodplaner.searchscreen.presenter.SearchScreenPresenter;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ViewInterface {

    private SearchView searchView;
    private AdapterSelection adapterSelection;
    private RecyclerView selectionRv, filteredMealRv;
    private MealsAdapter mealAdapter;
    private RepositoryImp repository;
    private ChipGroup chipGroup;
    private SearchScreenPresenter presenter; // Use the concrete class to access clearResources

    // Network Variables
    private ConnectivityManager.NetworkCallback networkCallback;
    private Snackbar connectivitySnackbar;
    private boolean wasOffline = false;

    // Optional: A text view to show "Offline" status in center of screen
    // You might need to add a TextView with id `tv_offline_message` to your XML
    // or just toggle visibility of existing views.

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new RepositoryImp(getContext());
        presenter = new SearchScreenPresenter(repository, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupAdapters();
        setupListeners();

        // Initialize Snackbar
        connectivitySnackbar = Snackbar.make(view, "No Internet Connection - Search Unavailable", Snackbar.LENGTH_INDEFINITE);

        // Check Initial State
        if (isNetworkAvailable()) {
            wasOffline = false;
            showOnlineUI();
            presenter.restoreState(); // Load default data
        } else {
            wasOffline = true;
            connectivitySnackbar.show();
            showOfflineUI();
        }

        setupNetworkListener();
    }

    private void initViews(View view) {
        selectionRv = view.findViewById(R.id.selection_rv);
        filteredMealRv = view.findViewById(R.id.filtered_meal_rv);
        searchView = view.findViewById(R.id.search_view);
        chipGroup = view.findViewById(R.id.selectors_chip_group);
    }

    private void setupAdapters() {
        // Selection Adapter (Horizontal)
        adapterSelection = new AdapterSelection(new ArrayList<>(), item -> {
            if (item instanceof Area) presenter.filterByCountry(((Area) item).getStrArea());
            else if (item instanceof Category) presenter.filterByCategory(((Category) item).getStrCategory());
            else if (item instanceof Ingredient) presenter.filterByIngredient(((Ingredient) item).getStrIngredient());
        });
        selectionRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        selectionRv.setAdapter(adapterSelection);

        // Meals Adapter (Grid)
        mealAdapter = new MealsAdapter(new ArrayList<>(), meal -> presenter.getFullMeal(meal.getStrMeal()));
        filteredMealRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        filteredMealRv.setAdapter(mealAdapter);
    }

    private void setupListeners() {
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.country_chip) presenter.getListOfArea();
            else if (id == R.id.category_chip) presenter.getListOfCatgories();
            else if (id == R.id.ingredients_chip) presenter.getListOfIngredients();
        });

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

    // --- Network Logic ---

    private void setupNetworkListener() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    if (connectivitySnackbar.isShown()) connectivitySnackbar.dismiss();
                    if (wasOffline) {
                        reloadOnlineData();
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
        // Disable or hide search functionality since it needs API
        searchView.setEnabled(false);
        chipGroup.setVisibility(View.GONE);
        selectionRv.setVisibility(View.GONE);
        filteredMealRv.setVisibility(View.GONE);
    }

    private void showOnlineUI() {
        searchView.setEnabled(true);
        chipGroup.setVisibility(View.VISIBLE);
        selectionRv.setVisibility(View.VISIBLE);
        filteredMealRv.setVisibility(View.VISIBLE);
    }

    private void reloadOnlineData() {
        showOnlineUI();
        presenter.getListOfArea(); // Re-fetch default data (Areas)
        Toast.makeText(getContext(), "Search is back online!", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    // --- View Interface Implementation ---

    @Override
    public void showMeals(List<MealDTO> meals) {
        mealAdapter.setList(meals);
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
    public void onDestroyView() {
        super.onDestroyView();

        // 1. Clear Presenter
        if (presenter != null) {
            presenter.clearResources();
        }

        // 2. Unregister Network Callback
        if (networkCallback != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.unregisterNetworkCallback(networkCallback);
            } catch (Exception e) {
                Log.e("SearchFragment", "Callback unregister error", e);
            }
        }
    }
}