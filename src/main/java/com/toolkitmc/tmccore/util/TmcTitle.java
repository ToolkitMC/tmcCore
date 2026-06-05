package com.toolkitmc.tmccore.util;

import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcTitle {
    public static void sendTitle(ServerPlayerEntity player, String title, String subtitle) {
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(title)));
        player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal(subtitle)));
    }

    public static void sendActionbar(ServerPlayerEntity player, String message) {
        player.sendMessage(Text.literal(message), true);
    }
}