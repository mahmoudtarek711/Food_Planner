package com.example.foodplaner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private List<Meal> meals;

    public MealsAdapter(List<Meal> meals) {
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
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getName());
        holder.mealLocation.setText(meal.getLocation());
        holder.mealDesc.setText(meal.getDescription());
        holder.mealImg.setImageResource(meal.getImageResId());
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg;
        TextView mealName, mealLocation, mealDesc;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.meal_img);
            mealName = itemView.findViewById(R.id.meal_name);
            mealLocation = itemView.findViewById(R.id.meal_og);
            mealDesc = itemView.findViewById(R.id.meal_desc);
        }
    }
}

