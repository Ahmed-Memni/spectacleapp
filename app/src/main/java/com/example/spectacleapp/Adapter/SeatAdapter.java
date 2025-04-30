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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private Context context;
    private List<String> seatList;
    private Set<Integer> selectedPositions = new HashSet<>();
    private double[] seatPrices; // Store the prices for each seat selection
    private OnPriceChangeListener priceChangeListener; // Listener for price changes
    private OnSeatSelectionChangeListener seatSelectionListener; // Listener for seat selection changes

    // Listener interface to notify the activity when the price changes
    public interface OnPriceChangeListener {
        void onPriceChanged(double totalPrice); // Callback method to update the price
    }

    // Listener interface to notify the activity when the seat selection count changes
    public interface OnSeatSelectionChangeListener {
        void onSeatSelectionChanged(int selectedCount); // Callback method to update the seat selection count
    }

    // Constructor for the adapter, passing both listeners to the adapter
    public SeatAdapter(Context context, List<String> seatList, double[] seatPrices,
                       OnPriceChangeListener priceChangeListener, OnSeatSelectionChangeListener seatSelectionListener) {
        this.context = context;
        this.seatList = seatList;
        this.seatPrices = seatPrices;
        this.priceChangeListener = priceChangeListener;
        this.seatSelectionListener = seatSelectionListener;
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
                updatePrice(); // Update price when selection changes
                updateSelectedSeatsCount(); // Update seat count when selection changes
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

    // Update the seat list if necessary
    public void updateSeatList(List<String> newSeatList) {
        this.seatList = newSeatList;
        notifyDataSetChanged(); // Notify adapter that the seat list has changed
    }

    // Update price based on selected seats
    private void updatePrice() {
        double totalPrice = 0;
        for (Integer position : selectedPositions) {
            // Price depends on which seat (first, second, or third price)
            if (position < seatPrices.length) {
                totalPrice += seatPrices[position]; // Add the corresponding price
            }
        }
        // Notify the activity that the price has changed
        if (priceChangeListener != null) {
            priceChangeListener.onPriceChanged(totalPrice);
        }
    }

    // Update seat selection count
    private void updateSelectedSeatsCount() {
        int selectedCount = selectedPositions.size();
        // Notify the activity about the updated selected seat count
        if (seatSelectionListener != null) {
            seatSelectionListener.onSeatSelectionChanged(selectedCount);
        }
    }

    // Method to get the list of selected seats
    public List<String> getSelectedSeats() {
        List<String> selectedSeats = new ArrayList<>();
        for (Integer position : selectedPositions) {
            if (position < seatList.size()) {
                selectedSeats.add(seatList.get(position)); // Add the seat corresponding to the selected position
            }
        }
        return selectedSeats;
    }
}
