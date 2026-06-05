package com.toolkitmc.tmccore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toolkitmc.tmccore.TmcCore;
import com.toolkitmc.tmccore.event.TmcEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TmcConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("tmcCore");
    private static final Map<String, TmcConfig> LOADED_CONFIGS = new HashMap<>();

    public static void init() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            TmcCore.LOGGER.error("Failed to create config dir", e);
        }

        loadFromAllMods();
        loadExternalConfigs();

        TmcCore.LOGGER.info("tmcCore config system loaded {} configs", LOADED_CONFIGS.size());
    }

    private static void loadFromAllMods() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            mod.getRootPaths().forEach(root -> {
                try {
                    Path tmcDir = root.resolve("tmcCore");
                    if (Files.exists(tmcDir)) {
                        try (var stream = Files.walk(tmcDir)) {
                            stream.filter(p -> p.toString().endsWith(".json"))
                                    .forEach(p -> loadConfig(p, true));
                        }
                    }
                } catch (Exception ignored) {}
            });
        }
    }

    private static void loadExternalConfigs() {
        try (var stream = Files.walk(CONFIG_DIR)) {
            stream.filter(p -> p.toString().endsWith(".json")).forEach(p -> loadConfig(p, false));
        } catch (IOException ignored) {}
    }

    private static void loadConfig(Path path, boolean fromJar) {
        try {
            TmcConfig cfg = GSON.fromJson(new InputStreamReader(Files.newInputStream(path)), TmcConfig.class);
            String key = (fromJar ? "jar:" : "ext:") + path.getFileName();
            LOADED_CONFIGS.put(key, cfg);
            TmcCore.EVENT_BUS.post(new TmcEvents.ConfigReloaded(key));
        } catch (Exception e) {
            TmcCore.LOGGER.warn("Config load failed: {}", path);
        }
    }

    public static TmcConfig get(String name) {
        return LOADED_CONFIGS.get(name);
    }
}