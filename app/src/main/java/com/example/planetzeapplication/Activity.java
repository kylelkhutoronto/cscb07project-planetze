package com.example.planetzeapplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

import android.util.*;
public class Activity {
    /**  four fields for each activity
     * activityId: specific id of activity
     * activityName: specific name of activity
     * category: activity category
     * emission: emission (tons of CO2)
    */

    //
    long activityId;
    String activityName;
    String category;
    // standard emission
    double baseEmission;
    long habitTracker;

    boolean tracking;

    //constructors
    public Activity(){
        this.activityId = 0;
        this.activityName = "";
        this.baseEmission = 0;
        this.category = "";
        this.habitTracker = 0;
        this.tracking = false;
    }

    public Activity(long activityId, String activityName, String category, long emission){
        this.activityId = activityId;
        this.activityName = activityName;
        this.baseEmission = emission;
        this.category = category;
        this.habitTracker = 0;
        this.tracking = false;
    }

    public long getActivityId() { return activityId; }
    public void setActivityId(long activityId) { this.activityId = activityId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getCategory() { return category; }
    public String getCategory(Activity x, ArrayList<Activity> activityList ) {

        return x.category;}

    public void setCategory(String category) { this.category = category; }
    public double getEmission() { return baseEmission; }

    public double getEmission(Activity x, ArrayList<Activity> activityList ) {

        return x.baseEmission;}
    public void setEmission(long emission) { this.baseEmission = emission; }
    public long getHabitTracker() { return habitTracker; }
    public void setHabitTracker(long habitTracker) { this.habitTracker = habitTracker; }
    public boolean isTracking() { return tracking; }
    public void setTracking(boolean tracking) { this.tracking = tracking; }

    // get all activities done within 14 days
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activities");






    public Activity idToActivity (long activityId,ArrayList<Activity> activities) {
        Activity activity = null;
        for(Activity x: activities) {
            if(x.activityId == activityId)
                 activity = x;
        }
        return activity;
    }

    public Activity nameToActivity (String activityName,ArrayList<Activity> activities) {
        Activity activity = null;
        for(Activity x: activities) {
            if(x.activityName.equals(activityName))
                activity = x;
        }
        return activity;
    }


    public HashMap<String,Long> recentActivities(ArrayList<DayTracker> dayTrackerList, ArrayList<Activity> activities){
        /* return a dictionary of activities done by user
         *  along with the number of times it was seen in the
         *  period tracked (14 days). */

        //sort the list using comparator based on date
        dayTrackerList.sort(Comparator.comparing(tracker -> tracker.date));
        //create a dictionary to store the activities along with num occurences.
        HashMap <String,Long> recActivities = new HashMap<>();
        //iterate over each activity log for each day to add activities to dict.
        for(int x = 0; x < 14; x++){
            DayTracker day = new DayTracker();
            day = dayTrackerList.get(x);
            for(long index : day.log.keySet()){
                String activityName = idToActivity(index,activities).activityName;
                if(recActivities.containsKey(activityName))
                recActivities.put(activityName, recActivities.get(activityName));
                else recActivities.put(activityName, (long)0);
            }
        }

        // return activities
        return recActivities;
    }



    // get all activities in a category
    public ArrayList<Activity> getActivitiesOfCat(String category, ArrayList<Activity> activityList){
        ArrayList<Activity> catActivities = new ArrayList<>();
        for(Activity x : activityList){
            if(x.category.equals(category))
                catActivities.add(x);
        }
        catActivities.sort(Comparator.comparing(activity->baseEmission));
        return catActivities;
    }

    //get activity one tier below (by emission) the input activity in same category
    public String betterActivity(String activityName, String category, ArrayList<Activity> activityList){
        ArrayList<Activity> catActivities = getActivitiesOfCat(category, activityList);
        for(int x = 0; x < catActivities.size(); x++){
            if(catActivities.get(x).activityName.equals(activityName) && x != 0){
                return catActivities.get(x-1).activityName;
            }
        }
        return "";
    }

    public double lowestEmissionInCat( String category, ArrayList<Activity> activityList){
        ArrayList<Activity> catActivities = getActivitiesOfCat(category, activityList);
        catActivities.sort(Comparator.comparing(activity->baseEmission));
        return catActivities.get(0).baseEmission;
    }

    public void suggestHabits(ArrayList<DayTracker> dayTrackerList, ArrayList<Activity> activityList, HashMap<String,String> suggestions){
        /* Returns a dictionary where each key is an activityId of a
        recent activity done by the user for more than
        7 of the 14 days and each value is a suggested habits
        activityId.
         */
        HashMap<String,Long> recentActivities = new HashMap<>();
        recentActivities = recentActivities( dayTrackerList,activityList);
        for(String x : recentActivities.keySet()){

            double lowestEmission = lowestEmissionInCat(nameToActivity(x,activityList).category,activityList);
            long val = 0;
            if(recentActivities.get(x) != null) val = recentActivities.get(x);
            if(val >= 8 && nameToActivity(x,activityList).getEmission() >= (1.5*lowestEmission)){
                String betterHabit = betterActivity(x, category, activityList);
                suggestions.put(x,betterHabit);
            }
        }
        return;
    }

    public void updateHabit(long activityId) {
        /*Checks db if 'tracking' is true. If so, increment 'habitTracker'.
        * Otherwise, 'tracking' is set to true.*/
        DatabaseReference activityReference = FirebaseDatabase.getInstance()
                .getReference("Activities")
                .child(String.valueOf(activityId));

        // Fetch the current data of the activity
        activityReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current Activity object
                    Activity activity = dataSnapshot.getValue(Activity.class);

                    if (activity != null) {
                        if (activity.isTracking()) {
                            // If tracking is true, increment the habitTracker
                            long currentHabitTracker = activity.getHabitTracker();
                            activity.setHabitTracker(currentHabitTracker + 1);

                            // Update the habitTracker value in the database
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("habitTracker", activity.getHabitTracker());

                            activityReference.updateChildren(updates)
                                    .addOnSuccessListener(aVoid ->
                                            Log.d("Firebase", "Habit Tracker incremented successfully"))
                                    .addOnFailureListener(e ->
                                            Log.e("Firebase", "Failed to increment habit tracker", e));
                        } else {
                            // If tracking is false, change tracking to true
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("tracking", true);

                            activityReference.updateChildren(updates)
                                    .addOnSuccessListener(aVoid ->
                                            Log.d("Firebase", "Tracking set to true successfully"))
                                    .addOnFailureListener(e ->
                                            Log.e("Firebase", "Failed to set tracking", e));
                        }
                    }
                } else {
                    Log.d("Firebase", "Activity not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error while fetching activity", databaseError.toException());
            }
        });
    }


    public HashMap<String,Long> habits(ArrayList<Activity> habitActivities){
        HashMap<String,Long> finHabits = new HashMap<>();
        for(Activity habit: habitActivities){
            finHabits.put(habit.activityName, habit.habitTracker);
        }
        return finHabits;
    }

    public HashMap<String, Long>  fetchActivitiesFromFirebase( HashMap<String, Long> tracked) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayList<Activity> activities = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Activity activity = childSnapshot.getValue(Activity.class);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }

                    ArrayList<Activity> habitActivities = new ArrayList<>();
                    for(Activity activity: activities){
                        if(activity.tracking){
                            habitActivities.add(activity);
                        }
                    }

                    Activity activity = new Activity();
                    HashMap<String, Long> tracked = new HashMap<>();
                    tracked = activity.habits(habitActivities);



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
        return tracked;
    }





}
