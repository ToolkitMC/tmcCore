package com.toolkitmc.core.api.data;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Persistent data attachment system for players, entities, and worlds.
 *
 * <p>Provides typed key-value storage that persists through death (optionally),
 * server restart, and dimension travel. Backed by nbt PersistentState and
 * entity NBT via mixins.
 *
 * <pre>
 *   // Define a typed key
 *   public static final TmDataKey&lt;Integer&gt; KILL_COUNT =
 *       TmDataKey.of(Identifier.of("mymod", "kill_count"), Integer.class, 0);
 *
 *   // Set on player
 *   TmCore.data().setPlayer(player, KILL_COUNT, 42);
 *
 *   // Get from player
 *   int kills = TmCore.data().getPlayer(player, KILL_COUNT); // returns 0 if absent
 *
 *   // Remove
 *   TmCore.data().removePlayer(player, KILL_COUNT);
 *
 *   // World-level data (survives restart)
 *   TmCore.data().setWorld(world, MY_KEY, value);
 *   Optional&lt;String&gt; val = TmCore.data().getWorldOpt(world, MY_STRING_KEY);
 * </pre>
 */
public interface TmDataAttachments {

    // -------------------------------------------------------------------------
    // Player attachments
    // -------------------------------------------------------------------------

    <T> void setPlayer(ServerPlayerEntity player, TmDataKey<T> key, T value);

    <T> T getPlayer(ServerPlayerEntity player, TmDataKey<T> key);

    <T> Optional<T> getPlayerOpt(ServerPlayerEntity player, TmDataKey<T> key);

    <T> void removePlayer(ServerPlayerEntity player, TmDataKey<T> key);

    boolean hasPlayer(ServerPlayerEntity player, TmDataKey<?> key);

    // -------------------------------------------------------------------------
    // Entity attachments (non-player)
    // -------------------------------------------------------------------------

    <T> void setEntity(Entity entity, TmDataKey<T> key, T value);

    <T> T getEntity(Entity entity, TmDataKey<T> key);

    <T> Optional<T> getEntityOpt(Entity entity, TmDataKey<T> key);

    <T> void removeEntity(Entity entity, TmDataKey<T> key);

    // -------------------------------------------------------------------------
    // World attachments (PersistentState-backed, survives restart)
    // -------------------------------------------------------------------------

    <T> void setWorld(ServerWorld world, TmDataKey<T> key, T value);

    <T> T getWorld(ServerWorld world, TmDataKey<T> key);

    <T> Optional<T> getWorldOpt(ServerWorld world, TmDataKey<T> key);

    <T> void removeWorld(ServerWorld world, TmDataKey<T> key);

    // -------------------------------------------------------------------------
    // Copy-on-death control
    // -------------------------------------------------------------------------

    /**
     * Marks a player data key to be preserved across death (respawn).
     * Must be called during initialization.
     */
    <T> void keepOnDeath(TmDataKey<T> key);

    /**
     * Returns {@code true} if this key is preserved across player death.
     */
    boolean isKeptOnDeath(TmDataKey<?> key);
}
