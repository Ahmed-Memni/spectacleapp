package com.example.spectacleapp.Adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Models.DaySchedule;
import com.example.spectacleapp.R;

import java.util.List;

public class  DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<DaySchedule> daySchedules;
    private OnItemClickListener listener;
    private int selectedPosition = 0; // First one selected by default

    public DateAdapter(List<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_button, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DaySchedule daySchedule = daySchedules.get(position);
        holder.dateButton.setText(daySchedule.getDate());

        // Highlight selected item
        if (selectedPosition == position) {
            holder.dateButton.setBackgroundColor(Color.parseColor("#D14706"));
            holder.dateButton.setTextColor(Color.WHITE);
        } else {
            holder.dateButton.setBackgroundColor(Color.LTGRAY);
            holder.dateButton.setTextColor(Color.BLACK);
        }

        holder.dateButton.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onItemClick(daySchedule); // Call the callback
            }
        });
    }

    @Override
    public int getItemCount() {
        return daySchedules.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        Button dateButton;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateButton = itemView.findViewById(R.id.dateButton);
        }
    }

    // ✅ New listener interface
    public interface OnItemClickListener {
        void onItemClick(DaySchedule daySchedule);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}