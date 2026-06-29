package com.toolkitmc.tmccore.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class TmcItemBuilder {
    private ItemStack stack;

    public TmcItemBuilder() {
        this.stack = new ItemStack(Items.DIAMOND);
    }

    public TmcItemBuilder setName(String name) {
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name));
        return this;
    }

    public TmcItemBuilder setCount(int count) {
        stack.setCount(count);
        return this;
    }

    public TmcItemBuilder addNbt(String key, String value) {
        // NBT is now handled via Data Components in 1.21.1+
        return this;
    }

    public ItemStack build() {
        return stack;
    }
}