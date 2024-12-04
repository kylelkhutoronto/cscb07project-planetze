package com.example.planetzeapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;


import java.util.HashMap;
import java.util.Map;

public class EcoGauge {
    private Map<String, Double> globalAverages;

    public EcoGauge() {
        this.globalAverages = new HashMap<>();
        fetchGlobalAveragesFromFirebase();
    }

    public static Map<String, Long> getEmissionsByCategory(Map<String, Long> activityLogs) {
        Map<String, Long> emissionsByCategory = new HashMap<>();
        emissionsByCategory.put("Transportation", activityLogs.getOrDefault("Transportation", 0L));
        emissionsByCategory.put("Energy Use", activityLogs.getOrDefault("Energy Use", 0L));
        emissionsByCategory.put("Food Consumption", activityLogs.getOrDefault("Food Consumption", 0L));
        emissionsByCategory.put("Shopping/Consumption", activityLogs.getOrDefault("Shopping/Consumption", 0L));
        return emissionsByCategory;
    }

    private void fetchGlobalAveragesFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GlobalAverages");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String country = child.getKey();
                    Double emission = child.getValue(Double.class);
                    globalAverages.put(country, emission);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error fetching global averages: " + error.getMessage());
            }
        });
    }
    public Map<String, String> compareToUserEmission(User currentUser) {
        Map<String, String> comparisonResults = new HashMap<>();
        double userEmission = currentUser.totemission;

        for (Map.Entry<String, Double> entry : globalAverages.entrySet()) {
            String region = entry.getKey();
            double averageEmission = entry.getValue();
            String result = userEmission > averageEmission
                    ? "Above Average"
                    : userEmission == averageEmission
                    ? "Equal to Average"
                    : "Below Average";
            comparisonResults.put(region, result);
        }
        return comparisonResults;
    }
}
