package com.example.planetzeapplication;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SurveyResultsActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseDatabase db;
    private String location, q2, q3, q4, q5, q6, q7, q8, q9Beef, q9Pork, q9Chicken, q9Fish,
            q10, q11, q12, q13, q14, q15, q16, q17, q18, q19, q20, q21;
    private double locationAverage, compareUserLocation, compareUserGlobal;
    private int globalTarget;
    private double totalEmissions, transportEmissions, foodEmissions, housingEmissions,
            consumptionEmissions;
    TextView totalText, comparisonTitle, comparisonText;
    AnyChartView pieChart;
    String aboveBelowLocation, aboveBelowGlobal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);

        Log.d("FindingView", "Let's start finding the view ids...");

        totalText = findViewById(R.id.total_emissions);
        pieChart = findViewById(R.id.pieChart);
        comparisonTitle = findViewById(R.id.comparison);
        comparisonText = findViewById(R.id.comparisonText);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("SurveyResultsActivity2", "User not authenticated");
            return;
        }

        String uid = currentUser.getUid();

        db = FirebaseDatabase
                .getInstance();

        DatabaseReference ref1 = db.getReference("Users")
                .child(uid).child("questionnaire").child("answers");

        ref1.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    location = snapshot.child("location").getValue(String.class);
                    q2 = snapshot.child("q2").getValue(String.class);
                    q3 = snapshot.child("q3").getValue(String.class);
                    q4 = snapshot.child("q4").getValue(String.class);
                    q5 = snapshot.child("q5").getValue(String.class);
                    q6 = snapshot.child("q6").getValue(String.class);
                    q7 = snapshot.child("q7").getValue(String.class);
                    q8 = snapshot.child("q8").getValue(String.class);
                    q9Beef = snapshot.child("q9Beef").getValue(String.class);
                    q9Pork = snapshot.child("q9Pork").getValue(String.class);
                    q9Chicken = snapshot.child("q9Chicken").getValue(String.class);
                    q9Fish = snapshot.child("q9Fish").getValue(String.class);
                    q10 = snapshot.child("q10").getValue(String.class);
                    q11 = snapshot.child("q11").getValue(String.class);
                    q12 = snapshot.child("q12").getValue(String.class);
                    q13 = snapshot.child("q13").getValue(String.class);
                    q14 = snapshot.child("q14").getValue(String.class);
                    q15 = snapshot.child("q15").getValue(String.class);
                    q16 = snapshot.child("q16").getValue(String.class);
                    q17 = snapshot.child("q17").getValue(String.class);
                    q18 = snapshot.child("q18").getValue(String.class);
                    q19 = snapshot.child("q19").getValue(String.class);
                    q20 = snapshot.child("q20").getValue(String.class);
                    q21 = snapshot.child("q21").getValue(String.class);

                    calculateAndSaveEmissions();

                    Log.e("Setting", "Let's start setting the data...");

                    setData();
                } else {
                    Log.w("SurveyResultsActivity2", "survey answers have no data");
                }
            } else {
                Log.e("SurveyResultsActivity2", "Error reading survey answers", task.getException());
            }
        });
    }

    private void calculateAndSaveEmissions() {

        transportEmissions = TransportationCalculation.transportEmissionsKG(q2, q3, q4, q5, q6, q7) / 1000.0;
        foodEmissions = FoodCalculation.foodEmissionsKG(q8, q9Beef, q9Pork, q9Chicken, q9Fish, q10) / 1000.0;
        housingEmissions = HousingCalculation.housingEmissionsKG(this, q11, q13, q15, q12, q14, q16, q17) / 1000.0;
        consumptionEmissions = ConsumptionCalculation.consumptionEmissionsKG(q18, q19, q20, q21) / 1000.0;
        totalEmissions = transportEmissions + foodEmissions + housingEmissions + consumptionEmissions;

        try {
            InputStream fis = getResources().openRawResource(R.raw.globalaverages);
            BufferedReader b = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = b.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    if (location.equals(parts[0])) {
                        locationAverage = Double.parseDouble(parts[1].trim());
                        Log.i("location average",
                                "Location found in file and average saved: "
                                        + parts[0].trim() + ", " + parts[1].trim());
                        break;
                    }
                }
            }
            b.close();
            fis.close();
        } catch (Exception ex) {
            Log.d("location average", "Error reading file", ex);
        }

        globalTarget = 2;

        compareUserLocation = ((totalEmissions - locationAverage) / locationAverage) * 100;

        compareUserGlobal = ((totalEmissions - globalTarget) / globalTarget) * 100;

        aboveBelowLocation = compareUserLocation > 0 ? "above" : "below";

        aboveBelowGlobal = compareUserGlobal > 0 ? "above" : "below";

        Log.d("Calculations", "Calculations are done...");

        saveAnswerToFirebase("totalEmissions", totalEmissions);
        saveAnswerToFirebase("transportationEmissions", transportEmissions);
        saveAnswerToFirebase("foodEmissions", foodEmissions);
        saveAnswerToFirebase("housingEmissions", housingEmissions);
        saveAnswerToFirebase("consumptionEmissions", consumptionEmissions);
        saveAnswerToFirebase("locationAverage", locationAverage);
        saveAnswerToFirebase("globalTargetEmission", globalTarget);

        Log.d("Calculations", "Results have been uploaded to Firebase Database");

    }

    private void saveAnswerToFirebase(String emissionCategory, double result) {

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = db.getReference("Users").child(uid)
                    .child("questionnaire")
                    .child("results");
            userRef.child(emissionCategory).setValue(result);
        } else {
            Log.e("SurveyResultsActivity2", "User is not authenticated.");
        }
    }

    private void setData() {

        Log.d("chart", "Total Emissions:" + totalEmissions);
        totalText.setText(getString(R.string.total_emissions,totalEmissions));

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                totalText,
                3, // Min text size
                100, // Max text size
                2, // Step granularity
                TypedValue.COMPLEX_UNIT_SP // Unit
        );

        Pie pie = AnyChart.pie();

        List<DataEntry> dataEntries = new ArrayList<>();

        dataEntries.add(new ValueDataEntry(getString(R.string.cat1), transportEmissions));
        dataEntries.add(new ValueDataEntry(getString(R.string.cat2), foodEmissions));
        dataEntries.add(new ValueDataEntry(getString(R.string.cat3), housingEmissions));
        dataEntries.add(new ValueDataEntry(getString(R.string.cat4), consumptionEmissions));

        pie.data(dataEntries);

        pie.palette(new String[] {"#F2BDCD", "#6DB474", "#024265", "#C75B4B"});

        pie.title("Category Breakdown");
        pie.title().fontSize(20).fontColor("#373f51")
                        .fontFamily("sans-serif-black");

        pie.labels().fontColor("#FFFFFFFF")
                .fontFamily("sans-serif-black");

        pie.animation(true);
        pie.legend().enabled(true).fontColor("#373f51")
                .position("bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .fontSize(9.8)
                .padding(3, 0, 3, 1).fontFamily("sans-serif").iconSize(10);

        pieChart.setChart(pie);

        comparisonTitle.setText(getString(R.string.comparison_title,location));

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                comparisonTitle,
                3, // Min text size
                100, // Max text size
                2, // Step granularity
                TypedValue.COMPLEX_UNIT_SP // Unit
        );

        comparisonText.setText(getString(R.string.comparison_text, totalEmissions, locationAverage,
                compareUserLocation, aboveBelowLocation, location, compareUserGlobal,
                aboveBelowGlobal, globalTarget));

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                comparisonText,
                3, // Min text size
                100, // Max text size
                2, // Step granularity
                TypedValue.COMPLEX_UNIT_SP // Unit
        );

    }

}

