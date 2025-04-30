package com.example.spectacleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Models.TimeSlot;
import com.example.spectacleapp.R;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeViewHolder> {

    private Context context;
    private List<TimeSlot> timeSlotList;
    private int selectedPosition = -1; // Keeps track of the selected time slot position
    private OnTimeSlotClickListener onTimeSlotClickListener;

    // Constructor for the adapter, accepts Context, timeSlotList, and listener
    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList, OnTimeSlotClickListener listener) {
        this.context = context;
        this.timeSlotList = (timeSlotList != null) ? timeSlotList : new ArrayList<>();
        this.onTimeSlotClickListener = listener;
    }

    // Setter for selected position
    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    // Interface to communicate the selected time slot with the activity
    public interface OnTimeSlotClickListener {
        void onTimeSlotSelected(TimeSlot timeSlot);
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        TimeSlot slot = timeSlotList.get(position);
        holder.timeTextView.setText(slot.getTime());

        // Change background color for selected time slot
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.orange)); // Highlight selected time slot
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.grey)); // Default background color
        }

        // Set click listener to update the selection
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = position;

            // Notify the listener (activity) with the selected time slot
            if (onTimeSlotClickListener != null) {
                onTimeSlotClickListener.onTimeSlotSelected(slot);

                // Show a Toast message when a time slot is selected
                String message = "Selected Time Slot: " + slot.getTime();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            // Notify the adapter that the item selection has changed
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return (timeSlotList != null) ? timeSlotList.size() : 0;
    }

    // Update the time slots list and notify the adapter
    public void updateList(List<TimeSlot> newList) {
        this.timeSlotList.clear();
        if (newList != null) {
            this.timeSlotList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

}
