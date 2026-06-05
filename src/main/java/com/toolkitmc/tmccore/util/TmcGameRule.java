package com.toolkitmc.tmccore.util;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class TmcGameRule {
    public static void setBoolean(World world, GameRules.Key<GameRules.BooleanRule> rule, boolean value) {
        world.getGameRules().get(rule).set(value, null);
    }

    public static boolean getBoolean(World world, GameRules.Key<GameRules.BooleanRule> rule) {
        return world.getGameRules().getBoolean(rule);
    }
}