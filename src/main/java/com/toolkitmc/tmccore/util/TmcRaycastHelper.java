package com.toolkitmc.tmccore.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;

public class TmcRaycastHelper {

    public static void execute(ServerPlayerEntity player, BlockHitResult result, Runnable action) {
        action.run();
    }

    public static void runWithPlayer(ServerPlayerEntity player, Runnable action) {
        action.run();
    }

    public static void runWithServer(MinecraftServer server, Runnable action) {
        action.run();
    }
}