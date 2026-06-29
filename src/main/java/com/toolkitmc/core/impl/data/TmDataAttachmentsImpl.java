package com.toolkitmc.core.impl.data;

import com.google.gson.Gson;
import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.data.TmDataAttachments;
import com.toolkitmc.core.api.data.TmDataKey;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data attachments backed by:
 * - Player/entity: NBT via {@link DataHolder} mixin interface
 * - World: {@link PersistentState}
 *
 * <p>NBT serialization uses GSON for typed values. For production use
 * of complex types, consider adding explicit Codec-based serializers.
 */
public final class TmDataAttachmentsImpl implements TmDataAttachments {

    private static final Gson GSON = new Gson();
    private static final String NBT_ROOT_KEY = "tmcore_data";

    private final Set<TmDataKey<?>> keepOnDeathKeys = ConcurrentHashMap.newKeySet();

    // -------------------------------------------------------------------------
    // Fabric callbacks
    // -------------------------------------------------------------------------

    public void registerFabricCallbacks() {
        // Copy keep-on-death keys from the old entity to the new one on respawn
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            NbtCompound oldNbt = getOrCreatePlayerNbt(oldPlayer);
            NbtCompound newNbt = getOrCreatePlayerNbt(newPlayer);

            for (TmDataKey<?> key : keepOnDeathKeys) {
                String nbtKey = nbtKeyFor(key.getId());
                if (oldNbt.contains(nbtKey)) {
                    newNbt.put(nbtKey, oldNbt.get(nbtKey));
                }
            }
        });

        TmCore.LOGGER.debug("TmDataAttachments callbacks registered.");
    }

    // -------------------------------------------------------------------------
    // Player
    // -------------------------------------------------------------------------

    @Override
    public <T> void setPlayer(ServerPlayerEntity player, TmDataKey<T> key, T value) {
        NbtCompound nbt = getOrCreatePlayerNbt(player);
        writeValue(nbt, key, value);
    }

    @Override
    public <T> T getPlayer(ServerPlayerEntity player, TmDataKey<T> key) {
        return getPlayerOpt(player, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getPlayerOpt(ServerPlayerEntity player, TmDataKey<T> key) {
        NbtCompound nbt = getOrCreatePlayerNbt(player);
        return readValue(nbt, key);
    }

    @Override
    public <T> void removePlayer(ServerPlayerEntity player, TmDataKey<T> key) {
        getOrCreatePlayerNbt(player).remove(nbtKeyFor(key.getId()));
    }

    @Override
    public boolean hasPlayer(ServerPlayerEntity player, TmDataKey<?> key) {
        return getOrCreatePlayerNbt(player).contains(nbtKeyFor(key.getId()));
    }

    // -------------------------------------------------------------------------
    // Entity
    // -------------------------------------------------------------------------

    @Override
    public <T> void setEntity(Entity entity, TmDataKey<T> key, T value) {
        NbtCompound nbt = getOrCreateEntityNbt(entity);
        writeValue(nbt, key, value);
    }

    @Override
    public <T> T getEntity(Entity entity, TmDataKey<T> key) {
        return getEntityOpt(entity, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getEntityOpt(Entity entity, TmDataKey<T> key) {
        NbtCompound nbt = getOrCreateEntityNbt(entity);
        return readValue(nbt, key);
    }

    @Override
    public <T> void removeEntity(Entity entity, TmDataKey<T> key) {
        getOrCreateEntityNbt(entity).remove(nbtKeyFor(key.getId()));
    }

    // -------------------------------------------------------------------------
    // World (PersistentState)
    // -------------------------------------------------------------------------

    @Override
    public <T> void setWorld(ServerWorld world, TmDataKey<T> key, T value) {
        TmWorldData state = getWorldState(world);
        writeValue(state.getData(), key, value);
        state.markDirty();
    }

    @Override
    public <T> T getWorld(ServerWorld world, TmDataKey<T> key) {
        return getWorldOpt(world, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getWorldOpt(ServerWorld world, TmDataKey<T> key) {
        TmWorldData state = getWorldState(world);
        return readValue(state.getData(), key);
    }

    @Override
    public <T> void removeWorld(ServerWorld world, TmDataKey<T> key) {
        TmWorldData state = getWorldState(world);
        state.getData().remove(nbtKeyFor(key.getId()));
        state.markDirty();
    }

    // -------------------------------------------------------------------------
    // Death persistence
    // -------------------------------------------------------------------------

    @Override
    public <T> void keepOnDeath(TmDataKey<T> key) {
        keepOnDeathKeys.add(key);
    }

    @Override
    public boolean isKeptOnDeath(TmDataKey<?> key) {
        return keepOnDeathKeys.contains(key);
    }

    // -------------------------------------------------------------------------
    // NBT helpers
    // -------------------------------------------------------------------------

    private static String nbtKeyFor(Identifier id) {
        // Use "__" as separator — colons aren't allowed in NBT keys
        return id.getNamespace() + "__" + id.getPath();
    }

    private <T> void writeValue(NbtCompound nbt, TmDataKey<T> key, T value) {
        nbt.putString(nbtKeyFor(key.getId()), GSON.toJson(value));
    }

    private <T> Optional<T> readValue(NbtCompound nbt, TmDataKey<T> key) {
        String nbtKey = nbtKeyFor(key.getId());
        if (!nbt.contains(nbtKey)) return Optional.empty();
        try {
            // 1.21.8+: getString returns Optional<String>
            Optional<String> raw = nbt.getString(nbtKey);
            if (raw.isEmpty()) return Optional.empty();
            T value = GSON.fromJson(raw.get(), key.getType());
            return Optional.ofNullable(value);
        } catch (Exception e) {
            TmCore.LOGGER.warn("Failed to read data key {}: {}", key.getId(), e.getMessage());
            return Optional.empty();
        }
    }

    // -------------------------------------------------------------------------
    // NBT access via DataHolder mixin interface
    // -------------------------------------------------------------------------

    private NbtCompound getOrCreatePlayerNbt(ServerPlayerEntity player) {
        return getOrCreateEntityNbt(player);
    }

    private NbtCompound getOrCreateEntityNbt(Entity entity) {
        if (entity instanceof DataHolder holder) {
            NbtCompound root = holder.tmcore_getData();
            if (root == null) {
                root = new NbtCompound();
                holder.tmcore_setData(root);
            }
            return root;
        }
        TmCore.LOGGER.warn("Entity {} does not implement DataHolder — data will not persist.", entity.getClass().getSimpleName());
        return new NbtCompound();
    }

    private TmWorldData getWorldState(ServerWorld world) {
        // 1.21.8+: PersistentStateManager.getOrCreate() takes PersistentStateType<T>
        return world.getPersistentStateManager().getOrCreate(TmWorldData.TYPE);
    }

    // -------------------------------------------------------------------------
    // PersistentState for world data
    // -------------------------------------------------------------------------

    public static final class TmWorldData extends PersistentState {
        private NbtCompound data = new NbtCompound();

        /** PersistentStateType — required by 1.21.8+ API */
        public static final PersistentStateType<TmWorldData> TYPE = new PersistentStateType<>(
            "tmcore_world_data",
            TmWorldData::new,
            TmWorldData::readNbt,
            null  // codec — optional, null is fine
        );

        public TmWorldData() {}

        public static TmWorldData readNbt(NbtCompound nbt) {
            TmWorldData state = new TmWorldData();
            // 1.21.8+: getCompound returns Optional<NbtCompound>
            nbt.getCompound("data").ifPresent(c -> state.data = c);
            return state;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            nbt.put("data", data);
            return nbt;
        }

        public NbtCompound getData() { return data; }
    }
}
