package com.gumillea.inlandport.core.util.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class IPUtil {

    public static ResourceLocation loc(String modId, String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static int secondsToTicks(float seconds) {
        return (int) (seconds * 20);
    }

    public static int toTicks(Object duration) {
        return duration instanceof Float seconds ? IPUtil.secondsToTicks(seconds) : (int) duration;
    }

    public static boolean random(Level level, double chance) {
        return level.getRandom().nextFloat() <= chance;
    }

    public static boolean tickRandom(Level level, int tick, double chance) {
        return level.getGameTime() % tick == 0 && random(level, chance);
    }

    public static AABB box(double x, double y, double z) {
        return new AABB(-x, -y, -z, x, y, z);
    }

    public static AABB box(double range) {
        return box(range, range, range);
    }

    public static AABB entityBox(Entity entity, double x, double y, double z) {
        return entity.getBoundingBox().inflate(x, y, z);
    }

}
