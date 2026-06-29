package com.toolkitmc.tmccore.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TmcParticleBuilder {

    public static void spawn(ServerWorld world, BlockPos pos, ParticleEffect effect, int count) {
        world.spawnParticles(effect, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, count, 0.5, 0.5, 0.5, 0.02);
    }

    public static void spawnExplosion(ServerWorld world, BlockPos pos) {
        spawn(world, pos, ParticleTypes.EXPLOSION, 20);
    }

    public static void spawnSmoke(ServerWorld world, BlockPos pos, int count) {
        spawn(world, pos, ParticleTypes.SMOKE, count);
    }
}