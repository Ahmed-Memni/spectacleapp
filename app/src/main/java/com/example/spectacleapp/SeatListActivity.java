package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Adapter.DateAdapter;
import com.example.spectacleapp.Adapter.SeatAdapter;
import com.example.spectacleapp.Adapter.TimeSlotAdapter;
import com.example.spectacleapp.Models.DaySchedule;
import com.example.spectacleapp.Models.Spectacles;
import com.example.spectacleapp.Models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class SeatListActivity extends AppCompatActivity implements SeatAdapter.OnPriceChangeListener, SeatAdapter.OnSeatSelectionChangeListener {

    private static final String TAG = "SeatListActivity";

    private TimeSlotAdapter timeSlotAdapter;
    private TextView priceTextView;
    private TextView seatsSelectedTextView;
    private RecyclerView seatRecyclerView;
    private SeatAdapter seatAdapter;
    private Spectacles spectacle;
    private DaySchedule selectedDaySchedule;
    private TimeSlot selectedTimeSlot;

    // Global variables to hold selected seats and price
    private List<String> selectedSeats = new ArrayList<>();
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_list);

        // Retrieve the Spectacle object
        spectacle = getIntent().getParcelableExtra("spectacle");

        if (spectacle == null) {
            Log.e(TAG, "Spectacle data is missing");
            Toast.makeText(this, "Error: Spectacle data is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Spectacle loaded: " + spectacle.getTitle());

        setupUI();
        setupDateAndTimeRecyclerViews();
        setupSeatRecyclerView();
        setupBookButton();
    }

    private void setupUI() {
        ImageView backBtn = findViewById(R.id.BackBtn);
        backBtn.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            Intent intent = new Intent(SeatListActivity.this, ImageDetailActivity.class);
            intent.putExtra("spectacle", spectacle);
            startActivity(intent);
            finish();
        });

        priceTextView = findViewById(R.id.PriceText);
        seatsSelectedTextView = findViewById(R.id.Seats_Selected);
    }

    private void setupDateAndTimeRecyclerViews() {
        List<DaySchedule> daySchedules = spectacle.getDaySchedules();
        Log.d(TAG, "Day schedules: " + daySchedules.size());

        // Date RecyclerView
        RecyclerView dateRecyclerView = findViewById(R.id.dateRecyclerview);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DateAdapter dateAdapter = new DateAdapter(daySchedules);
        dateRecyclerView.setAdapter(dateAdapter);

        // Time RecyclerView
        RecyclerView timeRecyclerView = findViewById(R.id.TimeRecyclerview);
        timeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeSlotAdapter = new TimeSlotAdapter(this, new ArrayList<>(), new TimeSlotAdapter.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotSelected(TimeSlot timeSlot) {
                selectedTimeSlot = timeSlot;
                Log.d(TAG, "Time slot selected: " + timeSlot.getTime());
                showTimeSlotMessage(timeSlot);

                // Update the seat list based on the selected time slot
                updateSeatList(timeSlot);
            }
        });
        timeRecyclerView.setAdapter(timeSlotAdapter);

        // When a date is selected
        dateAdapter.setOnItemClickListener(daySchedule -> {
            if (daySchedule != null && daySchedule.getTimeSlots() != null) {
                selectedDaySchedule = daySchedule;
                timeSlotAdapter.updateList(daySchedule.getTimeSlots());
                seatAdapter.updateSeatList(new ArrayList<>());

                if (!daySchedule.getTimeSlots().isEmpty()) {
                    timeSlotAdapter.setSelectedPosition(0); // Select first time slot after date click
                }
            }
        });

        // Preselect the first date if available
        if (!daySchedules.isEmpty() && daySchedules.get(0).getTimeSlots() != null) {
            List<TimeSlot> firstDayTimeSlots = daySchedules.get(0).getTimeSlots();
            timeSlotAdapter.updateList(firstDayTimeSlots);

            if (!firstDayTimeSlots.isEmpty()) {
                timeSlotAdapter.setSelectedPosition(0); // Select first time slot immediately
            }
        }
    }

    private void setupSeatRecyclerView() {
        seatRecyclerView = findViewById(R.id.seatRecycleview);
        seatRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns

        // Initialize with an empty seat list
        List<Double> priceList = spectacle.getPrice();
        double[] priceArray = new double[priceList.size()];
        for (int i = 0; i < priceList.size(); i++) {
            priceArray[i] = priceList.get(i);  // Convert List to array
        }
        seatAdapter = new SeatAdapter(this, new ArrayList<>(), priceArray, this, this);
        seatRecyclerView.setAdapter(seatAdapter);
    }

    private void updateSeatList(TimeSlot timeSlot) {
        if (timeSlot == null) {
            Log.e(TAG, "Selected time slot is null");
            return;
        }

        List<String> availableSeats = timeSlot.getSeats();
        if (availableSeats == null) {
            Log.e(TAG, "Available seats are null for time slot: " + timeSlot.getTime());
            return;
        }

        Log.d(TAG, "Updating seat list for time slot: " + timeSlot.getTime());
        seatAdapter.updateSeatList(availableSeats);
        seatAdapter.notifyDataSetChanged(); // Notify adapter about data change
    }

    private void showTimeSlotMessage(TimeSlot timeSlot) {
        // Show a Toast message with the selected time slot
        String message = "Selected Time Slot: " + timeSlot.getTime();
        Log.d(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupBookButton() {
        Button bookButton = findViewById(R.id.button2);
        bookButton.setOnClickListener(v -> {
            Log.d(TAG, "Book button clicked");

            if (selectedTimeSlot == null || selectedSeats.isEmpty()) {
                Log.e(TAG, "Please select a time and seats");
                Toast.makeText(SeatListActivity.this, "Please select a time and seats", Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare the data to pass
            List<Object> bookingData = new ArrayList<>();
            if (selectedDaySchedule != null) {
                Log.d(TAG, "Selected day: " + selectedDaySchedule.getDate());
                bookingData.add(selectedDaySchedule.getDate()); // First item is the selected date
            } else {
                Log.e(TAG, "No day schedule selected");
                Toast.makeText(this, "Error: No day schedule selected", Toast.LENGTH_SHORT).show();
                return;
            }

            bookingData.add(selectedTimeSlot.getTime());  // Second item is the selected time
            bookingData.addAll(selectedSeats);            // Remaining items are the selected seat positions

            // Log the booking data
            Log.d(TAG, "Booking data: " + bookingData);

            // Pass data to Reserve Activity
            Intent intent = new Intent(SeatListActivity.this, ReservationActivity.class);
            intent.putExtra("spectacle", spectacle);
            intent.putExtra("bookingData", bookingData.toArray(new Object[0]));
            startActivityForResult(intent, 100);
        });
    }

    @Override
    public void onPriceChanged(double totalPrice) {
        this.totalPrice = totalPrice;
        priceTextView.setText(String.format("%.2f Dinar", totalPrice));
    }

    @Override
    public void onSeatSelectionChanged(int selectedCount) {
        seatsSelectedTextView.setText(selectedCount + " Seats Selected");

        selectedSeats.clear();

        List<String> fullSeatList = selectedTimeSlot.getSeats();
        List<String> actuallySelectedSeats = seatAdapter.getSelectedSeats();

        for (String selectedSeat : actuallySelectedSeats) {
            int position = fullSeatList.indexOf(selectedSeat);
            if (position != -1) {
                selectedSeats.add(String.valueOf(position)); // Save the position as a String
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            spectacle = data.getParcelableExtra("updatedSpectacle");

            if (spectacle != null) {
                Log.d(TAG, "Spectacle updated: " + spectacle.getTitle());
                if (selectedTimeSlot != null) {
                    updateSeatList(selectedTimeSlot);
                }
            }
        }
    }
}