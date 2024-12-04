package com.example.planetzeapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setup() {
        user = new User("Test User", "test@example.com", 0);
        user.days = new HashMap<>();

        DayTracker day1 = new DayTracker("2024-12-03", new HashMap<>());
        day1.log.put("Transportation", 50L);
        day1.log.put("Food", 20L);

        DayTracker day2 = new DayTracker("2024-12-04", new HashMap<>());
        day2.log.put("Energy Use", 30L);
        day2.log.put("Shopping", 10L);

        user.days.put("2024-12-03", day1);
        user.days.put("2024-12-04", day2);
    }

    @Test
    void testGetEmissionsOverTime() {
        Map<Long, Long> emissionsOverTime = user.getEmissionsOverTime(20241203L, 20241204L);

        assertEquals(2, emissionsOverTime.size());
        assertEquals(70L, emissionsOverTime.get(20241203L));
        assertEquals(40L, emissionsOverTime.get(20241204L));
    }
}
