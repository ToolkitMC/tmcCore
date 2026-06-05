package com.toolkitmc.tmccore.region;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TmcRegion {

    private static final Map<String, Region> regions = new HashMap<>();

    public static void createRegion(String name, BlockPos pos1, BlockPos pos2) {
        BlockPos min = new BlockPos(
            Math.min(pos1.getX(), pos2.getX()),
            Math.min(pos1.getY(), pos2.getY()),
            Math.min(pos1.getZ(), pos2.getZ())
        );
        BlockPos max = new BlockPos(
            Math.max(pos1.getX(), pos2.getX()),
            Math.max(pos1.getY(), pos2.getY()),
            Math.max(pos1.getZ(), pos2.getZ())
        );
        regions.put(name, new Region(min, max));
    }

    public static boolean isInside(String name, BlockPos pos) {
        Region r = regions.get(name);
        return r != null && r.isInside(pos);
    }

    public static void removeRegion(String name) {
        regions.remove(name);
    }

    private record Region(BlockPos min, BlockPos max) {
        boolean isInside(BlockPos pos) {
            return pos.getX() >= min.getX() && pos.getX() <= max.getX() &&
                   pos.getY() >= min.getY() && pos.getY() <= max.getY() &&
                   pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
        }
    }
}