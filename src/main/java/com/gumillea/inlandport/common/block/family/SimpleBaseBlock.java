package com.gumillea.inlandport.common.block.family;

import com.gumillea.inlandport.core.util.utils.CompatUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleBaseBlock extends Block {

    public final String name;
    public final String modId;

    public SimpleBaseBlock(Properties properties, String modId, String name) {
        super(properties);
        this.name = name;
        this.modId = modId;
    }

    public Block getVariant(Variant variant) {
        return CompatUtil.block(modId, variant.getPrefix() + name.replace("_planks", "").replace("_ore", "") + variant.getSuffix());
    }

    public Item getItemVariant(Variant variant) {
        return CompatUtil.item(modId, variant.getPrefix() + name.replace("_planks", "").replace("_ore", "") + variant.getSuffix());
    }

    public Block getBaseBlock() {
        return getVariant(Variant.BASE);
    }

    public Block getChiseled() {
        return getVariant(Variant.CHISELED_BLOCK);
    }

    public Block getStairs() {
        return getVariant(Variant.STAIRS);
    }

    public Block getSlab() {
        return getVariant(Variant.SLAB);
    }

    public Block getWall() {
        return getVariant(Variant.WALL);
    }

    public static final Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> REG_MAP;

    static {
        Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> map = new EnumMap<>(Variant.class);

        map.put(Variant.CHISELED_BLOCK, (base, props) -> new Block(props));
        map.put(Variant.STAIRS, (base, props) -> new StairBlock(base.get().defaultBlockState(), props));
        map.put(Variant.SLAB, (base, props) -> new SlabBlock(props));
        map.put(Variant.WALL, (base, props) -> new WallBlock(props));

        REG_MAP = Collections.unmodifiableMap(map);
    }



}


