package com.toolkitmc.tmccore.registry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TmcRegistryHelper {

    public static <T extends Item> T registerItem(String modId, String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(modId, name), item);
    }

    public static <T extends Block> T registerBlock(String modId, String name, T block) {
        return Registry.register(Registries.BLOCK, Identifier.of(modId, name), block);
    }

    public static <T extends EntityType<?>> T registerEntity(String modId, String name, T entityType) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(modId, name), entityType);
    }
}