package com.example.spectacleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.spectacleapp.Adapter.Actor;
import com.example.spectacleapp.Adapter.SidebarPagerAdapter;
import com.example.spectacleapp.Models.DaySchedule;
import com.example.spectacleapp.Models.Spectacles;
import com.example.spectacleapp.Models.TimeSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ProgressBar progressBarSlider;
    private ProgressBar progressBarTop;  // Second progress bar for the RecyclerView (Sidebar)
    private RecyclerView recyclerViewTopMovies;  // New RecyclerView instance
    private RecyclerView recyclerView;// New RecyclerView with ID "recyclerView"

    private ProgressBar progressBarUpcoming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Create dummy Spectacles list (replace URLs with real ones later)
        List<Spectacles> spectaclesList = new ArrayList<>();
        // First create TimeSlots for a day
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        ArrayList<TimeSlot> timeSlots1 = new ArrayList<>();
        timeSlots1.add(new TimeSlot(
                "14:00",
                new ArrayList<>(Arrays.asList("A1", "A2", "A3"))
        ));
        timeSlots.add(new TimeSlot(
                "14:00",
                new ArrayList<>(Arrays.asList("A1", "A2", "A3"))
        ));

        timeSlots.add(new TimeSlot(
                "18:00",
                new ArrayList<>(Arrays.asList("B1", "B2", "C1"))
        ));

// Now create a DaySchedule with these TimeSlots
        ArrayList<DaySchedule> daySchedules = new ArrayList<>();

        daySchedules.add(new DaySchedule(
                "2024-04-27",
                timeSlots
        ));


        daySchedules.add(new DaySchedule("2024-04-28",timeSlots1));

        spectaclesList.add(new Spectacles(
                "Spectacle One",
                "A thrilling adventure film.",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdrmElk_ILXrmYrG9M0rqhl9yBWU31OdvTBg&s",
                "2h 15m",
                8,
                2023,
                new double[]{12.99, 15.99, 19.99},
                new ArrayList<>(Arrays.asList("Action", "Adventure")),
                new ArrayList<>(Arrays.asList(
                        new Actor("Sal3a", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSfqgndXs1s0BoHvHRgl4wsYu2E8H9qRwFtFw&s"),
                        new Actor("Rock", "https://cdn.britannica.com/36/147936-050-8E84B614/Dwayne-Johnson.jpg")
                )),
                "https://www.google.com/maps?q=Eiffel+Tower",
                daySchedules // empty daySchedules list for now
        ));

        spectaclesList.add(new Spectacles(
                "Spectacle Two",
                "Romantic comedy with a twist.",
                "https://c8.alamy.com/comp/FXARPC/magic-show-poster-FXARPC.jpg",
                "1h 45m",
                7,
                2022,
                new double[]{15.9, 17.99, 19.99},
                new ArrayList<>(Arrays.asList("Romance", "Comedy")),
                new ArrayList<>(Arrays.asList(
                        new Actor("Thor", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXjJfrUZOmZRNegoiUAoPEWchd-kGBLWAGgw&s"),
                        new Actor("Hoe", "https://ca-times.brightspotcdn.com/dims4/default/57bdaa2/2147483647/strip/true/crop/2048x1365+0+0/resize/1200x800!/quality/75/?url=https%3A%2F%2Fwww.trbimg.com%2Fimg-546baffb%2Fturbine%2Flat-tom-hardy-la0021109433-20140905")
                )),
                "https://goo.gl/maps/XYZ123",
                daySchedules // empty daySchedules list for now
        ));

        // Create and set the adapter for both ViewPager2 and RecyclerView
        SidebarPagerAdapter adapter = new SidebarPagerAdapter(this, spectaclesList);
        viewPager.setAdapter(adapter);

        // Use LinearLayoutManager for RecyclerView
        recyclerViewTopMovies.setAdapter(adapter);  // Set the same adapter for RecyclerView
        recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Optional: For horizontal list
        recyclerViewTopMovies.postDelayed(() -> progressBarSlider.setVisibility(View.GONE), 1000);
        viewPager.postDelayed(() -> progressBarSlider.setVisibility(View.GONE), 1000);
        recyclerViewTopMovies.postDelayed(() -> progressBarTop.setVisibility(View.GONE), 1000);
        // Set up the second RecyclerView ("recyclerView")
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));  // Default vertical layout
        recyclerView.setAdapter(adapter);  // Use the same adapter for this RecyclerView as well
        recyclerView.postDelayed(() -> progressBarUpcoming.setVisibility(View.GONE), 1000);
    }
}
