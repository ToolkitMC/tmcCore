package com.toolkitmc.core.api.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * S2C / C2S packet abstraction for ToolkitMC mods.
 *
 * <p>Eliminates boilerplate around Fabric's networking API.
 * Channels are auto-registered on both sides when declared.
 *
 * <pre>
 *   // Define channel ID
 *   public static final Identifier MY_CHANNEL = Identifier.of("mymod", "my_packet");
 *
 *   // Register S2C handler on client (in ClientModInitializer)
 *   TmCore.networking().registerClientReceiver(MY_CHANNEL, (buf, responseSender) -> {
 *       String message = buf.readString();
 *       // handle on client
 *   });
 *
 *   // Send S2C from server
 *   TmCore.networking().sendToPlayer(player, MY_CHANNEL, buf -> buf.writeString("hello"));
 *
 *   // Register C2S handler on server (in ModInitializer)
 *   TmCore.networking().registerServerReceiver(MY_CHANNEL, (player, buf, sender) -> {
 *       String data = buf.readString();
 *       // handle on server
 *   });
 *
 *   // Send C2S from client
 *   TmCore.networking().sendToServer(MY_CHANNEL, buf -> buf.writeInt(42));
 * </pre>
 */
public interface TmNetworking {

    // -------------------------------------------------------------------------
    // Server-side: receive C2S packets
    // -------------------------------------------------------------------------

    /**
     * Registers a C2S (client → server) packet handler.
     * Must be called server-side during initialization.
     */
    void registerServerReceiver(Identifier channel, ServerPacketHandler handler);

    // -------------------------------------------------------------------------
    // Client-side: receive S2C packets
    // -------------------------------------------------------------------------

    /**
     * Registers an S2C (server → client) packet handler.
     * Must be called client-side (ClientModInitializer).
     */
    void registerClientReceiver(Identifier channel, ClientPacketHandler handler);

    // -------------------------------------------------------------------------
    // Sending
    // -------------------------------------------------------------------------

    /**
     * Sends an S2C packet to a single player.
     */
    void sendToPlayer(ServerPlayerEntity player, Identifier channel, PacketWriter writer);

    /**
     * Sends an S2C packet to all players on the server.
     */
    void sendToAll(MinecraftServer server, Identifier channel, PacketWriter writer);

    /**
     * Sends an S2C packet to a collection of players.
     */
    void sendToPlayers(Collection<ServerPlayerEntity> players, Identifier channel, PacketWriter writer);

    /**
     * Sends an S2C packet to all players tracking the given entity.
     */
    void sendToTracking(net.minecraft.entity.Entity entity, Identifier channel, PacketWriter writer);

    /**
     * Sends a C2S packet from client to server.
     * Only callable on the client side.
     */
    void sendToServer(Identifier channel, PacketWriter writer);

    // -------------------------------------------------------------------------
    // Inner types
    // -------------------------------------------------------------------------

    /** Writes data to a {@link PacketByteBuf}. */
    @FunctionalInterface
    interface PacketWriter extends java.util.function.Consumer<PacketByteBuf> {}

    /** Handles a C2S packet on the server. */
    @FunctionalInterface
    interface ServerPacketHandler {
        void receive(ServerPlayerEntity player, PacketByteBuf buf, PacketSender responseSender);
    }

    /** Handles an S2C packet on the client. */
    @FunctionalInterface
    interface ClientPacketHandler {
        void receive(PacketByteBuf buf, PacketSender responseSender);
    }
}
