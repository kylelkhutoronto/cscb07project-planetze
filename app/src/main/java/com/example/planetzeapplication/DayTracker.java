package com.example.planetzeapplication;

import java.util.*;
import java.time.LocalDate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DayTracker {
    HashMap<Long, Double> log;
    double dayEmission;
    LocalDate date;
    static double tonConvert = 0.001102;

    public DayTracker() {
        this.log = new HashMap<>();
        this.dayEmission = 0.0;
        this.date = LocalDate.now();
    }

    public DayTracker(HashMap<Long, Double> log, double totalEmission, LocalDate date) {
        this.log = log;
        this.dayEmission = totalEmission;
        this.date = date;
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activities");

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    if (currentUser == null) {
        Log.e("SurveyActivity", "Error: User not authenticated.");
        finish();
        return;
    }
    String uid = currentUser.getUid();
    public void addData(String uid, String data) {
        try {
            // Get the current user reference
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = database.child("users").child(uid);

            // Get the current date as LocalDate
            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.toString(); // e.g., "2024-12-03"

            // Add data to the structure: users -> user123 -> days -> 2024-12-03
            DatabaseReference dayRef = userRef.child("days").child(formattedDate);
            dayRef.setValue(data);

            System.out.println("Data added to Firebase under user: " + uid + ", date: " + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActivity(String activityName, String category, String subtype, double magnitude) {
        Activity activity = null;
        try {
            activity = ActivityCreator.newActivity(activityName, category, subtype, magnitude);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if (activity != null) {
            double emission = computeEmission(activity);

            // Reference to the Firebase database
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = database.child("users").child(uid);

            // Get the current date as LocalDate
            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.toString(); // e.g., "2024-12-03"
            DatabaseReference dayRef = userRef.child("days").child(formattedDate);

            // Get the current emissions log (if exists)
            dayRef.child("log").child(String.valueOf(activity.getActivityId())).setValue(emission);

            // Optionally, update daily emissions
            dayRef.child("dailyEmission").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Double currentDailyEmission = task.getResult().getValue(Double.class);
                    if (currentDailyEmission == null) {
                        currentDailyEmission = 0.0;
                    }
                    dayRef.child("dailyEmission").setValue(currentDailyEmission + emission);
                }
            });

            System.out.println("Activity added with emission " + emission + " for user " + uid + " on date " + formattedDate);
        } else {
            System.out.println("Invalid reference");
        }
    }


    public void delActivity(String category, String subtype, double magnitude) {
        Activity reference = ActivityCreator.newActivity("temp", category, subtype, magnitude);
        if (log.containsKey(reference.getActivityId())) {
            double oldEmission = log.get(reference.getActivityId());
            double delEmission = computeEmission(reference);
            dayEmission -= delEmission;
            log.put(reference.getActivityId(), oldEmission - delEmission);
        }
        else {
            System.out.println("Activity not found");
        }
    }

    public void modActivity(String category, String subtype, double newMagnitude) {
        Activity modifyActivity = null;
        try {
            modifyActivity = ActivityCreator.newActivity("temp", category, subtype, newMagnitude);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (modifyActivity != null && log.containsKey(modifyActivity.getActivityId())) {
            double oldEmission = log.get(modifyActivity.getActivityId());
            double newEmission = computeEmission(modifyActivity);
            dayEmission -= oldEmission;
            log.put(modifyActivity.getActivityId(), newEmission);
            dayEmission += newEmission;
        }
        else {
            System.out.println("Invalid reference or activity not found");
        }
    }

    public double computeEmission(Activity activity) {
        if (activity != null && activity.getMagnitude() > 0) {
            return activity.computeSessionEmission();
        }
        else {
            System.out.println("Invalid reference or activity");
            return 0.0;
        }
    }

    public void viewLog() {
        if (log.isEmpty()) {
            System.out.println("No activities logged");
        }
        else {
            System.out.println("Activity log for " + date + ":");
            for (long activityId: log.keySet()) {
                System.out.println("Activity ID: " + activityId + " Emission: " + log.get(activityId) * tonConvert + " tons CO2e");
            }
        }
    }

    public double getDailyEmission() {
        return dayEmission * tonConvert;
    }

    public String getDate() {
        return date.toString();
    }
}
