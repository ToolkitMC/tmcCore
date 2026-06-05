package com.toolkitmc.tmccore.util;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class TmcItemHelper {

    public static void execute(ItemStack stack, Runnable action) {
        action.run();
    }

    public static void runWithPlayer(ServerPlayerEntity player, Runnable action) {
        action.run();
    }

    public static void runWithServer(MinecraftServer server, Runnable action) {
        action.run();
    }
}