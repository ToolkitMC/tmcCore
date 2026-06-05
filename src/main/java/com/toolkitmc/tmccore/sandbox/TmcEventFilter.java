package com.toolkitmc.tmccore.sandbox;

import java.util.HashSet;
import java.util.Set;

public class TmcEventFilter {

    private static final Set<String> blockedEvents = new HashSet<>();

    public static void blockEvent(String eventName) {
        blockedEvents.add(eventName);
    }

    public static void allowEvent(String eventName) {
        blockedEvents.remove(eventName);
    }

    public static boolean isAllowed(String eventName) {
        return !blockedEvents.contains(eventName);
    }
}