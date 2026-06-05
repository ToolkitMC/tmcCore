package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcBanManager {

    private static final Set<UUID> bannedPlayers = new HashSet<>();

    public static void ban(ServerPlayerEntity player, String reason) {
        bannedPlayers.add(player.getUuid());
        player.networkHandler.disconnect(Text.literal("Banned: " + reason));
        TmcAuditLogger.log(player, "Player banned: " + reason);
    }

    public static void unban(UUID uuid) {
        bannedPlayers.remove(uuid);
    }

    public static boolean isBanned(ServerPlayerEntity player) {
        return bannedPlayers.contains(player.getUuid());
    }
}