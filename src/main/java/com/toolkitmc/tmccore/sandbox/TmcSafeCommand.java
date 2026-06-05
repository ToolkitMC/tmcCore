package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcSafeCommand {

    public static boolean executeSafely(ServerCommandSource source, String command) {
        if (source.getPlayer() != null) {
            ServerPlayerEntity player = source.getPlayer();
            if (!TmcRateLimiter.allow(player.getUuid(), "command", 30)) {
                player.sendMessage(Text.literal("§cYou are sending commands too fast!"), false);
                return false;
            }
            TmcAuditLogger.log(player, "Executed command: " + command);
        }
        return true;
    }
}