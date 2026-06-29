package com.toolkitmc.core.impl.data;

import org.jetbrains.annotations.Nullable;

/**
 * Interface injected onto {@link net.minecraft.entity.Entity} via mixin.
 *
 * <p>In 1.21.8+, entity serialization moved from NbtCompound to WriteView/ReadView.
 * All tmCore data is stored as a single JSON string under the "tmcore_data" key.
 */
public interface DataHolder {
    /** Returns the raw JSON payload for this entity, or {@code null} if never written. */
    @Nullable String tmcore_getJson();
    void tmcore_setJson(@Nullable String json);
}
