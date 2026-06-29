package com.toolkitmc.tmccore.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TmcAdvancement {
    public static void grant(ServerPlayerEntity player, String advancementId) {
        AdvancementEntry adv = player.getServer().getAdvancementLoader()
                .get(Identifier.of(advancementId));
        if (adv != null) {
            player.getAdvancementTracker().grantCriterion(adv, "done");
        }
    }
}