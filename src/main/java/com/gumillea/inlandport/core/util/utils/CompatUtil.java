package com.gumillea.inlandport.core.util.utils;

import com.gumillea.inlandport.core.util.IPCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class CompatUtil {
    public static boolean mod(String mod) {
        return ModList.get().isLoaded(mod);
    }

    public static Block block(String modId, String path) {
        Block block = BuiltInRegistries.BLOCK.get(IPUtil.loc(modId, path));
        return block == Blocks.AIR ? null : block;
    }

    public static Fluid fluid(String modId, String path) {
        return BuiltInRegistries.FLUID.get(IPUtil.loc(modId, path));
    }

    public static Fluid getFluid(FluidType type) {
        ResourceLocation key = RegUtil.key(type);
        return fluid(key.getNamespace(), key.getPath());
    }

    public static Item item(String modId, String path) {
        Item item = BuiltInRegistries.ITEM.get(IPUtil.loc(modId, path));
        return item == Blocks.AIR.asItem() ? null : item;
    }

    public static Item fDItem(String path) {
        return item(IPCompat.FD, path);
    }

    public static Holder<MobEffect> effectHolder(String modId, String path) {
        return BuiltInRegistries.MOB_EFFECT.getHolder(IPUtil.loc(modId, path)).orElse(null);
    }

    public static MobEffect effect(String modId, String path) {
        Holder<MobEffect> holder = effectHolder(modId, path);
        return holder != null ? holder.value() : null;
    }

    public static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registry, @Nullable String modId, String name) {
        modId = modId == null ? "c" : modId;
        return TagKey.create(registry, IPUtil.loc(modId, name));
    }

    public static TagKey<Item> itemTag(String modId, String name) {
        return tag(Registries.ITEM, modId, name);
    }

    public static TagKey<Item> itemTag(String name) {
        return itemTag(null, name);
    }

    public static TagKey<Block> blockTag(String modId, String name) {
        return tag(Registries.BLOCK, modId, name);
    }

    public static TagKey<Block> blockTag(String name) {
        return blockTag(null, name);
    }

    public static TagKey<MobEffect> effectTag(String modId, String name) {
        return tag(Registries.MOB_EFFECT, modId, name);
    }

    public static TagKey<MobEffect> effectTag(String name) {
        return effectTag(null, name);
    }

    public static TagKey<Biome> biomeTag(String modId, String name) {
        return tag(Registries.BIOME, modId, name);
    }

    public static TagKey<Biome> biomeTag(String name) {
        return biomeTag(null, name);
    }

    public static TagKey<DamageType> damageTypeTag(String modId, String name) {
        return tag(Registries.DAMAGE_TYPE, modId, name);
    }

    public static TagKey<DamageType> damageTypeTag(String name) {
        return damageTypeTag(null, name);
    }

    public static <T> boolean isEmpty(Registry<T> registry, TagKey<T> tagKey) {
        return registry.getTag(tagKey).map(tag -> tag.size() == 0).orElse(true);
    }

    public static boolean isBlockTagEmpty(TagKey<Block> tagKey) {
        return isEmpty(BuiltInRegistries.BLOCK, tagKey);
    }

    public static boolean isItemTagEmpty(TagKey<Item> tagKey) {
        return isEmpty(BuiltInRegistries.ITEM, tagKey);
    }

    public static boolean isPresent(ResourceLocation location) {
        return Minecraft.getInstance().getResourceManager().getResource(location).isPresent();
    }

    public static BlockSetType getBlockSetType(String modId, String baseName) {
        return BlockSetType.register(new BlockSetType(modId + ":" + baseName));
    }

    public static WoodType getWoodType(String modId, String baseName) {
        return WoodType.register(new WoodType(modId + ":" + baseName, getBlockSetType(modId, baseName)));
    }

}
