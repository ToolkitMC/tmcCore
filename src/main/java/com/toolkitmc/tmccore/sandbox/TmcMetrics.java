package com.toolkitmc.tmccore.sandbox;

import java.util.HashMap;
import java.util.Map;

public class TmcMetrics {

    private static final Map<String, Integer> counters = new HashMap<>();

    public static void increment(String key) {
        counters.put(key, counters.getOrDefault(key, 0) + 1);
    }

    public static int get(String key) {
        return counters.getOrDefault(key, 0);
    }

    public static void reset(String key) {
        counters.remove(key);
    }

    public static void resetAll() {
        counters.clear();
    }
}