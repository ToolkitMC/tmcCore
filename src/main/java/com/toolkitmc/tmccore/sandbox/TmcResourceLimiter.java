package com.toolkitmc.tmccore.sandbox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcResourceLimiter {

    private static final Map<UUID, Integer> actionCount = new HashMap<>();

    public static boolean allowAction(UUID uuid, int maxActions) {
        int count = actionCount.getOrDefault(uuid, 0);
        if (count >= maxActions) {
            return false;
        }
        actionCount.put(uuid, count + 1);
        return true;
    }

    public static void reset(UUID uuid) {
        actionCount.remove(uuid);
    }
}