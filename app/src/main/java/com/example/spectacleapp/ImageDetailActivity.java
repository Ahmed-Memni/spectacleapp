package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spectacleapp.Adapter.Actor;
import com.example.spectacleapp.Models.Spectacles; // Make sure to import Spectacles model

import java.util.ArrayList;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Find views
        ImageView imageView = findViewById(R.id.imageDetailView);
        TextView titleText = findViewById(R.id.imageTitle);
        TextView descText = findViewById(R.id.imageDescription);

        TextView ratingText = findViewById(R.id.imageRating);
        TextView yearText = findViewById(R.id.imageYear);
        TextView priceText = findViewById(R.id.imagePrice);
        TextView genreText = findViewById(R.id.imageGenre);
        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        Button doNothingBtn = findViewById(R.id.doNothingBtn);
        LinearLayout actorsContainer = findViewById(R.id.actorsContainer);

        // Retrieve the Spectacles object passed from the previous activity
        Spectacles spectacle = getIntent().getParcelableExtra("spectacle");

        if (spectacle != null) {
            // Set the image, title, and description from the Spectacles object
            Glide.with(this)
                    .load(spectacle.getPoster())
                    .fitCenter()
                    .into(imageView);

            titleText.setText(spectacle.getTitle());
            descText.setText(spectacle.getDescription());
            ratingText.setText("Rating: " + spectacle.getRat());  // Assuming 'rat' is the rating
            yearText.setText("Year: " + spectacle.getYear());
            double[] prices = spectacle.getPrice();
            if (prices != null && prices.length == 3) {
                priceText.setText("Prices: Dinar " + prices[0] + ", " + prices[1] + ", " + prices[2]);
            } else {
                priceText.setText("Prices: not available");
            }

            genreText.setText("Genre: " + String.join(", ", spectacle.getGenre()));

            // Add actors to the actors container
            // Add actors to the actors container
            ArrayList<Actor> actorsList = spectacle.getCasts();
            if (actorsList != null) {
                for (Actor actor : actorsList) {
                    // Create a container for the actor image and name
                    LinearLayout actorContainer = new LinearLayout(this);
                    actorContainer.setOrientation(LinearLayout.VERTICAL);
                    actorContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    actorContainer.setGravity(Gravity.CENTER);

                    // Create the ImageView for the actor's image
                    ImageView actorImage = new ImageView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                    params.setMargins(16, 0, 16, 0);
                    actorImage.setLayoutParams(params);
                    actorImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(this)
                            .load(actor.getImageUrl())
                            .circleCrop()
                            .into(actorImage);

                    // Create the TextView for the actor's name
                    TextView actorName = new TextView(this);
                    actorName.setText(actor.getName());
                    actorName.setGravity(Gravity.CENTER);
                    actorName.setPadding(0, 8, 0, 0); // Padding to space out the name from the image

                    // Add the ImageView and TextView to the actor container
                    actorContainer.addView(actorImage);
                    actorContainer.addView(actorName);

                    // Add the actor container to the actorsContainer
                    actorsContainer.addView(actorContainer);
                }
            }

        }


        // Back to Main Activity button
        backToMainBtn.setOnClickListener(v -> {
            // Create an Intent to start SeatListActivity
            if (spectacle != null && spectacle.getGoogleMapsLink() != null) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(android.net.Uri.parse(spectacle.getGoogleMapsLink()));
                startActivity(mapIntent);
            }
        });
        // Find the back button by its ID
        ImageView backButton = findViewById(R.id.Backbutton);

// Set up the click listener to navigate to MainActivity
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ImageDetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
            startActivity(intent);
            finish(); // Optional: close ImageDetailActivity
        });

        // Do Nothing button (for example, navigate to SeatListActivity)
        doNothingBtn.setOnClickListener(v -> {
            // Create an Intent to start SeatListActivity
            Intent intent = new Intent(ImageDetailActivity.this, SeatListActivity.class);

            // Pass the Spectacles object to SeatListActivity
            if (spectacle != null) {
                intent.putExtra("spectacle", spectacle);
            }

            // Start the SeatListActivity
            startActivity(intent);
        });


    }
}
