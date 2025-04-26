package com.example.spectacleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Models.TimeSlot;
import com.example.spectacleapp.R;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeViewHolder> {

    private Context context;
    private List<TimeSlot> timeSlotList;

    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = (timeSlotList != null) ? timeSlotList : new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        return (timeSlotList != null) ? timeSlotList.size() : 0;
    }

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
