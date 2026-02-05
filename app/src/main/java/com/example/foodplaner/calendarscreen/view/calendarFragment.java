package com.example.foodplaner.calendarscreen.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplaner.R;
import com.example.foodplaner.model.DayModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link calendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class calendarFragment extends Fragment {
    private TextView tvYear, tvMonth, tvMealName;
    private RecyclerView rvDays;
    private View mealCard;
    private Calendar currentCal;
    private LinearLayoutManager layoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public calendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment calendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static calendarFragment newInstance(String param1, String param2) {
        calendarFragment fragment = new calendarFragment();
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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        tvYear = view.findViewById(R.id.tvYear);
        tvMonth = view.findViewById(R.id.tvMonth);
        rvDays = view.findViewById(R.id.rvDays);
        mealCard = view.findViewById(R.id.mealCard);
        tvMealName = view.findViewById(R.id.tvMealName);

        currentCal = Calendar.getInstance();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDays.setLayoutManager(layoutManager);

        // Year Buttons
        view.findViewById(R.id.btnPrevYear).setOnClickListener(v -> { currentCal.add(Calendar.YEAR, -1); syncUI(); });
        view.findViewById(R.id.btnNextYear).setOnClickListener(v -> { currentCal.add(Calendar.YEAR, 1); syncUI(); });

        // Month Buttons
        view.findViewById(R.id.btnPrevMonth).setOnClickListener(v -> { currentCal.add(Calendar.MONTH, -1); syncUI(); });
        view.findViewById(R.id.btnNextMonth).setOnClickListener(v -> { currentCal.add(Calendar.MONTH, 1); syncUI(); });

        // Day Scroll Buttons
        view.findViewById(R.id.btnPrevDayScroll).setOnClickListener(v -> {
            int first = layoutManager.findFirstVisibleItemPosition();
            if (first > 0) rvDays.smoothScrollToPosition(first - 1);
        });
        view.findViewById(R.id.btnNextDayScroll).setOnClickListener(v -> {
            int last = layoutManager.findLastVisibleItemPosition();
            if (last < rvDays.getAdapter().getItemCount() - 1) rvDays.smoothScrollToPosition(last + 1);
        });

        view.findViewById(R.id.btnRemoveMeal).setOnClickListener(v -> mealCard.setVisibility(View.GONE));

        syncUI();
        return view;
    }
    private void syncUI() {
        // Update Text
        tvYear.setText(String.valueOf(currentCal.get(Calendar.YEAR)));
        tvMonth.setText(new SimpleDateFormat("MMMM", Locale.getDefault()).format(currentCal.getTime()));

        // Update Days
        List<DayModel> days = new ArrayList<>();
        Calendar temp = (Calendar) currentCal.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int max = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat fmt = new SimpleDateFormat("EEE", Locale.getDefault());

        for (int i = 1; i <= max; i++) {
            days.add(new DayModel(fmt.format(temp.getTime()), String.valueOf(i)));
            temp.add(Calendar.DAY_OF_MONTH, 1);
        }

        DayAdapter adapter = new DayAdapter(days, (pos, day) -> {
            tvMealName.setText("Meal for " + day.getDayNumber() + " " + tvMonth.getText());
            mealCard.setVisibility(View.VISIBLE);
        });
        rvDays.setAdapter(adapter);
    }
}