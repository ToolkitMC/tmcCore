package com.toolkitmc.core.mixin;

import com.toolkitmc.core.impl.data.DataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects tmCore data storage into all entities.
 * Data is persisted via writeNbt/readNbt hooks.
 */
@Mixin(Entity.class)
public abstract class ServerPlayerEntityMixin implements DataHolder {

    @Unique
    private @Nullable NbtCompound tmcore$data = null;

    @Override
    public @Nullable NbtCompound tmcore_getData() {
        return tmcore$data;
    }

    @Override
    public void tmcore_setData(NbtCompound data) {
        this.tmcore$data = data;
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void tmcore$writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (tmcore$data != null && !tmcore$data.isEmpty()) {
            nbt.put("tmcore_data", tmcore$data);
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    private void tmcore$readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("tmcore_data")) {
            tmcore$data = nbt.getCompound("tmcore_data");
        }
    }
}
