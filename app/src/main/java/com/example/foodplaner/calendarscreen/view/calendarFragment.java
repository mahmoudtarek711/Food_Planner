package com.example.foodplaner.calendarscreen.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplaner.R;
import com.example.foodplaner.calendarscreen.presenter.CalendarPresenter;
import com.example.foodplaner.model.DayModel;
import com.example.foodplaner.model.MealRoomDTO;
import com.example.foodplaner.repository.LocalRepositoryImp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class calendarFragment extends Fragment implements CalendarViewInterface {

    private TextView tvYear, tvMonth;
    private RecyclerView rvDays;
    private RecyclerView rvPlannedMeals;

    private CalendarPresenter presenter;
    private Calendar currentCal;
    private PlannedMealsAdapter plannedMealsAdapter;
    private String selectedDate;

    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        presenter = new CalendarPresenter(this, LocalRepositoryImp.getInstance(getContext()));
        currentCal = Calendar.getInstance();

        setupListeners(view);
        syncUI(); // Generate the calendar days
    }

    private void initViews(View view) {
        tvYear = view.findViewById(R.id.tvYear);
        tvMonth = view.findViewById(R.id.tvMonth);

        // 1. Setup Days Horizontal RecyclerView
        rvDays = view.findViewById(R.id.rvDays);
        rvDays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // 2. Setup Planned Meals Vertical RecyclerView
        rvPlannedMeals = view.findViewById(R.id.rvPlannedMeals);
        rvPlannedMeals.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter with the Remove Click listener
        plannedMealsAdapter = new PlannedMealsAdapter(new ArrayList<>(), meal -> {
            presenter.removeMeal(meal);
        });
        rvPlannedMeals.setAdapter(plannedMealsAdapter);
    }

    private void setupListeners(View view) {
        // Year Navigation
        view.findViewById(R.id.btnPrevYear).setOnClickListener(v -> { currentCal.add(Calendar.YEAR, -1); syncUI(); });
        view.findViewById(R.id.btnNextYear).setOnClickListener(v -> { currentCal.add(Calendar.YEAR, 1); syncUI(); });

        // Month Navigation
        view.findViewById(R.id.btnPrevMonth).setOnClickListener(v -> { currentCal.add(Calendar.MONTH, -1); syncUI(); });
        view.findViewById(R.id.btnNextMonth).setOnClickListener(v -> { currentCal.add(Calendar.MONTH, 1); syncUI(); });
    }

    private void syncUI() {
        // 1. Update Year and Month Header
        tvYear.setText(String.valueOf(currentCal.get(Calendar.YEAR)));
        tvMonth.setText(new SimpleDateFormat("MMMM", Locale.getDefault()).format(currentCal.getTime()));

        // 2. Generate Days for the current month
        List<DayModel> days = new ArrayList<>();
        Calendar temp = (Calendar) currentCal.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat displayFmt = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dbFmt = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);

        for (int i = 1; i <= maxDays; i++) {
            days.add(new DayModel(
                    displayFmt.format(temp.getTime()),
                    String.valueOf(i),
                    dbFmt.format(temp.getTime())
            ));
            temp.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 3. Set the Days Adapter
        DayAdapter adapter = new DayAdapter(days, (pos, day) -> {
            selectedDate = day.getFullDate();
            Log.i("CalendarDebug", "Selected Date: " + selectedDate);
            presenter.getMealsByDate(selectedDate);
        });
        rvDays.setAdapter(adapter);
    }

    @Override
    public void showMeals(List<MealRoomDTO> meals) {
        if (meals != null && !meals.isEmpty()) {
            rvPlannedMeals.setVisibility(View.VISIBLE);
            plannedMealsAdapter.setList(meals);
        } else {
            rvPlannedMeals.setVisibility(View.GONE);
            plannedMealsAdapter.setList(new ArrayList<>()); // Clear existing
            Log.i("CalendarDebug", "No meals for this date.");
        }
    }

    @Override
    public void showRemoveSuccess() {

    }

    @Override
    public void onRemoveMealSuccess(MealRoomDTO meal) {
        Toast.makeText(getContext(), "Meal removed", Toast.LENGTH_SHORT).show();
        // Refresh the list from the database to reflect changes
        if (selectedDate != null) {
            presenter.getMealsByDate(selectedDate);
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), "Error: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.clearDisposables();
        }
    }
}