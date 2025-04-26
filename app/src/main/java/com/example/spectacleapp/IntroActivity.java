package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.MainActivity;
import com.example.spectacleapp.R;

public class IntroActivity extends AppCompatActivity {

    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);  // Make sure the XML layout is named correctly

        // Initialize the button
        getStartedButton = findViewById(R.id.button);

        // Set the button's onClickListener
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the next activity when the button is clicked
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);  // Change MainActivity to your next screen
                startActivity(intent);
                finish();  // Optionally finish this activity to prevent the user from going back
            }
        });
    }
}
