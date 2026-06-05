package com.toolkitmc.tmccore.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TmcWorldData {

    private static final Map<Identifier, NbtCompound> worldData = new HashMap<>();

    public static NbtCompound getOrCreate(ServerWorld world) {
        return worldData.computeIfAbsent(world.getRegistryKey().getValue(), k -> new NbtCompound());
    }

    public static void save(ServerWorld world, NbtCompound data) {
        worldData.put(world.getRegistryKey().getValue(), data);
    }
}