package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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

public class SeatListActivity extends AppCompatActivity {

    private TimeSlotAdapter timeSlotAdapter; // Added this for time slots

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_list);

        // Retrieve the Spectacle object passed from previous activity
        Spectacles spectacle = getIntent().getParcelableExtra("spectacle");

        if (spectacle == null) {
            Toast.makeText(this, "Error: Spectacle data is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Handle back button
        ImageView backBtn = findViewById(R.id.BackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SeatListActivity.this, ImageDetailActivity.class);
            if (spectacle != null) {
                intent.putExtra("spectacle", spectacle);
            }
            startActivity(intent);
            finish();
        });

        // Initialize the lists
        List<DaySchedule> daySchedules = spectacle.getDaySchedules();

        // Setup Date RecyclerView
        RecyclerView dateRecyclerView = findViewById(R.id.dateRecyclerview);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DateAdapter dateAdapter = new DateAdapter(daySchedules);
        dateRecyclerView.setAdapter(dateAdapter);

        // Setup Time RecyclerView
        RecyclerView timeRecyclerView = findViewById(R.id.TimeRecyclerview);
        timeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        timeSlotAdapter = new TimeSlotAdapter(this, new ArrayList<TimeSlot>());
        timeRecyclerView.setAdapter(timeSlotAdapter);


        // When a date is clicked
        dateAdapter.setOnItemClickListener(daySchedule -> {
            if (daySchedule != null) {
                List<TimeSlot> timeSlots = daySchedule.getTimeSlots();
                timeSlotAdapter.updateList(timeSlots); // update time slots
            }
        });

        // Preselect the first date and its times automatically
        if (!daySchedules.isEmpty()) {
            List<TimeSlot> initialTimeSlots = daySchedules.get(0).getTimeSlots();
            timeSlotAdapter.updateList(initialTimeSlots);
        }

        // Setup Seat RecyclerView (existing)
        RecyclerView seatRecyclerView = findViewById(R.id.seatRecycleview);
        seatRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns

        ArrayList<String> seatList = new ArrayList<>();
        seatList.add("A1");
        seatList.add("A2");
        seatList.add("A3");
        seatList.add("U1");
        seatList.add("U2");
        seatList.add("U3");
        // add more if needed

        SeatAdapter seatAdapter = new SeatAdapter(this, seatList);
        seatRecyclerView.setAdapter(seatAdapter);
    }
}
