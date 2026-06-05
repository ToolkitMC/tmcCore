package com.toolkitmc.tmccore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TmcCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmc")
            .then(CommandManager.literal("info")
                .executes(TmcCommand::info))
            .then(CommandManager.argument("message", StringArgumentType.greedyString())
                .executes(TmcCommand::sendMessage))
        );
    }

    private static int info(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(() -> Text.literal("tmcCore v1.0.0-alpha - ToolkitMC Library"), false);
        return 1;
    }

    private static int sendMessage(CommandContext<ServerCommandSource> ctx) {
        String msg = StringArgumentType.getString(ctx, "message");
        ctx.getSource().sendFeedback(() -> Text.literal("[tmcCore] " + msg), false);
        return 1;
    }
}