package com.toolkitmc.core.api.registry;

import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Optional;

/**
 * Cross-mod object registry for ToolkitMC mods.
 *
 * <p>Mods register named objects under their own namespace:
 * <pre>
 *   TmCore.registry().register("mymod", "my_feature", myFeatureInstance);
 *   Optional&lt;MyFeature&gt; f = TmCore.registry().get("mymod", "my_feature", MyFeature.class);
 * </pre>
 *
 * <p>All registrations are frozen after {@code SERVER_STARTED} — late registration
 * throws {@link IllegalStateException}.
 */
public interface TmRegistry {

    /**
     * Registers an object under the given namespace and key.
     *
     * @param namespace the mod ID of the registering mod
     * @param key       unique key within that namespace
     * @param value     the object to register (must not be null)
     * @throws IllegalArgumentException if namespace/key already registered
     * @throws IllegalStateException    if registry is frozen
     */
    <T> void register(String namespace, String key, T value);

    /**
     * Registers an object using an {@link Identifier} (namespace:key).
     */
    default <T> void register(Identifier id, T value) {
        register(id.getNamespace(), id.getPath(), value);
    }

    /**
     * Retrieves a registered object by namespace and key.
     *
     * @param type the expected type of the registered object
     * @return an Optional containing the object if found and type-compatible
     */
    <T> Optional<T> get(String namespace, String key, Class<T> type);

    /**
     * Retrieves using an {@link Identifier}.
     */
    default <T> Optional<T> get(Identifier id, Class<T> type) {
        return get(id.getNamespace(), id.getPath(), type);
    }

    /**
     * Returns {@code true} if the given namespace:key is registered.
     */
    boolean contains(String namespace, String key);

    default boolean contains(Identifier id) {
        return contains(id.getNamespace(), id.getPath());
    }

    /**
     * Returns all registered identifiers under the given namespace.
     */
    Collection<Identifier> getNamespace(String namespace);

    /**
     * Returns all registered identifiers across all namespaces.
     */
    Collection<Identifier> getAll();

    /**
     * Removes a registration. Only allowed before freeze.
     */
    void unregister(String namespace, String key);

    /**
     * Freezes the registry — no further registrations allowed.
     * Called automatically by tmCore after server start.
     */
    void freeze();

    boolean isFrozen();
}
