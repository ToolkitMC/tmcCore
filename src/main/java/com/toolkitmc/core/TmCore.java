package com.toolkitmc.core;

import com.toolkitmc.core.api.command.TmCommandRegistry;
import com.toolkitmc.core.api.config.TmConfigManager;
import com.toolkitmc.core.api.data.TmDataAttachments;
import com.toolkitmc.core.api.event.TmEventBus;
import com.toolkitmc.core.api.network.TmNetworking;
import com.toolkitmc.core.api.registry.TmRegistry;
import com.toolkitmc.core.impl.command.TmCommandRegistryImpl;
import com.toolkitmc.core.impl.config.TmConfigManagerImpl;
import com.toolkitmc.core.impl.data.TmDataAttachmentsImpl;
import com.toolkitmc.core.impl.event.TmEventBusImpl;
import com.toolkitmc.core.impl.network.TmNetworkingImpl;
import com.toolkitmc.core.impl.registry.TmRegistryImpl;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tmCore — Core API library for ToolkitMC Fabric mods.
 *
 * <p>Other mods depend on tmCore and access subsystems via static accessors:
 * <pre>
 *   TmCore.registry()    — cross-mod registry
 *   TmCore.events()      — custom event bus
 *   TmCore.config()      — JSON config management
 *   TmCore.networking()  — packet abstraction
 *   TmCore.commands()    — Brigadier wrapper
 *   TmCore.data()        — data attachments (player/entity/world)
 * </pre>
 */
public final class TmCore implements ModInitializer {

    public static final String MOD_ID = "tmcore";
    public static final String API_VERSION = "1";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Subsystem singletons — initialized in order during onInitialize
    private static TmRegistry registry;
    private static TmEventBus eventBus;
    private static TmConfigManager configManager;
    private static TmNetworking networking;
    private static TmCommandRegistry commandRegistry;
    private static TmDataAttachments dataAttachments;

    @Override
    public void onInitialize() {
        LOGGER.info("tmCore {} initializing (MC API version: {})", MOD_ID, API_VERSION);

        // Boot order matters — registry and event bus first, networking last
        registry = new TmRegistryImpl();
        eventBus = new TmEventBusImpl();
        configManager = new TmConfigManagerImpl();
        dataAttachments = new TmDataAttachmentsImpl();
        commandRegistry = new TmCommandRegistryImpl();
        networking = new TmNetworkingImpl();

        // Register internal Fabric hooks
        ((TmCommandRegistryImpl) commandRegistry).registerFabricCallback();
        ((TmNetworkingImpl) networking).registerChannels();
        ((TmDataAttachmentsImpl) dataAttachments).registerFabricCallbacks();

        LOGGER.info("tmCore initialized successfully.");
    }

    // -------------------------------------------------------------------------
    // Public API accessors — always non-null after onInitialize
    // -------------------------------------------------------------------------

    /** Cross-mod object registry. */
    public static TmRegistry registry() {
        assertInitialized(registry, "registry");
        return registry;
    }

    /** Custom event bus for ToolkitMC mods. */
    public static TmEventBus events() {
        assertInitialized(eventBus, "eventBus");
        return eventBus;
    }

    /** JSON-based per-mod config management. */
    public static TmConfigManager config() {
        assertInitialized(configManager, "configManager");
        return configManager;
    }

    /** S2C / C2S packet abstraction. */
    public static TmNetworking networking() {
        assertInitialized(networking, "networking");
        return networking;
    }

    /** Brigadier command registration wrapper. */
    public static TmCommandRegistry commands() {
        assertInitialized(commandRegistry, "commandRegistry");
        return commandRegistry;
    }

    /** Persistent data attachments for players, entities, and worlds. */
    public static TmDataAttachments data() {
        assertInitialized(dataAttachments, "dataAttachments");
        return dataAttachments;
    }

    private static void assertInitialized(Object obj, String name) {
        if (obj == null) {
            throw new IllegalStateException(
                "tmCore subsystem '" + name + "' accessed before TmCore.onInitialize() completed. " +
                "Ensure your mod's onInitialize runs after tmCore (add 'tmcore' to depends in fabric.mod.json)."
            );
        }
    }
}
