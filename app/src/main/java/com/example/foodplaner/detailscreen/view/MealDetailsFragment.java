package com.example.foodplaner.detailscreen.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.model.MealDTO;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetailsFragment extends Fragment {
    TextView name;
    Button location;
    RecyclerView ing_rv;
    TextView steps;
    Button back_btn;
    Button add_to_calendar;
    MealDTO meal;
    ImageView meal_image;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealDetailsFragment newInstance(String param1, String param2) {
        MealDetailsFragment fragment = new MealDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.meal_name_details_page);
        meal_image = view.findViewById(R.id.meal_img_details_page);
        location = view.findViewById(R.id.location_btn_details_page);
        ing_rv = view.findViewById(R.id.rv_ing_details_page);
        steps = view.findViewById(R.id.meal_steps_details_page);
        back_btn = view.findViewById(R.id.back_btn_details_page);
        add_to_calendar = view.findViewById(R.id.add_meal_to_calendar_btn);
        add_to_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, name.getText().toString() + " Added to favorites", Snackbar.LENGTH_SHORT).show();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MealDetailsFragment.this).navigateUp();
            }
        });

        meal = getArguments().getParcelable("meal");

        if (meal != null) {
            name.setText(meal.getStrMeal());
            location.setText(meal.getStrArea());
            steps.setText(meal.getStrInstructions());
            Glide.with(getContext())
                    .load(meal.getStrMealThumb()) // URL
                    .placeholder(R.drawable.applogo) // optional
                    .error(R.drawable.ic_launcher_background)       // optional
                    .into(meal_image);

            setupIngredientsRecycler(meal);
            YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = extractYoutubeId(meal.getStrYoutube());
                    youTubePlayer.cueVideo(videoId, 0);

                }
            });
        }



    }
    private void setupIngredientsRecycler(MealDTO meal) {
        ing_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ing_rv.setNestedScrollingEnabled(false);

        IngredientAdapter adapter = new IngredientAdapter(
                meal.getIngredients(),
                meal.getMeasures()
        );
        ing_rv.setAdapter(adapter);
    }
    private String extractYoutubeId(String url) {
        if (url == null || url.trim().isEmpty()) return null;
        try {
            if (url.contains("youtu.be/")) {
                String[] parts = url.split("youtu.be/");
                return parts[1].split("\\?")[0].split("&")[0];
            } else if (url.contains("v=")) {
                String[] parts = url.split("v=");
                return parts[1].split("&")[0].split("\\?")[0];
            }
        } catch (Exception e) {
            Log.e("YOUTUBE_ERROR", "Error parsing: " + url);
        }
        return null;
    }




}