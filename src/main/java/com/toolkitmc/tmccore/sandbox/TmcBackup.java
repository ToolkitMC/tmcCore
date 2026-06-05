package com.toolkitmc.tmccore.sandbox;

import com.toolkitmc.tmccore.TmcCore;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcBackup {

    private static final Map<UUID, NbtCompound> backups = new HashMap<>();

    public static void backup(UUID uuid, NbtCompound data) {
        backups.put(uuid, data.copy());
        TmcCore.LOGGER.info("[Backup] Created backup for {}", uuid);
    }

    public static NbtCompound restore(UUID uuid) {
        return backups.getOrDefault(uuid, new NbtCompound());
    }

    public static void clear(UUID uuid) {
        backups.remove(uuid);
    }
}