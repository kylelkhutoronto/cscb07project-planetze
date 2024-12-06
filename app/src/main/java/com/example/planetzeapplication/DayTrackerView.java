package com.example.planetzeapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

public class DayTrackerView extends AppCompatActivity {
    private ListView daysListView;
    private Spinner daySpinner;
    private DayTracker dayTracker;
    private String userId;
    private List<String> daysList;

    private EditText activityNameEditText;
    private Spinner categorySpinner;
    private Spinner subtypeSpinner;
    private EditText magnitudeEditText;
    private FrameLayout activityFormContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_tracker);

        // Initialize UI elements
        daysListView = findViewById(R.id.daysListView);
        daySpinner = findViewById(R.id.daysSpinner);
        activityFormContainer = findViewById(R.id.activityForm);
        activityNameEditText = findViewById(R.id.activityNameEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        subtypeSpinner = findViewById(R.id.subtypeSpinner);
        magnitudeEditText = findViewById(R.id.magnitudeEditText);

        // Get User ID from Firebase Authentication
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize the DayTracker object
        dayTracker = new DayTracker();

        // Retrieve days from Firebase and display in the spinner
        retrieveDaysFromFirebase();

        // Add listeners for buttons
        Button addActivityButton = findViewById(R.id.addActivityButton);
        addActivityButton.setOnClickListener(v -> loadForm("add"));

        Button modifyActivityButton = findViewById(R.id.modifyActivityButton);
        modifyActivityButton.setOnClickListener(v -> loadForm("modify"));

        Button deleteActivityButton = findViewById(R.id.deleteActivityButton);
        deleteActivityButton.setOnClickListener(v -> loadForm("delete"));
    }

    // Retrieve days from Firebase and update the spinner
    private void retrieveDaysFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference daysRef = database.child("users").child(userId).child("days");

        daysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    daysList = new ArrayList<>();
                    for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                        String date = daySnapshot.getKey();
                        daysList.add(date);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DayTrackerView.this,
                            android.R.layout.simple_spinner_item, daysList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    daySpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DayTrackerView.this, "Failed to retrieve days", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load form based on action (Add, Modify, Delete)
    private void loadForm(String action) {
        activityFormContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        switch (action) {
            case "add":
                inflater.inflate(R.layout.add_activity, activityFormContainer, true);
                break;
            case "modify":
                inflater.inflate(R.layout.mod_activity, activityFormContainer, true);
                break;
            case "delete":
                inflater.inflate(R.layout.del_activity, activityFormContainer, true);
                break;
        }

        // Initialize the new views
        activityNameEditText = findViewById(R.id.activityNameEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        subtypeSpinner = findViewById(R.id.subtypeSpinner);
        magnitudeEditText = findViewById(R.id.magnitudeEditText);

        // Set up the submit button action
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> handleSubmit(action));
    }

    // Handle form submission
    private void handleSubmit(String action) {
        String activityName = activityNameEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String subtype = subtypeSpinner.getSelectedItem().toString();
        double magnitude;

        try {
            magnitude = Double.parseDouble(magnitudeEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(DayTrackerView.this, "Please enter a valid magnitude", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (action) {
            case "add":
                dayTracker.addActivity(activityName, category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Added", Toast.LENGTH_SHORT).show();
                break;
            case "modify":
                dayTracker.modActivity(category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Modified", Toast.LENGTH_SHORT).show();
                break;
            case "delete":
                dayTracker.delActivity(category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Deleted", Toast.LENGTH_SHORT).show();
                break;
        }

        // Update Firebase with the changes
        dayTracker.saveDayTrackerData(userId);
        updateUI();
    }

    // Update the UI with the current day log and daily emission
    private void updateUI() {
        String log = "Activity log for " + dayTracker.getDate() + ":\n";
        log += "Total Emission: " + dayTracker.getDailyEmission() + " tons CO2e";
        daySpinner.setSelection(0);
    }
}