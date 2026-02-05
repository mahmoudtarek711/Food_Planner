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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.homescreen.view.MealsAdapter;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.model.MealResponse;
import com.example.foodplaner.network.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private RecyclerView selectionRv, filteredMealRv;
    private SelectionAdapter selectionAdapter;
    private MealsAdapter mealAdapter;

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

        // 1. Selection RV
        selectionRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        selectionAdapter = new SelectionAdapter(getCountryImages());
        selectionRv.setAdapter(selectionAdapter);

        // 2. Filtered Meals RV
        filteredMealRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // IMPORTANT: Call the method to actually fetch data!
        getDummyMealData();
    }

    // Inside SearchFragment class
    private List<Integer> getCountryImages() {
        List<Integer> images = new ArrayList<>();
        // Add your drawable resources here
        images.add(R.drawable.brazil_flag);
        images.add(R.drawable.brazil_flag); // Replace with your actual drawable names
        images.add(R.drawable.brazil_flag);
        images.add(R.drawable.brazil_flag);
        return images;
    }

    private void getDummyMealData() {

    }
}

class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder> {

    private final List<Integer> selectionList; // Changed to Integer

    public SelectionAdapter(List<Integer> selectionList) {
        this.selectionList = selectionList;
    }

    @NonNull
    @Override
    public SelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selection_img_cardview, parent, false);
        return new SelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionViewHolder holder, int position) {
        int imageResId = selectionList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(imageResId) // Loads the drawable ID
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                .into(holder.selectionImage);
    }

    @Override
    public int getItemCount() {
        return selectionList.size();
    }

    class SelectionViewHolder extends RecyclerView.ViewHolder {
        ImageView selectionImage;

        public SelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            selectionImage = itemView.findViewById(R.id.cardImage);
        }
    }
}

