package com.toolkitmc.tmccore.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class TmcUtils {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String vecToString(Vec3d vec) {
        return vec.x + "," + vec.y + "," + vec.z;
    }

    public static NbtCompound createNbt(String key, String value) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(key, value);
        return nbt;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}