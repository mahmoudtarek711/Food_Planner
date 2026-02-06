package com.example.foodplaner.homescreen.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.model.MealDTO;
import com.example.foodplaner.R;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private List<MealDTO> meals;
    private OnMealClickListener listener;

    public MealsAdapter(List<MealDTO> meals, OnMealClickListener listener) {
        this.listener = listener;
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_card_view, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealDTO meal = meals.get(position);
        holder.mealName.setText(meal.getStrMeal());
        holder.mealLocation.setText(meal.getStrArea());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb()) // URL
                .placeholder(R.drawable.applogo) // optional
                .error(R.drawable.ic_launcher_background)       // optional
                .into(holder.mealImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg;
        TextView mealName, mealLocation;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.meal_img);
            mealName = itemView.findViewById(R.id.meal_name);
            mealLocation = itemView.findViewById(R.id.meal_og);
        }
    }
    public interface OnMealClickListener {
        void onMealClick(MealDTO meal);
    }
    public void setList(List<MealDTO> outerList)
    {
        meals = outerList;
    }
}

