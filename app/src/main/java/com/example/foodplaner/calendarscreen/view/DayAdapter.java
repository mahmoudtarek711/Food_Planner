package com.example.foodplaner.calendarscreen.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplaner.R;
import com.example.foodplaner.model.DayModel;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private List<DayModel> list;
    private OnDayClickListener listener;
    private int selectedPos = -1;

    public interface OnDayClickListener { void onDayClick(int position, DayModel day); }

    public DayAdapter(List<DayModel> list, OnDayClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayModel day = list.get(position);
        holder.tvNum.setText(day.getDayNumber());
        holder.tvName.setText(day.getDayName());

        holder.itemView.setBackgroundColor(selectedPos == position ? Color.LTGRAY : Color.TRANSPARENT);
        holder.itemView.setOnClickListener(v -> {
            int old = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPos);
            listener.onDayClick(selectedPos, day);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tvDayNumber);
            tvName = itemView.findViewById(R.id.tvDayName);
        }
    }
}
