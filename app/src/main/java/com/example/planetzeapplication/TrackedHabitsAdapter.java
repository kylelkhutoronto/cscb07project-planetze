package com.example.planetzeapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.*;

public class TrackedHabitsAdapter extends RecyclerView.Adapter<TrackedHabitsAdapter.TrackedHabitViewHolder> {

    private HashMap<String, Long> habits;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activities");


    public TrackedHabitsAdapter(HashMap<String, Long> habits) {
        this.habits = habits;
    }



    @NonNull
    @Override
    public TrackedHabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracked_habit_item, parent, false);
        return new TrackedHabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackedHabitViewHolder holder, int position) {
        String habitName = (String) habits.keySet().toArray()[position];
        Long trackedDays = habits.get(habitName);

        holder.habitName.setText(habitName);
        holder.trackingDays.setText(trackedDays + "@string days");
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public static class TrackedHabitViewHolder extends RecyclerView.ViewHolder {
        TextView habitName;
        TextView trackingDays;

        public TrackedHabitViewHolder(View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name);
            trackingDays = itemView.findViewById(R.id.tracking_days);
        }
    }
}
