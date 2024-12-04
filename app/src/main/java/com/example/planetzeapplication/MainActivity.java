package com.example.planetzeapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView totalEmissionsTextView;
    private RecyclerView emissionsByCategoryRecyclerView;
    private RecyclerView emissionsTrendRecyclerView;
    private CategoryAdapter categoryAdapter;
    private TrendAdapter trendAdapter;
    private User currentUser;
    private TextView comparisonTextView;
    private EcoGauge ecoGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);


        totalEmissionsTextView = findViewById(R.id.total_emissions);
        emissionsByCategoryRecyclerView = findViewById(R.id.category_recycler_view);
        emissionsTrendRecyclerView = findViewById(R.id.trend_recycler_view);
        comparisonTextView = findViewById(R.id.comparison_results);


        emissionsByCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        emissionsByCategoryRecyclerView.setAdapter(categoryAdapter);
        ecoGauge = new EcoGauge();

        emissionsTrendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendAdapter = new TrendAdapter(new ArrayList<>());
        emissionsTrendRecyclerView.setAdapter(trendAdapter);


        fetchUserData();
    }

    private void fetchUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/KvFyQ74zUrMGqVanWn8wA00MHcl1");
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                currentUser = task.getResult().getValue(User.class);
                if (currentUser != null) {
                    setupUI();
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupComparisonSection() {
        Map<String, String> comparisonResults = ecoGauge.compareToUserEmission(currentUser);
        StringBuilder resultsBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : comparisonResults.entrySet()) {
            resultsBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        comparisonTextView.setText(resultsBuilder.toString());
    }

    private void setupUI() {
        if (currentUser == null) return;

        // Update Total Emissions
        totalEmissionsTextView.setText(currentUser.totemission + " kg CO2e");

        // Fetch Emissions by Category
        Map<String, Long> emissionsByCategory = currentUser.getEmissionsByCategory();
        categoryAdapter.updateData(new ArrayList<>(emissionsByCategory.entrySet()));

        // Fetch Emissions Trend
        Map<Long, Long> emissionsTrend = currentUser.getEmissionsOverTime(0, System.currentTimeMillis());
        trendAdapter.updateData(new ArrayList<>(emissionsTrend.entrySet()));
    }
}
