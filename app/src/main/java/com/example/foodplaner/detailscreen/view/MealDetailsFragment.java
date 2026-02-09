package com.example.foodplaner.detailscreen.view;

import android.app.DatePickerDialog;
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
import com.example.foodplaner.detailscreen.presenter.MealDetailsPresenter;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.repository.LocalRepositoryImp;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealDetailsFragment extends Fragment implements MealDetailsViewInterface{
    TextView name;
    Button location;
    RecyclerView ing_rv;
    TextView steps;
    Button back_btn;
    MaterialButton add_to_favorites;
    MaterialButton add_to_calendar;
    MealDTO meal;
    ImageView meal_image;
    private MealDetailsPresenter presenter;
    private YouTubePlayerView youTubePlayerView;
    private boolean isFavorite = false;
    private boolean isPlanned = false;


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
        // 1. Initialize Views
        initViews(view);

        // 2. Setup Presenter
        presenter = new MealDetailsPresenter(this, LocalRepositoryImp.getInstance(requireContext()));

        // 3. Get Data and Bind
        meal = getArguments().getParcelable("meal");
        if (meal != null) {
            bindMealData(view, meal);
            presenter.checkIfFavorite(meal.getStrIdMeal());
        }

        // 4. Navigation
        back_btn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        meal = getArguments().getParcelable("meal");

        if (meal != null) {
            // UI Binding
            name.setText(meal.getStrMeal());
            location.setText(meal.getStrArea());
            steps.setText(meal.getStrInstructions());
            Glide.with(this).load(meal.getStrMealThumb()).placeholder(R.drawable.applogo).into(meal_image);
            setupIngredientsRecycler(meal);

            // Database logic
            presenter.checkIfFavorite(meal.getStrIdMeal());

            // Favorite Button Logic
            add_to_favorites.setOnClickListener(v -> {
                if (isFavorite) {
                    presenter.removeFromFavorite(meal);
                    // Note: onFavoriteStatusChanged will handle the UI update via the presenter
                } else {
                    presenter.addToFavorite(meal);
                }
            });

            // YouTube Logic
            getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = extractYoutubeId(meal.getStrYoutube());
                    if (videoId != null) youTubePlayer.cueVideo(videoId, 0);
                }
            });
        }

        back_btn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        presenter.checkIfPlanned(meal.getStrIdMeal());

        add_to_calendar.setOnClickListener(v -> {

            showDatePicker();

        });


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


    @Override
    public void onFavoriteStatusChanged(boolean isFav) {
        this.isFavorite = isFav;
        if (isFavorite) {
            add_to_favorites.setIconResource(R.drawable.heart_broken); // or your "X" icon
            add_to_favorites.setIconTintResource(R.color.black); // visually show it's selected
        } else {
            add_to_favorites.setIconResource(R.drawable.favorite); // empty heart
            add_to_favorites.setIconTintResource(R.color.white);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarStatusChanged(boolean planned) {

    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {

                    String date = year + "-" + (month+1) + "-" + dayOfMonth;

                    presenter.addToCalendar(meal, date);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (youTubePlayerView != null) {
            youTubePlayerView.release();
            youTubePlayerView = null;
        }
    }

    private void bindMealData(View view, MealDTO meal) {
        name.setText(meal.getStrMeal());
        location.setText(meal.getStrArea());
        steps.setText(meal.getStrInstructions());

        Glide.with(this).load(meal.getStrMealThumb()).into(meal_image);
        setupIngredientsRecycler(meal);

        // Favorite Toggle
        add_to_favorites.setOnClickListener(v -> {
            if (isFavorite) {
                presenter.removeFromFavorite(meal);
            } else {
                presenter.addToFavorite(meal);
            }
        });

        // YouTube
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = extractYoutubeId(meal.getStrYoutube());
                if(videoId != null) youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }
    private void initViews(View view) {
        name = view.findViewById(R.id.meal_name_details_page);
        meal_image = view.findViewById(R.id.meal_img_details_page);
        location = view.findViewById(R.id.location_btn_details_page);
        ing_rv = view.findViewById(R.id.rv_ing_details_page);
        steps = view.findViewById(R.id.meal_steps_details_page);
        back_btn = view.findViewById(R.id.back_btn_details_page);
        add_to_favorites = view.findViewById(R.id.add_meal_to_calendar_btn);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        add_to_calendar = view.findViewById(R.id.add_to_calendar_btn);
        // Inside onViewCreated or a dedicated check method
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || user.isAnonymous()) {
            // Hide the buttons so the guest can't even click them
            add_to_favorites.setVisibility(View.GONE);
            add_to_calendar.setVisibility(View.GONE);
        } else {
            add_to_favorites.setVisibility(View.VISIBLE);
            add_to_calendar.setVisibility(View.VISIBLE);
        }
    }
}
