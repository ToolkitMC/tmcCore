package com.toolkitmc.tmccore.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcServerUtils {
    public static void broadcast(MinecraftServer server, String message) {
        server.getPlayerManager().broadcast(Text.literal("[tmcCore] " + message), false);
    }

    public static void sendToPlayer(ServerPlayerEntity player, String message) {
        player.sendMessage(Text.literal("[tmcCore] " + message), false);
    }
}