package com.example.planetzeapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class DayTrackerView extends AppCompatActivity {
    private ListView daysListView;
    private TextView logTextView;
    private List<String> daysList;
    private DayTracker dayTracker;

    private FrameLayout activityFormContainer;
    private EditText activityNameEditText;
    private Spinner categorySpinner;
    private Spinner subtypeSpinner;
    private EditText magnitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_tracker);

        dayTracker = new DayTracker();

        daysListView = findViewById(R.id.daysListView);
        logTextView = findViewById(R.id.logTextView);
        activityFormContainer = findViewById(R.id.activityForm);
        activityNameEditText = findViewById(R.id.activityNameEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        subtypeSpinner = findViewById(R.id.subtypeSpinner);
        magnitudeEditText = findViewById(R.id.magnitudeEditText);

        daysList = new ArrayList<>();
        daysList.add("2024-12-01");
        daysList.add("2024-12-02");
        daysList.add("2024-12-03");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daysList);
        daysListView.setAdapter(adapter);

        daysListView.setOnItemClickListener((parentView, view, position, id) -> {
            String selectedDay = daysList.get(position);
            showDayLog(selectedDay);
        });

        Button addActivityButton = findViewById(R.id.addActivityButton);
        addActivityButton.setOnClickListener(v -> loadForm("add"));

        Button modifyActivityButton = findViewById(R.id.modifyActivityButton);
        modifyActivityButton.setOnClickListener(v -> loadForm("modify"));

        Button deleteActivityButton = findViewById(R.id.deleteActivityButton);
        deleteActivityButton.setOnClickListener(v -> loadForm("delete"));

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> subtypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.subtypes, android.R.layout.simple_spinner_item);
        subtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subtypeSpinner.setAdapter(subtypeAdapter);
    }

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

        // Initialize the new views after inflating the layout
        activityNameEditText = findViewById(R.id.activityNameEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        subtypeSpinner = findViewById(R.id.subtypeSpinner);
        magnitudeEditText = findViewById(R.id.magnitudeEditText);

        // Set up the submit button action based on the form
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> handleSubmit(action));
    }

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

        // Perform action based on the form (Add, Modify, Delete)
        switch (action) {
            case "add":
                // Add activity
                dayTracker.addActivity(activityName, category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Added", Toast.LENGTH_SHORT).show();
                break;

            case "modify":
                // Modify activity
                dayTracker.modActivity(category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Modified", Toast.LENGTH_SHORT).show();
                break;

            case "delete":
                // Delete activity
                dayTracker.delActivity(category, subtype, magnitude);
                Toast.makeText(DayTrackerView.this, "Activity Deleted", Toast.LENGTH_SHORT).show();
                break;
        }

        showDayLog(dayTracker.getDate());
        activityFormContainer.setVisibility(View.GONE);
    }

    private void showDayLog(String day) {
        String log = "Log for " + day + ":\n";
        log += "Activity: Walk\nEmission: " + dayTracker.getDailyEmission() + " tons CO2e\n";
        logTextView.setText(log);
    }
}
