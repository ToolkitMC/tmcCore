package com.toolkitmc.tmccore.util;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TmcBlockUtils {
    public static void setBlock(ServerWorld world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state);
    }

    public static BlockState getBlock(ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    public static void breakBlock(ServerWorld world, BlockPos pos, boolean drop) {
        world.breakBlock(pos, drop);
    }
}