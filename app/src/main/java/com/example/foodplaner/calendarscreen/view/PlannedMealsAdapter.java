package com.example.foodplaner.calendarscreen.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.model.MealRoomDTO;
import java.util.List;

public class PlannedMealsAdapter extends RecyclerView.Adapter<PlannedMealsAdapter.ViewHolder> {
    private List<MealRoomDTO> mealList;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onRemoveClick(MealRoomDTO meal);
    }

    public PlannedMealsAdapter(List<MealRoomDTO> mealList, OnMealClickListener listener) {
        this.mealList = mealList;
        this.listener = listener;
    }

    public void setList(List<MealRoomDTO> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We inflate a wrapper that contains the card + the remove button
        // Or you can add the button inside meal_card_view.xml if you prefer
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planned_meal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealRoomDTO meal = mealList.get(position);
        holder.tvName.setText(meal.getStrMeal());
        holder.tvOrigin.setText(meal.getStrArea());
        Glide.with(holder.itemView.getContext()).load(meal.getStrMealThumb()).into(holder.ivMeal);

        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(meal));
    }

    @Override
    public int getItemCount() { return mealList == null ? 0 : mealList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvName, tvOrigin;
        Button btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.meal_img);
            tvName = itemView.findViewById(R.id.meal_name);
            tvOrigin = itemView.findViewById(R.id.meal_og);
            btnRemove = itemView.findViewById(R.id.btnRemoveMeal);
        }
    }
}
