package com.toolkitmc.tmccore.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TmcClientUtils {
    public static void sendClientMessage(String message) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("[tmcCore] " + message), false);
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
}