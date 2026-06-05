package com.toolkitmc.tmccore.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class TmcNetwork {
    public static final Identifier TMC_CHANNEL = Identifier.of("tmccore", "main");

    public static void init() {
        // Example payload registration
        PayloadTypeRegistry.playC2S().register(TmcPayload.ID, TmcPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TmcPayload.ID, TmcPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TmcPayload.ID, (payload, context) -> {
            // Handle server-side packet
        });

        ClientPlayNetworking.registerGlobalReceiver(TmcPayload.ID, (payload, context) -> {
            // Handle client-side packet
        });
    }

    public record TmcPayload(String data) implements CustomPayload {
        public static final CustomPayload.Id<TmcPayload> ID = new CustomPayload.Id<>(TMC_CHANNEL);
        public static final PacketCodec<RegistryByteBuf, TmcPayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeString(value.data),
                buf -> new TmcPayload(buf.readString())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}