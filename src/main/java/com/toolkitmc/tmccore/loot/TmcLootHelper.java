package com.toolkitmc.tmccore.loot;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class TmcLootHelper {

    public static void dropLoot(ServerWorld world, Identifier lootTableId, LootContextParameterSet params) {
        RegistryKey<LootTable> key = RegistryKey.of(RegistryKeys.LOOT_TABLE, lootTableId);
        LootTable lootTable = world.getServer().getReloadableRegistries().getLootTable(key);
        lootTable.generateLoot(params, stack -> {
            // Item düşürme işlemi burada yapılabilir
        });
    }
}
