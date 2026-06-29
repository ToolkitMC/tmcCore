package com.toolkitmc.tmccore.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class TmcRaycast {
    public static BlockHitResult raycast(PlayerEntity player, double distance) {
        return player.getWorld().raycast(new RaycastContext(
            player.getEyePos(),
            player.getEyePos().add(player.getRotationVector().multiply(distance)),
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            player
        ));
    }
}