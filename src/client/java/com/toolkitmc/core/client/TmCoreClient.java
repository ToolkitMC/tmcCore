package com.toolkitmc.core.client;

import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.impl.network.TmNetworkingImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class TmCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TmCore.LOGGER.info("tmCore client initializing.");

        // Apply deferred S2C packet receivers registered via TmNetworking.registerClientReceiver()
        ((TmNetworkingImpl) TmCore.networking()).applyClientReceivers();

        TmCore.LOGGER.info("tmCore client initialized.");
    }
}
