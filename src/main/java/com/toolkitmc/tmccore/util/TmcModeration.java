package com.toolkitmc.tmccore.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TmcModeration {
    private static final Set<UUID> muted = new HashSet<>();

    public static void mute(ServerPlayerEntity player) {
        muted.add(player.getUuid());
        player.sendMessage(Text.literal("§cYou have been muted."), false);
    }

    public static void unmute(ServerPlayerEntity player) {
        muted.remove(player.getUuid());
    }

    public static boolean isMuted(ServerPlayerEntity player) {
        return muted.contains(player.getUuid());
    }
}