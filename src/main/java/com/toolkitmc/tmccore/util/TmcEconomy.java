package com.toolkitmc.tmccore.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TmcEconomy {

    private static final Map<UUID, Integer> balances = new HashMap<>();

    public static int getBalance(ServerPlayerEntity player) {
        return balances.getOrDefault(player.getUuid(), 0);
    }

    public static void setBalance(ServerPlayerEntity player, int amount) {
        balances.put(player.getUuid(), Math.max(0, amount));
    }

    public static void addBalance(ServerPlayerEntity player, int amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public static boolean takeBalance(ServerPlayerEntity player, int amount) {
        int current = getBalance(player);
        if (current >= amount) {
            setBalance(player, current - amount);
            return true;
        }
        return false;
    }

    public static boolean transfer(ServerPlayerEntity from, ServerPlayerEntity to, int amount) {
        if (takeBalance(from, amount)) {
            addBalance(to, amount);
            return true;
        }
        return false;
    }
}