package com.toolkitmc.tmccore.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class TmcEffects {

    public static void spawnParticles(ServerWorld world, BlockPos pos, ParticleEffect effect, int count) {
        world.spawnParticles(effect, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, count, 0.3, 0.3, 0.3, 0.01);
    }

    public static void spawnEndRod(ServerWorld world, BlockPos pos, int count) {
        spawnParticles(world, pos, ParticleTypes.END_ROD, count);
    }

    public static void playSound(ServerWorld world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.MASTER, 1f, 1f);
    }

    public static void playLevelUp(ServerWorld world, BlockPos pos) {
        playSound(world, pos, SoundEvents.ENTITY_PLAYER_LEVELUP);
    }

    public static void playNote(ServerWorld world, BlockPos pos) {
        playSound(world, pos, SoundEvents.BLOCK_NOTE_BLOCK_PLING.value());
    }
}