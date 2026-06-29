package com.toolkitmc.tmccore.sandbox;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcBlacklist {

    private static final Set<UUID> blacklisted = new HashSet<>();

    public static void add(UUID uuid) {
        blacklisted.add(uuid);
    }

    public static void remove(UUID uuid) {
        blacklisted.remove(uuid);
    }

    public static boolean isBlacklisted(UUID uuid) {
        return blacklisted.contains(uuid);
    }
}