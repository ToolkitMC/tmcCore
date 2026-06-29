package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcMuteManager {

    private static final Set<UUID> mutedPlayers = new HashSet<>();

    public static void mute(ServerPlayerEntity player, String reason) {
        mutedPlayers.add(player.getUuid());
        player.sendMessage(Text.literal("§cYou have been muted. Reason: " + reason), false);
        TmcAuditLogger.log(player, "Player muted: " + reason);
    }

    public static void unmute(ServerPlayerEntity player) {
        mutedPlayers.remove(player.getUuid());
        player.sendMessage(Text.literal("§aYou have been unmuted."), false);
    }

    public static boolean isMuted(ServerPlayerEntity player) {
        return mutedPlayers.contains(player.getUuid());
    }
}