package com.example.foodplaner.searchscreen.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Area;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;

import java.util.List;

// AdapterSelection.java
public class AdapterSelection extends RecyclerView.Adapter<AdapterSelection.ViewHolder> {
    private List<Object> itemList; // Generic list to hold Area, Category, or Ingredient
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    public AdapterSelection(List<Object> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public void setData(List<?> newList) {
        this.itemList = (List<Object>) newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_img_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = itemList.get(position);
        String name = "";
        String imageUrl = null;

        if (item instanceof Area) {
            name = ((Area) item).getStrArea();
            String countryCode = CountryMapper.getCode(name);
            imageUrl = "https://flagcdn.com/w640/"+countryCode.toLowerCase()+".png";
            holder.imageView.setImageResource(R.drawable.applogo);
        } else if (item instanceof Category) {
            name = ((Category) item).getStrCategory();
            imageUrl = ((Category) item).getStrCategoryThumb();
        } else if (item instanceof Ingredient) {
            name = ((Ingredient) item).getStrIngredient();
            imageUrl = "https://www.themealdb.com/images/ingredients/" + name + ".png";
        }

        holder.textView.setText(name);
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return itemList != null ? itemList.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.cardImage);
            textView = v.findViewById(R.id.selection_text); // Adjust ID to match your TextView ID
        }
    }
}
