package com.toolkitmc.tmccore.util;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class TmcScoreboard {
    public static void createObjective(MinecraftServer server, String name, String displayName) {
        Scoreboard sb = server.getScoreboard();
        if (sb.getNullableObjective(name) == null) {
            sb.addObjective(name, null, Text.literal(displayName), null, false, null);
        }
    }

    public static void setScore(MinecraftServer server, String objective, net.minecraft.scoreboard.ScoreHolder holder, int score) {
        Scoreboard sb = server.getScoreboard();
        ScoreboardObjective obj = sb.getNullableObjective(objective);
        if (obj != null) {
            sb.getOrCreateScore(holder, obj).setScore(score);
        }
    }
}