package com.toolkitmc.core.impl.registry;

import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.registry.TmRegistry;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TmRegistryImpl implements TmRegistry {

    // key: "namespace:path", value: registered object
    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private volatile boolean frozen = false;

    @Override
    public <T> void register(String namespace, String key, T value) {
        if (frozen) throw new IllegalStateException(
            "TmRegistry is frozen — cannot register '" + namespace + ":" + key + "' after server start."
        );
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        String compound = namespace + ":" + key;
        if (store.containsKey(compound)) {
            throw new IllegalArgumentException(
                "TmRegistry: '" + compound + "' is already registered."
            );
        }
        store.put(compound, value);
        TmCore.LOGGER.debug("Registered: {}", compound);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String namespace, String key, Class<T> type) {
        Object obj = store.get(namespace + ":" + key);
        if (obj == null) return Optional.empty();
        if (!type.isInstance(obj)) return Optional.empty();
        return Optional.of(type.cast(obj));
    }

    @Override
    public boolean contains(String namespace, String key) {
        return store.containsKey(namespace + ":" + key);
    }

    @Override
    public Collection<Identifier> getNamespace(String namespace) {
        List<Identifier> result = new ArrayList<>();
        String prefix = namespace + ":";
        for (String k : store.keySet()) {
            if (k.startsWith(prefix)) {
                result.add(Identifier.of(namespace, k.substring(prefix.length())));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public Collection<Identifier> getAll() {
        List<Identifier> result = new ArrayList<>();
        for (String k : store.keySet()) {
            int colon = k.indexOf(':');
            if (colon > 0) {
                result.add(Identifier.of(k.substring(0, colon), k.substring(colon + 1)));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public void unregister(String namespace, String key) {
        if (frozen) throw new IllegalStateException("TmRegistry is frozen.");
        store.remove(namespace + ":" + key);
    }

    @Override
    public void freeze() {
        this.frozen = true;
        TmCore.LOGGER.info("TmRegistry frozen with {} entries.", store.size());
    }

    @Override
    public boolean isFrozen() { return frozen; }
}
