package com.toolkitmc.tmccore.util;

import net.minecraft.world.GameRules;
import net.minecraft.server.world.ServerWorld;

public class TmcGameRule {
    public static void setBoolean(ServerWorld world, GameRules.Key<GameRules.BooleanRule> rule, boolean value) {
        world.getGameRules().get(rule).set(value, world.getServer());
    }

    public static boolean getBoolean(ServerWorld world, GameRules.Key<GameRules.BooleanRule> rule) {
        return world.getGameRules().getBoolean(rule);
    }
}