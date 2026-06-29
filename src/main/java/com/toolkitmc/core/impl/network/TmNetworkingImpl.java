package com.toolkitmc.core.impl.network;

import com.toolkitmc.core.TmCore;
import com.toolkitmc.core.api.network.TmNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;

import java.util.*;

/**
 * TmNetworking implementation for Fabric 1.21.8+.
 *
 * <p>The old Identifier + PacketByteBuf API was removed in 1.21.
 * All networking now requires {@link CustomPayload} types with explicit codecs
 * registered in {@link PayloadTypeRegistry}.
 *
 * <p>We wrap each channel in a per-channel {@link RawPayload} so that callers
 * keep the existing PacketWriter / handler abstraction unchanged.
 */
public final class TmNetworkingImpl implements TmNetworking {

    // Deferred client handlers — stored here, applied in TmCoreClient.onInitializeClient()
    private final Map<Identifier, ClientPacketHandler> pendingClientReceivers = new LinkedHashMap<>();

    // Track which channels have already been registered in PayloadTypeRegistry
    // to avoid duplicate registration across server/client calls.
    private final Set<Identifier> registeredPayloadTypes = new HashSet<>();

    // -------------------------------------------------------------------------
    // Per-channel RawPayload  (one record class per channel isn't possible at
    // runtime, so we use a shared record keyed by its CustomPayload.Id)
    // -------------------------------------------------------------------------

    private record RawPayload(CustomPayload.Id<RawPayload> id, byte[] data)
            implements CustomPayload {

        @Override
        public CustomPayload.Id<RawPayload> getId() { return id; }
    }

    /** Returns a {@link PacketCodec} for a given channel payload id. */
    private static PacketCodec<PacketByteBuf, RawPayload> codecFor(CustomPayload.Id<RawPayload> id) {
        return new PacketCodec<>() {
            @Override
            public RawPayload decode(PacketByteBuf buf) {
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                return new RawPayload(id, data);
            }

            @Override
            public void encode(PacketByteBuf buf, RawPayload value) {
                buf.writeBytes(value.data());
            }
        };
    }

    // -------------------------------------------------------------------------
    // Server-side (C2S) receiver registration
    // -------------------------------------------------------------------------

    @Override
    public void registerServerReceiver(Identifier channel, ServerPacketHandler handler) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(handler, "handler");

        CustomPayload.Id<RawPayload> payloadId = new CustomPayload.Id<>(channel);

        // Register the payload type for C2S if not already done
        if (registeredPayloadTypes.add(channel)) {
            PayloadTypeRegistry.playC2S().register(payloadId, codecFor(payloadId));
        }

        ServerPlayNetworking.registerGlobalReceiver(payloadId,
            (payload, context) -> {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()));
                handler.receive(context.player(), buf, context.responseSender());
            }
        );
        TmCore.LOGGER.debug("C2S receiver registered: {}", channel);
    }

    // -------------------------------------------------------------------------
    // Client-side (S2C) receiver registration (deferred)
    // -------------------------------------------------------------------------

    @Override
    public void registerClientReceiver(Identifier channel, ClientPacketHandler handler) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(handler, "handler");
        pendingClientReceivers.put(channel, handler);
    }

    /**
     * Called by TmCoreClient to apply deferred client receivers.
     * S2C payload types must be registered via {@link PayloadTypeRegistry#playS2C()}.
     */
    public void applyClientReceivers() {
        for (Map.Entry<Identifier, ClientPacketHandler> entry : pendingClientReceivers.entrySet()) {
            Identifier channel = entry.getKey();
            ClientPacketHandler handler = entry.getValue();

            CustomPayload.Id<RawPayload> payloadId = new CustomPayload.Id<>(channel);

            // Register the payload type for S2C if not already done
            if (registeredPayloadTypes.add(channel)) {
                PayloadTypeRegistry.playS2C().register(payloadId, codecFor(payloadId));
            }

            ClientPlayNetworking.registerGlobalReceiver(payloadId,
                (payload, context) -> {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()));
                    handler.receive(buf, context.responseSender());
                }
            );
            TmCore.LOGGER.debug("S2C receiver registered (client): {}", channel);
        }
        pendingClientReceivers.clear();
    }

    // -------------------------------------------------------------------------
    // Sending
    // -------------------------------------------------------------------------

    @Override
    public void sendToPlayer(ServerPlayerEntity player, Identifier channel, PacketWriter writer) {
        ServerPlayNetworking.send(player, buildPayload(channel, writer));
    }

    @Override
    public void sendToAll(MinecraftServer server, Identifier channel, PacketWriter writer) {
        RawPayload payload = buildPayload(channel, writer);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public void sendToPlayers(Collection<ServerPlayerEntity> players, Identifier channel, PacketWriter writer) {
        RawPayload payload = buildPayload(channel, writer);
        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public void sendToTracking(Entity entity, Identifier channel, PacketWriter writer) {
        sendToPlayers(PlayerLookup.tracking(entity), channel, writer);
    }

    @Override
    public void sendToServer(Identifier channel, PacketWriter writer) {
        ClientPlayNetworking.send(buildPayload(channel, writer));
    }

    /** Called by TmCore.onInitialize. */
    public void registerChannels() {
        TmCore.LOGGER.debug("TmNetworking initialized.");
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    private static RawPayload buildPayload(Identifier channel, PacketWriter writer) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        writer.accept(buf);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        return new RawPayload(new CustomPayload.Id<>(channel), data);
    }
}
