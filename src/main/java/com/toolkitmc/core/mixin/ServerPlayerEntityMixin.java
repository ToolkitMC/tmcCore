package com.toolkitmc.core.mixin;

import com.toolkitmc.core.impl.data.DataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects tmCore data storage into all entities via the 1.21.8+ WriteView/ReadView API.
 *
 * <p>writeNbt/readNbt were removed in 1.21.8. Entity custom data is now persisted via
 * {@code writeCustomData(WriteView)} and {@code readCustomData(ReadView)}.
 *
 * <p>All tmCore data is serialized as a single JSON string stored under "tmcore_data".
 */
@Mixin(Entity.class)
public abstract class ServerPlayerEntityMixin implements DataHolder {

    private static final String TMCORE_KEY = "tmcore_data";

    @Unique
    private @Nullable String tmcore$json = null;

    @Override
    public @Nullable String tmcore_getJson() {
        return tmcore$json;
    }

    @Override
    public void tmcore_setJson(@Nullable String json) {
        this.tmcore$json = json;
    }

    @Inject(
        method = "writeCustomData(Lnet/minecraft/storage/WriteView;)V",
        at = @At("RETURN")
    )
    private void tmcore$writeCustomData(WriteView view, CallbackInfo ci) {
        if (tmcore$json != null && !tmcore$json.isEmpty()) {
            view.putString(TMCORE_KEY, tmcore$json);
        }
    }

    @Inject(
        method = "readCustomData(Lnet/minecraft/storage/ReadView;)V",
        at = @At("RETURN")
    )
    private void tmcore$readCustomData(ReadView view, CallbackInfo ci) {
        // getOptionalString returns Optional<String> — only present if key exists
        view.getOptionalString(TMCORE_KEY).ifPresent(json -> tmcore$json = json);
    }
}
