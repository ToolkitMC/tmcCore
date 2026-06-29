package com.toolkitmc.tmccore.sandbox;

import com.toolkitmc.tmccore.TmcCore;
import net.minecraft.server.network.ServerPlayerEntity;

public class TmcAuditLogger {

    public static void log(ServerPlayerEntity player, String action) {
        TmcCore.LOGGER.info("[AUDIT] {} performed: {}", player.getName().getString(), action);
    }

    public static void log(String source, String action) {
        TmcCore.LOGGER.info("[AUDIT] {} performed: {}", source, action);
    }

    public static void warn(ServerPlayerEntity player, String action) {
        TmcCore.LOGGER.warn("[AUDIT] {} attempted: {}", player.getName().getString(), action);
    }
}