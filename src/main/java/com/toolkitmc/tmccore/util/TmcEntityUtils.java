package com.toolkitmc.tmccore.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TmcEntityUtils {
    public static void teleport(Entity entity, BlockPos pos) {
        if (entity.getWorld() instanceof ServerWorld world) {
            entity.teleport(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, java.util.Set.of(), 0, 0);
        }
    }

    public static void killEntity(Entity entity) {
        entity.kill();
    }
}