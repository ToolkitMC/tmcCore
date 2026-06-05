package com.toolkitmc.tmccore.util;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TmcBlockHelper {

    public static void execute(BlockPos pos, BlockState state, Runnable action) {
        action.run();
    }

    public static void runWithPlayer(ServerPlayerEntity player, Runnable action) {
        action.run();
    }

    public static void runWithServer(MinecraftServer server, Runnable action) {
        action.run();
    }
}