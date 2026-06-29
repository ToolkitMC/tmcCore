package com.toolkitmc.tmccore.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class TmcInventory {
    public static boolean hasItem(PlayerEntity player, ItemStack item) {
        return player.getInventory().contains(item);
    }

    public static void giveItem(PlayerEntity player, ItemStack stack) {
        player.getInventory().offerOrDrop(stack);
    }

    public static void clearSlot(PlayerEntity player, int slot) {
        player.getInventory().setStack(slot, ItemStack.EMPTY);
    }
}