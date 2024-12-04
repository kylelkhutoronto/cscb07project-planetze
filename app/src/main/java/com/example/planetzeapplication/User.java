package com.example.planetzeapplication;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class User {
    public String fullName;
    public String email;
    public long userid;
    public HashMap<String, DayTracker> days; // String key for date (e.g., "2024-12-03")
    public double totemission;
    private Boolean donesurvey;


    public User() {
        this.days = new HashMap<>();
        this.totemission = 0;
    }


    public User(String fullName, String email, double totemission) {
        this.fullName = fullName;
        this.email = email;
        this.totemission = totemission;
        this.days = new HashMap<>();
        this.doneSurvey = false;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
    public Boolean getDoneSurvey() {
        return doneSurvey;
    }
    

    // Method to Calculate Total Emissions Over Time
    public Map<Long, Long> getEmissionsOverTime(long start_date, long end_date) {
        Map<Long, Long> emissionsOverTime = new TreeMap<>();
        if (days == null || days.isEmpty()) {
            return emissionsOverTime;
        }

        for (Map.Entry<String, DayTracker> entry : days.entrySet()) {
            try {

                long date = Long.parseLong(entry.getKey().replace("-", ""));
                if (date >= start_date && date <= end_date) {
                    emissionsOverTime.put(date, entry.getValue().totEmission);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return emissionsOverTime;
    }

    // Method to Calculate Emissions by Category
    public Map<String, Long> getEmissionsByCategory() {
        Map<String, Long> emissionsByCategory = new HashMap<>();
        if (days == null || days.isEmpty()) {
            return emissionsByCategory;
        }

        for (DayTracker tracker : days.values()) {
            for (Map.Entry<String, Long> entry : tracker.log.entrySet()) {
                String category = entry.getKey();
                long emission = entry.getValue();

                emissionsByCategory.put(
                        category,
                        emissionsByCategory.getOrDefault(category, 0L) + emission
                );
            }
        }
        return emissionsByCategory;
    }

}
