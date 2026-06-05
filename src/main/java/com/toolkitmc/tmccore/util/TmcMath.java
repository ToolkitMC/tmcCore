package com.toolkitmc.tmccore.util;

import net.minecraft.util.math.Vec3d;

public class TmcMath {

    public static double distance(Vec3d a, Vec3d b) {
        return a.distanceTo(b);
    }

    public static double distance2D(Vec3d a, Vec3d b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.z - b.z, 2));
    }

    public static Vec3d lerp(Vec3d start, Vec3d end, double t) {
        return start.lerp(end, t);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
}