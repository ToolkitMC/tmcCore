package com.toolkitmc.tmccore.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;

public class TmcRenderEvents {

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            // Özel HUD çizimi burada yapılabilir
        });
    }

    public static void drawText(DrawContext context, String text, int x, int y, int color) {
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            text, x, y, color
        );
    }
}