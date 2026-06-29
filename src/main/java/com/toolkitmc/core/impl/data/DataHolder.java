package com.toolkitmc.core.impl.data;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

/**
 * Interface injected onto {@link net.minecraft.entity.Entity} via mixin.
 * Provides access to tmCore's persistent data NBT compound.
 */
public interface DataHolder {
    @Nullable NbtCompound tmcore_getData();
    void tmcore_setData(NbtCompound data);
}
