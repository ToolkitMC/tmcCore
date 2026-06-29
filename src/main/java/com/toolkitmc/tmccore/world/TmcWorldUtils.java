package com.toolkitmc.tmccore.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TmcWorldUtils {
    public static boolean isDay(World world) {
        return world.getTimeOfDay() % 24000 < 12000;
    }

    public static BlockPos getSpawnPos(ServerWorld world) {
        return world.getSpawnPos();
    }

    public static void setTime(ServerWorld world, long time) {
        world.setTimeOfDay(time);
    }
}