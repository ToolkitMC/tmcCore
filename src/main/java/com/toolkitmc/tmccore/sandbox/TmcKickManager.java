package com.toolkitmc.tmccore.sandbox;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TmcKickManager {

    public static void kick(ServerPlayerEntity player, String reason) {
        player.networkHandler.disconnect(Text.literal("Kicked: " + reason));
        TmcAuditLogger.log(player, "Player kicked: " + reason);
    }
}