package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcPlayerIsolation {

    private static final Set<UUID> isolatedPlayers = new HashSet<>();

    public static void isolate(ServerPlayerEntity player) {
        isolatedPlayers.add(player.getUuid());
        TmcAuditLogger.log(player, "Player isolated");
    }

    public static void release(ServerPlayerEntity player) {
        isolatedPlayers.remove(player.getUuid());
        TmcAuditLogger.log(player, "Player released from isolation");
    }

    public static boolean isIsolated(ServerPlayerEntity player) {
        return isolatedPlayers.contains(player.getUuid());
    }
}