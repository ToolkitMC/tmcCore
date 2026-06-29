package com.toolkitmc.tmccore.util;
import net.minecraft.server.world.ServerWorld;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TmcTeleport {
    private static final java.util.Map<java.util.UUID, BlockPos> lastLocations = new java.util.HashMap<>();

    public static void teleportWithHistory(ServerPlayerEntity player, BlockPos pos) {
        lastLocations.put(player.getUuid(), player.getBlockPos());
        player.teleport((ServerWorld) player.getWorld(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, java.util.Set.of(), 0.0F, 0.0F, false);
    }

    public static void back(ServerPlayerEntity player) {
        BlockPos last = lastLocations.get(player.getUuid());
        if (last != null) {
            player.teleport((ServerWorld) player.getWorld(), last.getX(), last.getY(), last.getZ(), java.util.Set.of(), 0.0F, 0.0F, false);
        }
    }
}