package com.toolkitmc.core.impl.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.config.ConfigReloadEvent;
import com.toolkitmc.core.api.config.TmConfigManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class TmConfigManagerImpl implements TmConfigManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .serializeNulls()
        .create();

    private final Path configDir;

    // namespace → ConfigEntry
    private final Map<String, ConfigEntry<?>> entries = new LinkedHashMap<>();

    public TmConfigManagerImpl() {
        this.configDir = FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public <T> T register(String namespace, Class<T> configClass, T defaults) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(configClass, "configClass");
        Objects.requireNonNull(defaults, "defaults");

        if (entries.containsKey(namespace)) {
            throw new IllegalArgumentException("Config already registered for namespace: " + namespace);
        }

        Path path = getConfigPath(namespace);
        T loaded = loadOrDefault(path, configClass, defaults);
        entries.put(namespace, new ConfigEntry<>(configClass, loaded, defaults));
        TmCore.LOGGER.info("Config registered: {} → {}", namespace, path);
        return loaded;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String namespace, Class<T> configClass) {
        ConfigEntry<?> entry = requireEntry(namespace);
        return configClass.cast(entry.instance());
    }

    @Override
    public void reload(String namespace) {
        ConfigEntry<?> entry = requireEntry(namespace);
        Path path = getConfigPath(namespace);
        reloadEntry(namespace, path, entry);
        TmCore.events().fire(new ConfigReloadEvent(namespace, entry.configClass()));
        TmCore.LOGGER.info("Config reloaded: {}", namespace);
    }

    /** Generic helper so the compiler can bind T across loadOrDefault and ConfigEntry<T>. */
    private <T> void reloadEntry(String namespace, Path path, ConfigEntry<T> entry) {
        T reloaded = loadOrDefault(path, entry.configClass(), entry.defaults());
        entries.put(namespace, new ConfigEntry<>(entry.configClass(), reloaded, entry.defaults()));
    }

    @Override
    public void save(String namespace) {
        ConfigEntry<?> entry = requireEntry(namespace);
        writeToFile(getConfigPath(namespace), entry.instance());
    }

    @Override
    public Path getConfigPath(String namespace) {
        return configDir.resolve(namespace + ".json");
    }

    @Override
    public void reset(String namespace) {
        ConfigEntry<?> entry = requireEntry(namespace);
        resetEntry(namespace, entry);
        TmCore.LOGGER.info("Config reset to defaults: {}", namespace);
    }

    /** Generic helper to bind T for reset. */
    private <T> void resetEntry(String namespace, ConfigEntry<T> entry) {
        entries.put(namespace, new ConfigEntry<>(entry.configClass(), entry.defaults(), entry.defaults()));
        writeToFile(getConfigPath(namespace), entry.defaults());
    }

    @Override
    public Set<String> getRegisteredNamespaces() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public Optional<String> toJson(String namespace) {
        ConfigEntry<?> entry = entries.get(namespace);
        if (entry == null) return Optional.empty();
        return Optional.of(GSON.toJson(entry.instance()));
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private <T> T loadOrDefault(Path path, Class<T> type, T defaults) {
        if (!Files.exists(path)) {
            writeToFile(path, defaults);
            return defaults;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            T loaded = GSON.fromJson(reader, type);
            if (loaded == null) {
                TmCore.LOGGER.warn("Config file {} is empty or malformed, using defaults.", path);
                return defaults;
            }
            // Merge: fill missing fields from defaults by round-tripping
            JsonObject defaultJson = GSON.toJsonTree(defaults).getAsJsonObject();
            JsonObject loadedJson = GSON.toJsonTree(loaded).getAsJsonObject();
            mergeDefaults(defaultJson, loadedJson);
            T merged = GSON.fromJson(loadedJson, type);
            // Overwrite file with merged result so missing keys are persisted
            writeToFile(path, merged);
            return merged;
        } catch (IOException | com.google.gson.JsonParseException e) {
            TmCore.LOGGER.error("Failed to load config {}: {}. Using defaults.", path, e.getMessage());
            return defaults;
        }
    }

    /** Copies keys from {@code defaults} into {@code target} if they are missing. */
    private void mergeDefaults(JsonObject defaults, JsonObject target) {
        for (Map.Entry<String, com.google.gson.JsonElement> entry : defaults.entrySet()) {
            if (!target.has(entry.getKey())) {
                target.add(entry.getKey(), entry.getValue());
            }
        }
    }

    private void writeToFile(Path path, Object obj) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(obj, writer);
            }
        } catch (IOException e) {
            TmCore.LOGGER.error("Failed to write config {}: {}", path, e.getMessage());
        }
    }

    private ConfigEntry<?> requireEntry(String namespace) {
        ConfigEntry<?> entry = entries.get(namespace);
        if (entry == null) throw new IllegalArgumentException(
            "No config registered for namespace: " + namespace
        );
        return entry;
    }

    // -------------------------------------------------------------------------

    private record ConfigEntry<T>(Class<T> configClass, T instance, T defaults) {}
}
