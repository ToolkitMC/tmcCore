package com.toolkitmc.core.api.config;

import java.nio.file.Path;
import java.util.Optional;

/**
 * JSON-based per-mod config management.
 *
 * <p>Each mod registers a config class (POJO with defaults). tmCore handles
 * serialization, auto-reload on file change, and validation.
 *
 * <pre>
 *   // Define config
 *   public class MyModConfig {
 *       public boolean enableFeature = true;
 *       public int maxCooldownTicks = 20;
 *       public String prefix = "[MyMod]";
 *   }
 *
 *   // Register during onInitialize (returns current values immediately)
 *   MyModConfig cfg = TmCore.config().register("mymod", MyModConfig.class, new MyModConfig());
 *
 *   // Access later
 *   boolean active = TmCore.config().get("mymod", MyModConfig.class).enableFeature;
 *
 *   // Reload from disk manually
 *   TmCore.config().reload("mymod");
 *
 *   // Save current in-memory values to disk
 *   TmCore.config().save("mymod");
 * </pre>
 *
 * <p>Config files are stored at {@code config/<namespace>.json}.
 */
public interface TmConfigManager {

    /**
     * Registers a config class for a mod namespace.
     * If a config file already exists on disk, its values are loaded.
     * If not, {@code defaults} is written to disk.
     *
     * @param namespace    the mod ID
     * @param configClass  the config POJO class
     * @param defaults     instance with default values
     * @return the loaded (or default) config instance
     */
    <T> T register(String namespace, Class<T> configClass, T defaults);

    /**
     * Returns the current in-memory config for the given namespace.
     *
     * @throws IllegalArgumentException if namespace is not registered
     */
    <T> T get(String namespace, Class<T> configClass);

    /**
     * Reloads the config from disk, merging with defaults for missing keys.
     * Fires a {@link ConfigReloadEvent} after reload.
     */
    void reload(String namespace);

    /**
     * Saves the current in-memory config to disk.
     */
    void save(String namespace);

    /**
     * Returns the path of the config file for the given namespace.
     */
    Path getConfigPath(String namespace);

    /**
     * Resets a config to its registered defaults and saves.
     */
    void reset(String namespace);

    /**
     * Returns the namespace set of all registered configs.
     */
    java.util.Set<String> getRegisteredNamespaces();

    /**
     * Returns the raw JSON string of the current config, for debug/display.
     */
    Optional<String> toJson(String namespace);
}
