package com.toolkitmc.tmccore.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public class TmcGui {
    public static void openMenu(PlayerEntity player, String title, int rows) {
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
            (syncId, inv, p) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inv),
            Text.literal(title)
        ));
    }
}