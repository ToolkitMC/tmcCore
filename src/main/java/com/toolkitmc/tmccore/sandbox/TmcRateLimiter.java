package com.toolkitmc.tmccore.sandbox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcRateLimiter {

    private static final Map<UUID, Map<String, Long>> limits = new HashMap<>();

    public static boolean allow(UUID uuid, String action, int maxPerMinute) {
        long now = System.currentTimeMillis();
        var playerLimits = limits.computeIfAbsent(uuid, k -> new HashMap<>());
        long last = playerLimits.getOrDefault(action, 0L);

        if (now - last < 60000L / maxPerMinute) {
            return false;
        }
        playerLimits.put(action, now);
        return true;
    }

    public static void clear(UUID uuid) {
        limits.remove(uuid);
    }
}