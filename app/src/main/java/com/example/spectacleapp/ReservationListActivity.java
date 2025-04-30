package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Adapter.ReservationAdapter;
import com.example.spectacleapp.Models.Reservation;
import com.example.spectacleapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class ReservationListActivity extends AppCompatActivity {

    private RecyclerView reservationsRecyclerView;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        database = FirebaseDatabase.getInstance().getReference();

        // Fetch reservations from Firebase and set the adapter
        fetchReservationsAndFillRecyclerView();

        // Bottom navigation
        ChipNavigationBar chipNavigationBar = findViewById(R.id.chipNavigationBar);
        chipNavigationBar.setOnItemSelectedListener(itemId -> {
            if (itemId == R.id.explorer) {
                startActivity(new Intent(ReservationListActivity.this, MainActivity.class));
            }
        });
    }

    private void fetchReservationsAndFillRecyclerView() {
        database.child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reservation> fetchedReservations = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Reservation reservation = child.getValue(Reservation.class);
                    if (reservation != null) {
                        fetchedReservations.add(reservation);
                    }
                }

                reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(ReservationListActivity.this));
                reservationsRecyclerView.setAdapter(new ReservationAdapter(ReservationListActivity.this, fetchedReservations));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading reservations", error.toException());
            }
        });
    }
}
