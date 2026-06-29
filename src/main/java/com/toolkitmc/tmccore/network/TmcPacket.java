package com.toolkitmc.tmccore.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class TmcPacket {

    public static final Identifier CHANNEL = Identifier.of("tmccore", "main");

    public record Packet(String data) implements CustomPayload {
        public static final CustomPayload.Id<Packet> ID = new CustomPayload.Id<>(CHANNEL);
        public static final PacketCodec<RegistryByteBuf, Packet> CODEC = PacketCodec.of(
            (value, buf) -> buf.writeString(value.data),
            buf -> new Packet(buf.readString())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(Packet.ID, Packet.CODEC);
        PayloadTypeRegistry.playS2C().register(Packet.ID, Packet.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(Packet.ID, (payload, context) -> {
            // Server tarafı paket işleme
        });

        ClientPlayNetworking.registerGlobalReceiver(Packet.ID, (payload, context) -> {
            // Client tarafı paket işleme
        });
    }

    public static void sendToServer(String data) {
        ClientPlayNetworking.send(new Packet(data));
    }

    public static void sendToPlayer(net.minecraft.server.network.ServerPlayerEntity player, String data) {
        ServerPlayNetworking.send(player, new Packet(data));
    }
}