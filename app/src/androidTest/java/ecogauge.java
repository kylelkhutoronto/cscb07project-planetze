package com.example.planetzeapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EcoGaugeTest {
    private EcoGauge ecoGauge;

    @BeforeEach
    void setup() {
        ecoGauge = new EcoGauge();
    }

    @Test
    void testGetEmissionsByCategory() {
        Map<String, Long> activityLogs = new HashMap<>();
        activityLogs.put("Transportation", 30L);
        activityLogs.put("Food Consumption", 50L);

        Map<String, Long> result = EcoGauge.getEmissionsByCategory(activityLogs);
        assertEquals(30L, result.get("Transportation"));
        assertEquals(50L, result.get("Food Consumption"));
        assertEquals(0L, result.get("Energy Use"));
    }

    @Test
    void testCompareToUserEmission() {
        User user = new User("Test User", "test@example.com", 10.0);
        ecoGauge.globalAverages.put("Global", 4.5);
        ecoGauge.globalAverages.put("India", 1.5);

        Map<String, String> comparison = ecoGauge.compareToUserEmission(user);

        assertEquals("Above Average", comparison.get("Global"));
        assertEquals("Above Average", comparison.get("India"));
    }
}
