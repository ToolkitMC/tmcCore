package com.toolkitmc.tmccore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcCooldown {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(UUID uuid, String key, int seconds) {
        long now = System.currentTimeMillis();
        var playerCooldowns = cooldowns.computeIfAbsent(uuid, k -> new HashMap<>());
        if (playerCooldowns.containsKey(key)) {
            long last = playerCooldowns.get(key);
            if (now - last < seconds * 1000L) {
                return true;
            }
        }
        playerCooldowns.put(key, now);
        return false;
    }

    public static long getRemaining(UUID uuid, String key, int seconds) {
        var playerCooldowns = cooldowns.get(uuid);
        if (playerCooldowns == null || !playerCooldowns.containsKey(key)) return 0;
        long remaining = (playerCooldowns.get(key) + seconds * 1000L) - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public static void clear(UUID uuid) {
        cooldowns.remove(uuid);
    }
}