package com.toolkitmc.tmccore.config;

import java.util.HashMap;
import java.util.Map;

public class TmcConfig {
    public String modId;
    public String version;
    public Map<String, Object> settings = new HashMap<>();
    public boolean enabled = true;

    public TmcConfig() {}

    public Object getSetting(String key) {
        return settings.get(key);
    }
}