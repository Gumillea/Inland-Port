package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegUtil {
    public static ResourceLocation key(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ResourceLocation key(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    public static String path(Item item) {
        return key(item).getPath();
    }

    public static ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String path(Block block) {
        return key(block).getPath();
    }

    public static ResourceLocation key(MobEffect effect) {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect);
    }

    public static String path(MobEffect effect) {
        return key(effect).getPath();
    }

    public static ResourceLocation key(Potion potion) {
        return BuiltInRegistries.POTION.getKey(potion);
    }

    public static String path(Potion potion) {
        return key(potion).getPath();
    }

    public static ResourceLocation key(FluidType fluidType) {
        return NeoForgeRegistries.FLUID_TYPES.getKey(fluidType);
    }

    public static ResourceLocation key(Attribute attribute) {
        return BuiltInRegistries.ATTRIBUTE.getKey(attribute);
    }

    public static String path(Attribute attribute) {
        return key(attribute).getNamespace() + "." + key(attribute).getPath();
    }

    public static ResourceLocation key(SoundEvent event) {
        return BuiltInRegistries.SOUND_EVENT.getKey(event);
    }

    public static String path(SoundEvent event) {
        return key(event).getNamespace() + ":" + key(event).getPath();
    }

    public static ResourceLocation key(CreativeModeTab tab) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
    }

    public static String path(CreativeModeTab tab) {
        return key(tab).getPath();
    }

    public static String getSuffix(String path) {
        return path.contains("_") ? path.substring(path.lastIndexOf('_')) : "";
    }

    public static String getItemSuffix(Item item) {
        return getSuffix(path(item));
    }

    public static String getBlockSuffix(Block block) {
        return getItemSuffix(block.asItem());
    }

    public static String getBaseName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    public static boolean isVanilla (ResourceLocation location) {
        return location.getNamespace().equals("minecraft");
    }

    public static boolean isBlock (Item item) {
        return item instanceof BlockItem;
    }

    public static ItemStack stack(Item item) {
        return new ItemStack(item);
    }

}
