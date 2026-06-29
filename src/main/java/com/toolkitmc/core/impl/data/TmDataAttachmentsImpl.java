package com.toolkitmc.core.impl.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.data.TmDataAttachments;
import com.toolkitmc.core.api.data.TmDataKey;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.*;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data attachments backed by:
 * - Player/entity: JSON string via {@link DataHolder} mixin interface
 * - World: {@link PersistentState} with Codec serialization (required by 1.21.8+)
 *
 * <p>All values are serialized to JSON via GSON, then stored as a single JSON string.
 */
public final class TmDataAttachmentsImpl implements TmDataAttachments {

    private static final Gson GSON = new Gson();

    private final Set<TmDataKey<?>> keepOnDeathKeys = ConcurrentHashMap.newKeySet();

    // -------------------------------------------------------------------------
    // Fabric callbacks
    // -------------------------------------------------------------------------

    public void registerFabricCallbacks() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            JsonObject oldData = getOrCreateJson(oldPlayer);
            JsonObject newData = getOrCreateJson(newPlayer);

            for (TmDataKey<?> key : keepOnDeathKeys) {
                String jsonKey = jsonKeyFor(key.getId());
                if (oldData.has(jsonKey)) {
                    newData.add(jsonKey, oldData.get(jsonKey));
                }
            }
            flushJson(newPlayer, newData);
        });
        TmCore.LOGGER.debug("TmDataAttachments callbacks registered.");
    }

    // -------------------------------------------------------------------------
    // Player
    // -------------------------------------------------------------------------

    @Override
    public <T> void setPlayer(ServerPlayerEntity player, TmDataKey<T> key, T value) {
        JsonObject data = getOrCreateJson(player);
        data.addProperty(jsonKeyFor(key.getId()), GSON.toJson(value));
        flushJson(player, data);
    }

    @Override
    public <T> T getPlayer(ServerPlayerEntity player, TmDataKey<T> key) {
        return getPlayerOpt(player, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getPlayerOpt(ServerPlayerEntity player, TmDataKey<T> key) {
        return readFromJson(getOrCreateJson(player), key);
    }

    @Override
    public <T> void removePlayer(ServerPlayerEntity player, TmDataKey<T> key) {
        JsonObject data = getOrCreateJson(player);
        data.remove(jsonKeyFor(key.getId()));
        flushJson(player, data);
    }

    @Override
    public boolean hasPlayer(ServerPlayerEntity player, TmDataKey<?> key) {
        return getOrCreateJson(player).has(jsonKeyFor(key.getId()));
    }

    // -------------------------------------------------------------------------
    // Entity
    // -------------------------------------------------------------------------

    @Override
    public <T> void setEntity(Entity entity, TmDataKey<T> key, T value) {
        JsonObject data = getOrCreateJson(entity);
        data.addProperty(jsonKeyFor(key.getId()), GSON.toJson(value));
        flushJson(entity, data);
    }

    @Override
    public <T> T getEntity(Entity entity, TmDataKey<T> key) {
        return getEntityOpt(entity, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getEntityOpt(Entity entity, TmDataKey<T> key) {
        return readFromJson(getOrCreateJson(entity), key);
    }

    @Override
    public <T> void removeEntity(Entity entity, TmDataKey<T> key) {
        JsonObject data = getOrCreateJson(entity);
        data.remove(jsonKeyFor(key.getId()));
        flushJson(entity, data);
    }

    // -------------------------------------------------------------------------
    // World (PersistentState)
    // -------------------------------------------------------------------------

    @Override
    public <T> void setWorld(ServerWorld world, TmDataKey<T> key, T value) {
        TmWorldData state = getWorldState(world);
        state.data.addProperty(jsonKeyFor(key.getId()), GSON.toJson(value));
        state.markDirty();
    }

    @Override
    public <T> T getWorld(ServerWorld world, TmDataKey<T> key) {
        return getWorldOpt(world, key).orElseGet(key::getDefault);
    }

    @Override
    public <T> Optional<T> getWorldOpt(ServerWorld world, TmDataKey<T> key) {
        return readFromJson(getWorldState(world).data, key);
    }

    @Override
    public <T> void removeWorld(ServerWorld world, TmDataKey<T> key) {
        TmWorldData state = getWorldState(world);
        state.data.remove(jsonKeyFor(key.getId()));
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
    // JSON helpers
    // -------------------------------------------------------------------------

    private static String jsonKeyFor(Identifier id) {
        return id.getNamespace() + "__" + id.getPath();
    }

    private static JsonObject parseJson(@Nullable String raw) {
        if (raw == null || raw.isEmpty()) return new JsonObject();
        try {
            return JsonParser.parseString(raw).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    private JsonObject getOrCreateJson(Entity entity) {
        if (entity instanceof DataHolder holder) {
            return parseJson(holder.tmcore_getJson());
        }
        TmCore.LOGGER.warn("Entity {} does not implement DataHolder.", entity.getClass().getSimpleName());
        return new JsonObject();
    }

    private void flushJson(Entity entity, JsonObject data) {
        if (entity instanceof DataHolder holder) {
            holder.tmcore_setJson(GSON.toJson(data));
        }
    }

    private <T> Optional<T> readFromJson(JsonObject data, TmDataKey<T> key) {
        String jsonKey = jsonKeyFor(key.getId());
        if (!data.has(jsonKey)) return Optional.empty();
        try {
            T value = GSON.fromJson(data.get(jsonKey), key.getType());
            return Optional.ofNullable(value);
        } catch (Exception e) {
            TmCore.LOGGER.warn("Failed to read data key {}: {}", key.getId(), e.getMessage());
            return Optional.empty();
        }
    }

    // -------------------------------------------------------------------------
    // PersistentState for world data (1.21.8+: must use Codec)
    // -------------------------------------------------------------------------

    private TmWorldData getWorldState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TmWorldData.TYPE);
    }

    public static final class TmWorldData extends PersistentState {

        JsonObject data = new JsonObject();

        public TmWorldData() {}

        /**
         * Codec for TmWorldData.
         * Stores all data as a single JSON string under "payload".
         */
        private static final Codec<TmWorldData> CODEC = Codec.STRING
            .fieldOf("payload")
            .codec()
            .xmap(
                json -> {
                    TmWorldData d = new TmWorldData();
                    d.data = parseJson(json);
                    return d;
                },
                d -> d.data.toString()
            );

        public static final PersistentStateType<TmWorldData> TYPE = new PersistentStateType<>(
            "tmcore_world_data",
            TmWorldData::new,
            CODEC,
            null   // DataFixTypes — not needed
        );

        private static JsonObject parseJson(String raw) {
            if (raw == null || raw.isEmpty()) return new JsonObject();
            try {
                return JsonParser.parseString(raw).getAsJsonObject();
            } catch (Exception e) {
                return new JsonObject();
            }
        }

        public JsonObject getData() { return data; }
    }
}
