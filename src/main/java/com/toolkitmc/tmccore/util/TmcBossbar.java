package com.toolkitmc.tmccore.util;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcBossbar {
    public static ServerBossBar create(String title, BossBar.Color color) {
        return new ServerBossBar(Text.literal(title), color, BossBar.Style.PROGRESS);
    }

    public static void addPlayer(ServerBossBar bar, ServerPlayerEntity player) {
        bar.addPlayer(player);
    }

    public static void setProgress(ServerBossBar bar, float progress) {
        bar.setPercent(progress);
    }
}