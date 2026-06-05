package com.toolkitmc.tmccore.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TmcHud {
    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            draw(context);
        });
    }

    private static void draw(DrawContext context) {
        // Example HUD text
        context.drawTextWithShadow(
            TmcClientUtils.getClient().textRenderer,
            Text.literal("tmcCore HUD Active"),
            10, 10, 0xFFFFFF
        );
    }
}