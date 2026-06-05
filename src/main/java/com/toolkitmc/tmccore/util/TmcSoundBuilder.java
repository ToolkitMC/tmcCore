package com.toolkitmc.tmccore.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class TmcSoundBuilder {

    public static void play(ServerWorld world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.MASTER, 1f, 1f);
    }

    public static void playWithPitch(ServerWorld world, BlockPos pos, SoundEvent sound, float pitch) {
        world.playSound(null, pos, sound, SoundCategory.MASTER, 1f, pitch);
    }
}