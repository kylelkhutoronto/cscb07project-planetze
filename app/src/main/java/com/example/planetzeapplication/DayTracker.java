package com.example.planetzeapplication;

import java.util.*;
import java.time.LocalDate;
import com.google.firebase.database.*;

public class DayTracker {
    HashMap<Long, Double> log;
    double dayEmission;
    String date;
    static double tonConvert = 0.001102;

    public DayTracker() {
        this.log = new HashMap<>();
        this.dayEmission = 0.0;
        this.date = LocalDate.now().toString();
    }

    public DayTracker(HashMap<Long, Double> log, double totalEmission, String date) {
        this.log = log;
        this.dayEmission = totalEmission;
        this.date = date;
    }

    public void addActivity(String activityName, String category, String subtype, double magnitude) {
        Activity activity = null;
        try {
            activity = ActivityCreator.newActivity(activityName, category, subtype, magnitude);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (activity != null) {
            if (log.containsKey(activity.getActivityId())) {
                double currEmission = log.get(activity.getActivityId());
                double emission = computeEmission(activity);
                log.put(activity.getActivityId(), currEmission + emission);
                dayEmission += emission;
            }
            else {
                double emission = computeEmission(activity);
                log.put(activity.getActivityId(), emission);
                dayEmission += emission;
            }
        }
        else {
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
        return date;
    }

    public HashMap<Long, Double> getLog() {
        return log;
    }

    public void saveDayTrackerData(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dayRef = database.getReference("users").child(userId).child("days").child(date);

        dayRef.child("log").setValue(log);
        dayRef.child("dayEmission").setValue(dayEmission);
        dayRef.child("date").setValue(date);
    }
}
