package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcNotification {

    public static void send(ServerPlayerEntity player, String message) {
        player.sendMessage(Text.literal("§e[Notification] §f" + message), false);
    }

    public static void broadcast(ServerPlayerEntity player, String message) {
        player.getServer().getPlayerManager().broadcast(
            Text.literal("§e[Notification] §f" + message), false
        );
    }
}