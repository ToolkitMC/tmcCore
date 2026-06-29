package com.toolkitmc.tmccore.sandbox;

import net.fabricmc.loader.api.FabricLoader;

public class TmcVersionChecker {

    public static String getModVersion(String modId) {
        return FabricLoader.getInstance()
                .getModContainer(modId)
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isVersionAtLeast(String modId, String minVersion) {
        String current = getModVersion(modId);
        return current.compareTo(minVersion) >= 0;
    }
}