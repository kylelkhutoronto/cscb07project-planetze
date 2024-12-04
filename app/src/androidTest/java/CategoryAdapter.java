package com.example.planetzeapplication;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAdapterTest {

    @Test
    void testUpdateData() {
        List<Map.Entry<String, Long>> data = new ArrayList<>();
        data.add(Map.entry("Transportation", 50L));
        data.add(Map.entry("Food", 20L));

        CategoryAdapter adapter = new CategoryAdapter(data);
        assertEquals(2, adapter.getItemCount());
    }
}
