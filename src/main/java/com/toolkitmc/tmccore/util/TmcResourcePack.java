package com.toolkitmc.tmccore.util;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class TmcResourcePack {
    public static void register(String modId, String packName) {
        ResourceManagerHelper.registerBuiltinResourcePack(
            Identifier.of(modId, packName),
            FabricLoader.getInstance().getModContainer(modId).orElseThrow(),
            ResourcePackActivationType.NORMAL
        );
    }
}