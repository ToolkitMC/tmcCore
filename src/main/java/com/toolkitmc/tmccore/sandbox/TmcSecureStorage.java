package com.toolkitmc.tmccore.sandbox;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcSecureStorage {

    private static final Map<UUID, NbtCompound> secureData = new HashMap<>();

    public static NbtCompound getSecureData(ServerPlayerEntity player) {
        return secureData.computeIfAbsent(player.getUuid(), k -> new NbtCompound());
    }

    public static void setSecureData(ServerPlayerEntity player, NbtCompound data) {
        secureData.put(player.getUuid(), data);
    }

    public static void clearSecureData(UUID uuid) {
        secureData.remove(uuid);
    }
}