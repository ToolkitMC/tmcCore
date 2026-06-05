package com.toolkitmc.tmccore.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class TmcEntityHelper {

    public static void execute(Entity entity, Runnable action) {
        action.run();
    }

    public static void runWithPlayer(ServerPlayerEntity player, Runnable action) {
        action.run();
    }

    public static void runWithServer(MinecraftServer server, Runnable action) {
        action.run();
    }
}