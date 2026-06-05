package com.toolkitmc.tmccore.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcChat {

    public static void send(ServerPlayerEntity player, String message) {
        player.sendMessage(Text.literal("§a[tmcCore] §f" + message), false);
    }

    public static void sendError(ServerPlayerEntity player, String message) {
        player.sendMessage(Text.literal("§c[tmcCore] §f" + message), false);
    }

    public static void broadcast(ServerPlayerEntity player, String message) {
        player.getServer().getPlayerManager().broadcast(Text.literal("§a[tmcCore] §f" + message), false);
    }

    public static void broadcastError(ServerPlayerEntity player, String message) {
        player.getServer().getPlayerManager().broadcast(Text.literal("§c[tmcCore] §f" + message), false);
    }
}