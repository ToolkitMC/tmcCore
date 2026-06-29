package com.toolkitmc.tmccore.sandbox;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcWhitelist {

    private static final Set<UUID> whitelisted = new HashSet<>();

    public static void add(UUID uuid) {
        whitelisted.add(uuid);
    }

    public static void remove(UUID uuid) {
        whitelisted.remove(uuid);
    }

    public static boolean isWhitelisted(UUID uuid) {
        return whitelisted.contains(uuid);
    }
}