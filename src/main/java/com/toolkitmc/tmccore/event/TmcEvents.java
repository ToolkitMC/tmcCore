package com.toolkitmc.tmccore.event;

public class TmcEvents {
    public static class ConfigReloaded {
        public final String configName;
        public ConfigReloaded(String configName) { this.configName = configName; }
    }

    public static class PlayerJoinServer {
        public final net.minecraft.server.network.ServerPlayerEntity player;
        public PlayerJoinServer(net.minecraft.server.network.ServerPlayerEntity player) { this.player = player; }
    }
}