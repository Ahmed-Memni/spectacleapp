package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.Models.DaySchedule;
import com.example.spectacleapp.Models.Reservation;
import com.example.spectacleapp.Models.Spectacles;
import com.example.spectacleapp.Models.TimeSlot;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    private Button proceedButton, cancelButton;

    private Spectacles spectacle;           // Declare globally
    private Object[] bookingDataArray;      // Declare globally

    // Declare ActivityResultLauncher for handling the result
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialize buttons
        proceedButton = findViewById(R.id.proceedButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Initialize result launcher to handle returned data
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Spectacles updatedSpectacle = result.getData().getParcelableExtra("updatedSpectacle");
                        // Handle the updated spectacle, e.g., update UI or data
                        if (updatedSpectacle != null) {
                            Toast.makeText(this, "Spectacle updated", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Spectacles cancelledSpectacle = result.getData().getParcelableExtra("updatedSpectacle");
                        // Handle cancellation
                        if (cancelledSpectacle != null) {
                            Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Retrieve data passed via the intent
        Intent intent = getIntent();
        if (intent != null) {
            spectacle = intent.getParcelableExtra("spectacle"); // Set global
            bookingDataArray = (Object[]) intent.getSerializableExtra("bookingData"); // Set global

            // Ensure data is not null
            if (spectacle != null && bookingDataArray != null) {
                String spectacleName = spectacle.getTitle();
                String date = (String) bookingDataArray[0];
                String timeSlot = (String) bookingDataArray[1];
                ArrayList<String> seatList = new ArrayList<>();
                for (int i = 2; i < bookingDataArray.length; i++) {
                    seatList.add((String) bookingDataArray[i]);
                }

                Toast.makeText(this, "Received spectacle: " + spectacleName + "\nDate: " + date + "\nSeats: " + String.join(", ", seatList), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: Missing booking details", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        proceedButton.setOnClickListener(v -> {
            // Get references
            TextInputLayout nameInputLayout = findViewById(R.id.nameInputLayout);
            TextInputLayout usernameInputLayout = findViewById(R.id.usernameInputLayout);
            TextInputLayout phoneInputLayout = findViewById(R.id.phoneInputLayout);
            TextInputLayout paymentInputLayout = findViewById(R.id.paymentInputLayout);

            EditText nameEditText = findViewById(R.id.nameEditText);
            EditText usernameEditText = findViewById(R.id.usernameEditText);
            EditText phoneEditText = findViewById(R.id.phoneEditText);
            EditText paymentEditText = findViewById(R.id.paymentEditText);

            // Get values
            String name = nameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String phoneNumber = phoneEditText.getText().toString().trim();
            String paymentInfo = paymentEditText.getText().toString().trim();

            // Clear previous errors
            nameInputLayout.setError(null);
            usernameInputLayout.setError(null);
            phoneInputLayout.setError(null);
            paymentInputLayout.setError(null);

            boolean hasError = false;

            // Validate inputs
            if (name.isEmpty()) {
                nameInputLayout.setError("Name is required");
                hasError = true;
            }
            if (username.isEmpty()) {
                usernameInputLayout.setError("Username is required");
                hasError = true;
            }
            if (phoneNumber.isEmpty()) {
                phoneInputLayout.setError("Phone number is required");
                hasError = true;
            } else if (!phoneNumber.matches("\\d{8}")) {
                phoneInputLayout.setError("Phone number must be exactly 8 digits");
                hasError = true;
            }
            if (paymentInfo.isEmpty()) {
                paymentInputLayout.setError("Payment information is required");
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Please correct the errors above", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Proceed with reservation
            String date = (String) bookingDataArray[0];
            String timeSlot = (String) bookingDataArray[1];

            List<Integer> seatIndices = new ArrayList<>();
            for (int i = 2; i < bookingDataArray.length; i++) {
                seatIndices.add(Integer.parseInt(bookingDataArray[i].toString()));
            }

            // Calculate total price
            double totalPrice = 0;
            List<Double> seatPrices = spectacle.getPrice(); // fill in this later
            for (int index : seatIndices) {
                if (spectacle != null) {
                    for (DaySchedule day : spectacle.getDaySchedules()) {
                        if (day.getDate().equals(date)) {
                            for (TimeSlot slot : day.getTimeSlots()) {
                                if (slot.getTime().equals(timeSlot)) {
                                    String seatCode = slot.getSeats().get(index);
                                    if (seatCode.length() >= 2) {
                                        int digit = Character.getNumericValue(seatCode.charAt(1));
                                        if (digit >= 1 && digit <= 3) {
                                            totalPrice += seatPrices.get(digit - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Update spectacle seat status
            for (DaySchedule daySchedule : spectacle.getDaySchedules()) {
                if (daySchedule.getDate().equals(date)) {
                    for (TimeSlot slot : daySchedule.getTimeSlots()) {
                        if (slot.getTime().equals(timeSlot)) {
                            List<String> seats = slot.getSeats();
                            for (int index : seatIndices) {
                                if (index >= 0 && index < seats.size()) {
                                    String seatCode = seats.get(index);
                                    seats.set(index, seatCode.replaceFirst("^[A-Z]", "U"));
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }

            // Get selected seat codes
            List<String> seatCodes = new ArrayList<>();
            for (int i = 2; i < bookingDataArray.length; i++) {
                seatCodes.add((String) bookingDataArray[i]);
            }

            // Create reservation object
            Reservation reservation = new Reservation(
                    name,
                    username,
                    spectacle.getTitle(),
                    spectacle.getPoster(),
                    totalPrice,
                    date,
                    timeSlot,
                    seatCodes
            );

            // Store reservation in Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reservationsRef = database.getReference("reservations");
            reservationsRef.push().setValue(reservation)
                    .addOnSuccessListener(aVoid -> {
                        // Update spectacle seat info after reservation success
                        DatabaseReference spectaclesRef = database.getReference("spectaclesList");
                        spectaclesRef.orderByChild("title").equalTo(spectacle.getTitle())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            snapshot.getRef().setValue(spectacle)
                                                    .addOnSuccessListener(aVoid2 -> {
                                                        Toast.makeText(ReservationActivity.this, "Reservation Successful!", Toast.LENGTH_SHORT).show();
                                                        Intent mainIntent = new Intent(ReservationActivity.this, MainActivity.class);
                                                        mainIntent.putExtra("updatedSpectacle", spectacle);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(ReservationActivity.this, "Failed to update seat info.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(ReservationActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ReservationActivity.this, "Failed to reserve. Try again.", Toast.LENGTH_SHORT).show();
                    });
        });


        // Handle "Cancel" button click
        cancelButton.setOnClickListener(v -> {
            // Pass the spectacle object back when the cancellation occurs
            Toast.makeText(ReservationActivity.this, "Reservation cancelled", Toast.LENGTH_SHORT).show();

            // Create a result intent and pass the spectacle object
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedSpectacle", spectacle); // Pass the spectacle back

            // Set the result and finish the activity to go back to the previous screen
            setResult(RESULT_CANCELED, resultIntent); // Use RESULT_CANCELED for cancellation
            finish();
        });
    }
}
