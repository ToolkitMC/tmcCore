package com.toolkitmc.tmccore;

import net.fabricmc.api.DedicatedServerModInitializer;

public class TmcCoreServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        TmcCore.LOGGER.info("tmcCore Server features initialized");
        // Add dedicated server utilities here
    }
}