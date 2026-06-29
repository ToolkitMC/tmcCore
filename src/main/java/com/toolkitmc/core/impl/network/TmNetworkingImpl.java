package com.toolkitmc.core.impl.network;

import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.network.TmNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public final class TmNetworkingImpl implements TmNetworking {

    // Deferred client handlers — stored here, applied in TmCoreClient
    private final Map<Identifier, ClientPacketHandler> pendingClientReceivers = new LinkedHashMap<>();

    @Override
    public void registerServerReceiver(Identifier channel, ServerPacketHandler handler) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(handler, "handler");

        ServerPlayNetworking.registerGlobalReceiver(channel,
            (server, player, networkHandler, buf, responseSender) ->
                handler.receive(player, buf, responseSender)
        );
        TmCore.LOGGER.debug("C2S receiver registered: {}", channel);
    }

    @Override
    public void registerClientReceiver(Identifier channel, ClientPacketHandler handler) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(handler, "handler");
        // Client registration is deferred to TmCoreClient.onInitializeClient()
        pendingClientReceivers.put(channel, handler);
    }

    /** Called by TmCoreClient to apply deferred client receivers. */
    public void applyClientReceivers() {
        for (Map.Entry<Identifier, ClientPacketHandler> entry : pendingClientReceivers.entrySet()) {
            ClientPlayNetworking.registerGlobalReceiver(entry.getKey(),
                (client, networkHandler, buf, responseSender) ->
                    entry.getValue().receive(buf, responseSender)
            );
            TmCore.LOGGER.debug("S2C receiver registered (client): {}", entry.getKey());
        }
        pendingClientReceivers.clear();
    }

    @Override
    public void sendToPlayer(ServerPlayerEntity player, Identifier channel, PacketWriter writer) {
        PacketByteBuf buf = PacketByteBufs.create();
        writer.accept(buf);
        ServerPlayNetworking.send(player, channel, buf);
    }

    @Override
    public void sendToAll(MinecraftServer server, Identifier channel, PacketWriter writer) {
        sendToPlayers(server.getPlayerManager().getPlayerList(), channel, writer);
    }

    @Override
    public void sendToPlayers(Collection<ServerPlayerEntity> players, Identifier channel, PacketWriter writer) {
        // Build buf once, reuse for all players
        PacketByteBuf buf = PacketByteBufs.create();
        writer.accept(buf);
        for (ServerPlayerEntity player : players) {
            // Each call needs its own buf reference — re-slice
            PacketByteBuf copy = PacketByteBufs.create();
            copy.writeBytes(buf.copy());
            ServerPlayNetworking.send(player, channel, copy);
        }
        buf.release();
    }

    @Override
    public void sendToTracking(Entity entity, Identifier channel, PacketWriter writer) {
        sendToPlayers(PlayerLookup.tracking(entity), channel, writer);
    }

    @Override
    public void sendToServer(Identifier channel, PacketWriter writer) {
        PacketByteBuf buf = PacketByteBufs.create();
        writer.accept(buf);
        ClientPlayNetworking.send(channel, buf);
    }

    /** Called by TmCore.onInitialize — nothing to register at server startup for networking. */
    public void registerChannels() {
        TmCore.LOGGER.debug("TmNetworking initialized.");
    }
}
