package com.example.planetzeapplication;

import java.util.HashMap;

public class DayTracker {
    Localdate date;
    HashMap<Long, Long> log;
    double totEmission;


    public DayTracker() {
        this.log = new HashMap<>();
        this.totEmission = 0;
    }


    public DayTracker(String date, HashMap<Long, Long> log) {
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
