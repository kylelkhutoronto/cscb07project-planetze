package com.example.planetzeapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DayTrackerTest {
    private DayTracker dayTracker;

    @BeforeEach
    void setup() {
        HashMap<String, Long> log = new HashMap<>();
        log.put("Transportation", 30L);
        log.put("Food", 20L);
        dayTracker = new DayTracker("2024-12-03", log);
    }

    @Test
    void testCalculateTotalEmissions() {
        assertEquals(50L, dayTracker.calculateTotalEmissions());
    }
}
