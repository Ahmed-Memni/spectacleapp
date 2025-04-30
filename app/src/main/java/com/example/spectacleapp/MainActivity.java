package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.Adapter.SidebarPagerAdapter;
import com.example.spectacleapp.Models.Spectacles;
import com.example.spectacleapp.Models.TimeSlot;
import com.example.spectacleapp.Models.DaySchedule;
import com.example.spectacleapp.Utils.SpectaclesFilterUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ProgressBar progressBarSlider;
    private ProgressBar progressBarTop;  // Second progress bar for the RecyclerView (Sidebar)
    private RecyclerView recyclerViewTopMovies;  // New RecyclerView instance
    private RecyclerView recyclerView; // New RecyclerView with ID "recyclerView"
    private ProgressBar progressBarUpcoming;

    private DatabaseReference database;
    private List<Spectacles> spectaclesList = new ArrayList<>(); // List to hold the fetched spectacles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference();

        viewPager = findViewById(R.id.viewPager22);
        progressBarSlider = findViewById(R.id.progressBarSlider);
        progressBarTop = findViewById(R.id.progressBarTop);  // Second progress bar
        progressBarUpcoming = findViewById(R.id.progressBarUpcoming);
        recyclerViewTopMovies = findViewById(R.id.recyclerViewTopMovies);  // Original RecyclerView for top movies
        recyclerView = findViewById(R.id.recyclerView);  // New RecyclerView with ID "recyclerView"

        // Show both progress bars initially
        progressBarSlider.setVisibility(View.VISIBLE);
        progressBarTop.setVisibility(View.VISIBLE);
        progressBarUpcoming.setVisibility(View.VISIBLE);

        ChipNavigationBar chipNavigationBar = findViewById(R.id.chipNavigationBar);
        chipNavigationBar.setOnItemSelectedListener(itemId -> {
            if (itemId == R.id.explorer) {
                // Optional: Avoid reloading current activity
                // startActivity(new Intent(MainActivity.this, MainActivity.class));
                Log.d("Navigation", "Explorer selected");
            } else if (itemId == R.id.reservations) {
                Intent intent = new Intent(MainActivity.this, ReservationListActivity.class);
                startActivity(intent);
            }
        });

        // Fetch spectacles data from Firebase
        fetchSpectaclesFromFirebase();
    }

    private void fetchSpectaclesFromFirebase() {
        database.child("spectaclesList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
                EditText searchBox = findViewById(R.id.editTextText);

                String[] filterOptions = {"Title", "Genre", "Price Maximum", "Year Made", "Rating", "Day"};
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        filterOptions
                );
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFilter.setAdapter(adapterSpinner);

                spectaclesList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Spectacles spectacle = snapshot.getValue(Spectacles.class);
                    spectaclesList.add(spectacle);
                }

                // Full list adapters for both RecyclerViews
                SidebarPagerAdapter fullAdapter = new SidebarPagerAdapter(MainActivity.this, spectaclesList);
                SidebarPagerAdapter fullAdapterrating = new SidebarPagerAdapter(MainActivity.this, SpectaclesFilterUtils.sortByRatingDescending(spectaclesList));
                recyclerViewTopMovies.setAdapter(fullAdapter);
                recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(fullAdapter);

                // Set initial ViewPager content (full list)
                viewPager.setAdapter(fullAdapter);

                // Hide progress bars
                recyclerViewTopMovies.postDelayed(() -> progressBarTop.setVisibility(View.GONE), 1000);
                recyclerView.postDelayed(() -> progressBarUpcoming.setVisibility(View.GONE), 1000);
                viewPager.postDelayed(() -> progressBarSlider.setVisibility(View.GONE), 1000);

                // Live filtering for ViewPager
                searchBox.addTextChangedListener(new android.text.TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String input = s.toString().trim();
                        String selectedFilter = spinnerFilter.getSelectedItem().toString();
                        List<Spectacles> filtered = new ArrayList<>(spectaclesList);

                        switch (selectedFilter) {
                            case "Title":
                                filtered = SpectaclesFilterUtils.filterByTitle(filtered, input);
                                break;
                            case "Genre":
                                filtered = SpectaclesFilterUtils.filterByGenre(filtered, input);
                                break;
                            case "Rating":
                                try {
                                    int rating = Integer.parseInt(input);
                                    filtered = SpectaclesFilterUtils.filterByMinRating(filtered, rating);
                                } catch (NumberFormatException ignored) {}
                                break;
                            case "Year Made":
                                try {
                                    int year = Integer.parseInt(input);
                                    filtered = SpectaclesFilterUtils.filterByYear(filtered, year);
                                } catch (NumberFormatException ignored) {}
                                break;
                            case "Price Maximum":
                                try {
                                    double max = Double.parseDouble(input);
                                    filtered = SpectaclesFilterUtils.filterByPriceRange(filtered,  max);
                                } catch (NumberFormatException ignored) {}
                                break;
                            case "Day":
                                filtered = SpectaclesFilterUtils.filterByDay(filtered, input);
                                break;
                            case "Time":
                                // Optional: implement if needed
                                break;
                        }

                        SidebarPagerAdapter filteredAdapter = new SidebarPagerAdapter(MainActivity.this, filtered);
                        viewPager.setAdapter(filteredAdapter);
                    }

                    @Override
                    public void afterTextChanged(android.text.Editable s) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to fetch data: " + databaseError.getMessage());
            }
        });
    }

}
