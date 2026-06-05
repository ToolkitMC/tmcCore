package com.toolkitmc.tmccore;

import com.toolkitmc.tmccore.config.TmcConfigManager;
import com.toolkitmc.tmccore.event.TmcEventBus;
import com.toolkitmc.tmccore.network.TmcNetwork;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TmcCore implements ModInitializer {
    public static final String MOD_ID = "tmccore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TmcEventBus EVENT_BUS;

    @Override
    public void onInitialize() {
        LOGGER.info("tmcCore library initializing...");

        EVENT_BUS = new TmcEventBus();

        TmcConfigManager.init();
        // TmcNetwork.init(); // Fabric API gerektirir - devre dışı

        LOGGER.info("tmcCore fully loaded! (All modules active)");
    }
}