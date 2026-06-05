package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcAntiAbuse {

    private static final Map<UUID, Integer> violationCount = new HashMap<>();

    public static void reportViolation(ServerPlayerEntity player, String reason) {
        int count = violationCount.getOrDefault(player.getUuid(), 0) + 1;
        violationCount.put(player.getUuid(), count);
        TmcAuditLogger.warn(player, "Violation #" + count + ": " + reason);

        if (count >= 5) {
            TmcPlayerIsolation.isolate(player);
            TmcAuditLogger.log(player, "Auto-isolated due to abuse");
        }
    }

    public static void clearViolations(UUID uuid) {
        violationCount.remove(uuid);
    }
}