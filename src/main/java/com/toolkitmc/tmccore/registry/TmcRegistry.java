package com.toolkitmc.tmccore.registry;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TmcRegistry {

    public static <T> void register(Registry<T> registry, String path, T entry) {
        Registry.register(registry, Identifier.of("tmccore", path), entry);
    }
}