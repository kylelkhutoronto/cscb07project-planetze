package com.example.planetzeapplication;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.*;

public class DayTracker {

    HashMap<Long,Long> log;
    double dayEmission;
    LocalDate date;


    public DayTracker(){
        this.log = new HashMap<>();
        this.dayEmission = 0;
        this.date = null;
    }

    public DayTracker(HashMap<Long,Long> log, double dayemission, LocalDate date){
        this.log = log;
        this.dayEmission = dayemission;
        this.date = date;
    }

    public void newDay(User userid ,LocalDate date){
        DayTracker temp = new DayTracker(null,0,LocalDate.now());
        userid.days.add(0,temp);
        return;
    }

    public DayTracker findDay(User userid, LocalDate date){
        ArrayList<DayTracker> days = userid.days;
        for(DayTracker day : days){
            if(day.date.equals(date)) return day;
        }
        DayTracker nothing = new DayTracker(null,0,null);
        return nothing;
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activities");

    public void fetchActivitiesFromFirebaseDay(ArrayList<Activity> activities) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Activity activity = childSnapshot.getValue(Activity.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }


                    Log.d("Firebase", "Total activities retrieved: " + activities.size());
                } else {
                    Log.d("Firebase", "No data found at 'Activities'");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });

    }

    public void fetchDaysFromFirebase(String userId, HashMap<String, String> suggest) {
        DatabaseReference daysReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("days");

        ArrayList<DayTracker> dayTrackerList = new ArrayList<>();
        daysReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                        String dateString = daySnapshot.child("date").getValue(String.class);
                        double dayEmission = daySnapshot.child("totEmission").getValue(Double.class);
                        HashMap<Long, Long> log = new HashMap<>();

                        DataSnapshot logSnapshot = daySnapshot.child("log");
                        if (logSnapshot.exists()) {
                            for (DataSnapshot logEntry : logSnapshot.getChildren()) {
                                Long key = Long.valueOf(logEntry.getKey());
                                Long value = logEntry.getValue(Long.class);
                                log.put(key, value);
                            }
                        }
                        Activity activity = new Activity();
                        DayTracker day = new DayTracker();
                        ArrayList<Activity> finalVar = new ArrayList<>();
                        day.fetchActivitiesFromFirebaseDay(finalVar);
                         activity.suggestHabits(dayTrackerList, finalVar,suggest);
                    }

                    Log.d("Firebase", "Total days retrieved: " + dayTrackerList.size());
                } else {
                    Log.d("Firebase", "No days found for user " + userId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read days data", databaseError.toException());
            }
        });
    }


}

