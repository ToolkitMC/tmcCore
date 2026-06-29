package com.toolkitmc.core.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.command.TmCommandRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.*;

public final class TmCommandRegistryImpl implements TmCommandRegistry {

    private final Map<Identifier, LiteralArgumentBuilder<ServerCommandSource>> pending = new LinkedHashMap<>();
    private boolean closed = false;

    @Override
    public void register(Identifier id, LiteralArgumentBuilder<ServerCommandSource> command) {
        if (closed) throw new IllegalStateException(
            "TmCommandRegistry is closed — cannot register '" + id + "' after command registration phase."
        );
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(command, "command");

        if (pending.containsKey(id)) {
            throw new IllegalArgumentException("Command already registered with id: " + id);
        }
        pending.put(id, command);
        TmCore.LOGGER.debug("Command queued: {}", id);
    }

    @Override
    public boolean isRegistered(Identifier id) {
        return pending.containsKey(id);
    }

    @Override
    public int registeredCount() {
        return pending.size();
    }

    /** Called by TmCore.onInitialize — registers Fabric callback. */
    public void registerFabricCallback() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (Map.Entry<Identifier, LiteralArgumentBuilder<ServerCommandSource>> entry : pending.entrySet()) {
                try {
                    dispatcher.register(entry.getValue());
                    TmCore.LOGGER.debug("Command registered: {}", entry.getKey());
                } catch (Exception e) {
                    TmCore.LOGGER.error("Failed to register command {}: {}", entry.getKey(), e.getMessage(), e);
                }
            }
            closed = true;
            TmCore.LOGGER.info("TmCommandRegistry: {} command(s) registered.", pending.size());
        });
    }
}
