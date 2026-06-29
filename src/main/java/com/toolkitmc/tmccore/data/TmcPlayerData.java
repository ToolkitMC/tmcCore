package com.toolkitmc.tmccore.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcPlayerData {

    private static final Map<UUID, NbtCompound> playerData = new HashMap<>();

    public static NbtCompound getOrCreate(ServerPlayerEntity player) {
        return playerData.computeIfAbsent(player.getUuid(), k -> new NbtCompound());
    }

    public static void save(ServerPlayerEntity player, NbtCompound data) {
        playerData.put(player.getUuid(), data);
    }

    public static void remove(UUID uuid) {
        playerData.remove(uuid);
    }
}