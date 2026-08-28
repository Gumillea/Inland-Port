package com.gumillea.inlandport.common.block.family;

import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.gumillea.inlandport.core.util.utils.VariantUtil.*;

public enum Variant {
    BASE(null, null, null),

    CHISELED_BLOCK(Prefixes.CHISELED, null, Types.STONE_OR_MINERAL),
    STAIRS(null, "_stairs", null),
    SLAB(null, "_slab", null),
    WALL(null, "_wall", null),

    MOSSY_BLOCK(Prefixes.MOSSY, null, Types.STONE),
    MOSSY_STAIRS(Prefixes.MOSSY, "_stairs", Types.STONE),
    MOSSY_SLAB(Prefixes.MOSSY, "_slab", Types.STONE),
    MOSSY_WALL(Prefixes.MOSSY, "_wall", Types.STONE),

    BRICKS(null, "_bricks", Types.STONE_OR_MINERAL),
    BRICK_STAIRS(null, "_brick_stairs", Types.STONE_OR_MINERAL),
    BRICK_SLAB(null, "_brick_slab", Types.STONE_OR_MINERAL),
    BRICK_WALL(null, "_brick_wall", Types.STONE_OR_MINERAL),
    CRACKED_BRICKS("cracked_", "_bricks", Types.STONE_OR_MINERAL),

    MOSSY_BRICKS (Prefixes.MOSSY, "_bricks", Types.STONE),
    MOSSY_BRICK_STAIRS(Prefixes.MOSSY, "_brick_stairs", Types.STONE),
    MOSSY_BRICK_SLAB(Prefixes.MOSSY, "_brick_slab", Types.STONE),
    MOSSY_BRICK_WALL(Prefixes.MOSSY, "_brick_wall", Types.STONE),

    TILES(null, "_tiles", Types.STONE_OR_MINERAL),
    TILE_STAIRS(null, "_tile_stairs", Types.STONE_OR_MINERAL),
    TILE_SLAB(null, "_tile_slab", Types.STONE_OR_MINERAL),
    TILE_WALL(null, "_tile_wall", Types.STONE_OR_MINERAL),
    CRACKED_TILES("cracked_", "_tiles", Types.STONE_OR_MINERAL),

    POLISHED_BLOCK(Prefixes.POLISHED, null, Types.STONE),
    POLISHED_STAIRS(Prefixes.POLISHED, "_stairs", Types.STONE),
    POLISHED_SLAB(Prefixes.POLISHED, "_slab", Types.STONE),
    POLISHED_WALL(Prefixes.POLISHED, "_wall", Types.STONE),

    PILLAR(null, "_pillar", Types.STONE_OR_MINERAL),
    TOTEM(null, "_totem", Types.WOODEN),

    LOG(null, "_log", Types.WOODEN),
    STRIPPED_LOG("stripped_", "_log", Types.WOODEN),
    WOOD(null, "_wood", Types.WOODEN),
    STRIPPED_WOOD("stripped_", "_wood", Types.WOODEN),

    LEAVES(null, "_leaves", Types.WOODEN),

    BUTTON(null, "_button", Types.WOODEN_OR_MINERAL),
    PRESSURE_PLATE(null, "_pressure_plate", Types.WOODEN_OR_MINERAL),

    DOOR(null, "_door", Types.WOODEN_OR_MINERAL),
    TRAPDOOR(null, "_trapdoor", Types.WOODEN_OR_MINERAL),

    STANDING_SIGN(null, "_sign", Types.WOODEN),
    WALL_SIGN(null, "_wall_sign", Types.WOODEN),

    HANGING_SIGN(null, "_hanging_sign", Types.WOODEN),
    HANGING_WALL_SIGN(null, "_hanging_wall_sign", Types.WOODEN),

    FENCE(null, "_fence", Types.WOODEN),
    FENCE_GATE(null, "_fence_gate", Types.WOODEN),

    BOAT(null, "_boat", Types.WOODEN),
    CHEST_BOAT(null, "_chest_boat", Types.WOODEN),

    RAW_ORE("raw_", null, Types.MINERAL),
    INGOT(null, "_ingot", Types.MINERAL),
    GEM(null, null, Types.MINERAL),
    NUGGET(null, "_nugget", Types.MINERAL),

    AXE(null, "_axe", Types.MINERAL),
    PICKAXE(null, "_pickaxe", Types.MINERAL),
    HOE(null, "_hoe", Types.MINERAL),
    SHOVEL(null, "_shovel", Types.MINERAL),
    SWORD(null, "_sword", Types.MINERAL),

    HELMET(null, "_helmet", Types.MINERAL),
    CHESTPLATE(null, "_chestplate", Types.MINERAL),
    LEGGINGS(null, "_leggings", Types.MINERAL),
    BOOTS(null, "_boots", Types.MINERAL),

    SHEARS(null, "_shears", Types.MINERAL);

    private final String prefix;
    private final String suffix;
    private final String[] types;

    Variant(@Nullable String prefix, @Nullable String suffix, @Nullable String[] types) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.types = types;
    }
    
    public String getPrefix() {
        return prefix == null ? "" : prefix;
    }

    public String getSuffix() {
        return suffix == null ? "" : suffix;
    }

    public String getBaseSuffix() {
        return getSuffix().replace("_brick", "").replace("_title", "");
    }


    public Set<String> getTypes() {
        return types == null ? Collections.emptySet() : Arrays.stream(types).collect(Collectors.toSet());
    }

    public static Block get(Block block, Variant variant) {
        ResourceLocation location = RegUtil.key(block);
        return CompatUtil.block(location.getNamespace(), variant.getPrefix() + location.getPath().replace("_planks", "").replace("_ore", "") + variant.getSuffix());
    }

    public static Item get(Item block, Variant variant) {
        ResourceLocation location = RegUtil.key(block);
        return CompatUtil.item(location.getNamespace(), variant.getPrefix() + location.getPath().replace("_planks", "").replace("_ore", "") + variant.getSuffix());
    }

    public static EnumSet<Variant> all() {
        return EnumSet.allOf(Variant.class);
    }

    public static EnumSet<Variant> only(Variant... variants) {
        EnumSet<Variant> set = EnumSet.of(BASE);
        set.addAll(EnumSet.of(variants[0], variants));

        return set;
    }

    public static EnumSet<Variant> except(Object... excludes) {
        EnumSet<Variant> set = EnumSet.allOf(Variant.class);
        for (Object exclude : excludes) {
            if (exclude instanceof Variant variant) {
                set.remove(variant);
            } else if (exclude instanceof String string) {
                for (Variant variant : Variant.values()) {
                    if (string.endsWith("_") && variant.getPrefix().equals(string) || isRemovableSuffix(string, variant)) {
                        set.remove(variant);
                    }
                }
            }
        }
        set.add(BASE);
        return set;
    }

    private static boolean isRemovableSuffix(String string, Variant variant) {
        String suffix = variant.getSuffix();
        if (!string.startsWith("_")) return false;
        if (suffix.equals(string)) return true;

        return suffix.startsWith(string.replaceFirst("s$", "") + "_");
    }

    public static List<Block> getAllBlocks(Block baseBlock, Map<Variant, ?> map) {
        List<Block> blocks = new ArrayList<>();

        for (Variant variant : map.keySet()) {
            Block block = get(baseBlock, variant);
            if(block != null) blocks.add(block);
        }

        return Collections.unmodifiableList(blocks);
    }

    public static List<Item> getAllItems(Block baseBlock, Map<Variant, ?> map) {
        return getAllBlocks(baseBlock, map).stream().map(Block::asItem).toList();
    }

    public static boolean isGeneric(Variant variant) {
        return variant.getTypes().isEmpty();
    }

    public static boolean isStone(Variant variant) {
        return variant.getTypes().contains("stone") || isGeneric(variant);
    }

    public static boolean isWooden(Variant variant) {
        return variant.getTypes().contains("wooden") || isGeneric(variant);
    }
}