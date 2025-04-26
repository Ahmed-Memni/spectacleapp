package com.example.spectacleapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private Context context;
    private List<String> seatList;
    private Set<Integer> selectedPositions = new HashSet<>();

    public SeatAdapter(Context context, List<String> seatList) {
        this.context = context;
        this.seatList = seatList;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        String seat = seatList.get(position);
        holder.seatText.setText(seat);

        // Set color based on selection
        if (selectedPositions.contains(position)) {
            holder.seatText.setBackgroundColor(Color.parseColor("#333333")); // dark grey when selected
        } else {
            if (seat.startsWith("A")) {
                holder.seatText.setBackgroundColor(Color.parseColor("#D14706")); // orange (#D14706)
            } else if (seat.startsWith("U")) {
                holder.seatText.setBackgroundColor(Color.parseColor("#808080")); // grey
            }
        }

        holder.seatText.setOnClickListener(v -> {
            if (seat.startsWith("A")) { // Only allow clicking A seats
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position); // unselect
                } else {
                    selectedPositions.add(position); // select
                }
                notifyItemChanged(position);
            }
            // If U seat → do nothing
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView seatText;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatText = itemView.findViewById(R.id.seatTextView);
        }
    }

    public void updateSeatList(List<String> newSeatList) {
        this.seatList = newSeatList;
        notifyDataSetChanged();
    }
}
