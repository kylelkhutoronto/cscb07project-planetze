package com.example.planetzeapplication;

import java.util.HashMap;

public class DayTracker {
    public String date;
    public HashMap<String, Long> log;
    public long totEmission;


    public DayTracker() {
        this.log = new HashMap<>();
        this.totEmission = 0;
    }


    public DayTracker(String date, HashMap<String, Long> log) {
        this.date = date;
        this.log = log;
        this.totEmission = calculateTotalEmissions();
    }


    public long calculateTotalEmissions() {
        long total = 0;
        for (long emission : log.values()) {
            total += emission;
        }
        return total;
    }
}
