package com.example.planetzeapplication;


import java.util.*;
import com.google.firebase.database.FirebaseDatabase;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class HabitsActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private String uid;

    private RecyclerView suggestionsRecyclerView;
    private RecyclerView trackedHabitsRecyclerView;
    private SuggestionsAdapter suggestionsAdapter;
    private TrackedHabitsAdapter trackedHabitsAdapter;
    private HashMap<String,String> habitSuggestions = new HashMap<>();
    private HashMap<String, Long> trackedHabits = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Log.e("SurveyActivity", "Error: User not authenticated.");
            finish();
            return;
        }

        uid = currentUser.getUid();


        setContentView(R.layout.activity_habits);
        Activity activity = new Activity();
        DayTracker day = new DayTracker();

        activity.fetchActivitiesFromFirebase(trackedHabits);
        day.fetchDaysFromFirebase(uid, habitSuggestions);




        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        trackedHabitsRecyclerView = findViewById(R.id.trackedHabitsRecyclerView);

        // Setup RecyclerView for suggestions
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        suggestionsAdapter = new SuggestionsAdapter(habitSuggestions);
        suggestionsRecyclerView.setAdapter(suggestionsAdapter);

        // Setup RecyclerView for tracked habits
        trackedHabitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackedHabitsAdapter = new TrackedHabitsAdapter(trackedHabits);
        trackedHabitsRecyclerView.setAdapter(trackedHabitsAdapter);

    }

}

