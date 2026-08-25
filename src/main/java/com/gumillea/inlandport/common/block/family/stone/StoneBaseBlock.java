package com.gumillea.inlandport.common.block.family.stone;

import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.gumillea.inlandport.core.util.utils.VariantUtil.*;

public class StoneBaseBlock extends SimpleBaseBlock {

    public StoneBaseBlock(Properties properties, String modId, String name) {
        super(properties, modId, name);
    }

    public Block getPillar() {
        return getVariant(Variant.PILLAR);
    }

    public Block getBricks() {
        return getVariant(Variant.BRICKS);
    }

    public Block getBrickStairs() {
        return getVariant(Variant.BRICK_STAIRS);
    }

    public Block getBrickSlab() {
        return getVariant(Variant.BRICK_SLAB);
    }

    public Block getBrickWall() {
        return getVariant(Variant.BRICK_WALL);
    }

    public Block getCrackedBricks() {
        return getVariant(Variant.CRACKED_BRICKS);
    }

    public Block getTiles() {
        return getVariant(Variant.TILES);
    }

    public Block getTileStairs() {
        return getVariant(Variant.TILE_STAIRS);
    }

    public Block getTileSlab() {
        return getVariant(Variant.TILE_SLAB);
    }

    public Block getTileWall() {
        return getVariant(Variant.TILE_WALL);
    }

    public Block getCrackedTiles() {
        return getVariant(Variant.CRACKED_TILES);
    }

    public Block getPolishedBlock() {
        return getVariant(Variant.POLISHED_BLOCK);
    }

    public Block getPolishedStairs() {
        return getVariant(Variant.POLISHED_STAIRS);
    }

    public Block getPolishedSlab() {
        return getVariant(Variant.POLISHED_SLAB);
    }

    public Block getPolishedWall() {
        return getVariant(Variant.POLISHED_WALL);
    }

    public Block getMossyBlock() {
        return getVariant(Variant.MOSSY_BLOCK);
    }

    public Block getMossyStairs() {
        return getVariant(Variant.MOSSY_STAIRS);
    }

    public Block getMossySlab() {
        return getVariant(Variant.MOSSY_SLAB);
    }

    public Block getMossyWall() {
        return getVariant(Variant.MOSSY_WALL);
    }

    public Block getMossyBricks() {
        return getVariant(Variant.MOSSY_BRICKS);
    }

    public Block getMossyBrickStairs() {
        return getVariant(Variant.MOSSY_BRICK_STAIRS);
    }

    public Block getMossyBrickSlab() {
        return getVariant(Variant.MOSSY_BRICK_SLAB);
    }

    public Block getMossyBrickWall() {
        return getVariant(Variant.MOSSY_BRICK_WALL);
    }

    public static final Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> REG_MAP;

    static {
        Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> map = new EnumMap<>(Variant.class);

        for (Variant variant : Variant.values()) {
            if (!Variant.isStone(variant)) continue;
            switch (variant.getBaseSuffix()) {
                case Suffixes.SLAB ->  map.put(variant, (base, props) -> new SlabBlock(props));
                case Suffixes.STAIRS ->  map.put(variant, (base, props) -> new StairBlock(base.get().defaultBlockState(), props));
                case Suffixes.WALL -> map.put(variant, (base, props) -> new WallBlock(props));
                case Suffixes.PILLAR -> map.put(variant, (base, props) -> new RotatedPillarBlock(props));
                default -> map.put(variant, (base, props) -> new Block(props));
            }
        }

        REG_MAP = Collections.unmodifiableMap(map);
    }

}
