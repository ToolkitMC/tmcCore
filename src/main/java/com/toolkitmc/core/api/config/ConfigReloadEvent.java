package com.toolkitmc.core.api.config;

/**
 * Fired on the tmCore event bus after a config is reloaded from disk.
 *
 * <pre>
 *   TmCore.events().subscribe(ConfigReloadEvent.class, event -> {
 *       if ("mymod".equals(event.namespace())) {
 *           refreshCachedValues();
 *       }
 *   });
 * </pre>
 *
 * @param namespace   the mod ID whose config was reloaded
 * @param configClass the config class
 */
public record ConfigReloadEvent(String namespace, Class<?> configClass) {}
