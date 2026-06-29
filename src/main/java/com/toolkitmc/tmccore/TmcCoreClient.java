package com.toolkitmc.tmccore;

import net.fabricmc.api.ClientModInitializer;

public class TmcCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TmcCore.LOGGER.info("tmcCore Client features initialized");
        // Add client-only utilities here (rendering, keybinds, etc.)
    }
}