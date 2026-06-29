package com.toolkitmc.core.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

/**
 * Brigadier command registration wrapper for ToolkitMC mods.
 *
 * <p>Collects all command registrations and registers them in a single
 * Fabric callback pass, avoiding ordering issues.
 *
 * <pre>
 *   // Register during onInitialize (before SERVER_STARTED)
 *   TmCore.commands().register(
 *       Identifier.of("mymod", "mycommand"),
 *       CommandManager.literal("mycommand")
 *           .requires(src -> src.hasPermissionLevel(2))
 *           .then(CommandManager.literal("reload")
 *               .executes(ctx -> {
 *                   TmCore.config().reload("mymod");
 *                   ctx.getSource().sendFeedback(() -> Text.literal("Config reloaded."), false);
 *                   return 1;
 *               }))
 *   );
 * </pre>
 */
public interface TmCommandRegistry {

    /**
     * Queues a command for registration.
     *
     * @param id      unique identifier for this command entry (for logging/dedup)
     * @param command the root literal builder
     * @throws IllegalStateException if called after command registration is closed
     */
    void register(Identifier id, LiteralArgumentBuilder<ServerCommandSource> command);

    /**
     * Overload for simple string-keyed registration under a mod namespace.
     */
    default void register(String namespace, String key,
                          LiteralArgumentBuilder<ServerCommandSource> command) {
        register(Identifier.of(namespace, key), command);
    }

    /**
     * Returns {@code true} if the given id was already registered.
     */
    boolean isRegistered(Identifier id);

    /**
     * Returns the count of registered commands.
     */
    int registeredCount();
}
