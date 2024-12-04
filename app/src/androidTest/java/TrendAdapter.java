package com.example.planetzeapplication;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrendAdapterTest {

    @Test
    void testUpdateData() {
        List<Map.Entry<Long, Long>> data = new ArrayList<>();
        data.add(Map.entry(20241203L, 50L));
        data.add(Map.entry(20241204L, 70L));

        TrendAdapter adapter = new TrendAdapter(data);
        assertEquals(2, adapter.getItemCount());
    }
}
