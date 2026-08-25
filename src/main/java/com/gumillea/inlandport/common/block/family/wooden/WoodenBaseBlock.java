package com.gumillea.inlandport.common.block.family.wooden;

import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

public class WoodenBaseBlock extends SimpleBaseBlock {

    public WoodenBaseBlock(Properties properties, String modId, String name) {
        super(properties, modId, name);
    }

    public Block getLog() {
        return getVariant(Variant.LOG);
    }

    public Block getStrippedLog() {
        return getVariant(Variant.STRIPPED_LOG);
    }

    public Block getWood() {
        return getVariant(Variant.WOOD);
    }

    public Block getStrippedWood() {
        return getVariant(Variant.STRIPPED_WOOD);
    }

    public Block getButton() {
        return getVariant(Variant.BUTTON);
    }

    public Block getDoor() {
        return getVariant(Variant.DOOR);
    }

    public Block getFence() {
        return getVariant(Variant.FENCE);
    }

    public Block getFenceGate() {
        return getVariant(Variant.FENCE_GATE);
    }

    public Block getPressurePlate() {
        return getVariant(Variant.PRESSURE_PLATE);
    }

    public Block getTrapdoor() {
        return getVariant(Variant.TRAPDOOR);
    }

    public Item getSign() {
        return getItemVariant(Variant.STANDING_SIGN);
    }

    public Block getStandingSign() {
        return getVariant(Variant.STANDING_SIGN);
    }

    public Block getWallSign() {
        return getVariant(Variant.WALL_SIGN);
    }

    public Item getHangingSign() {
        return getItemVariant(Variant.HANGING_SIGN);
    }

    public Block getCeilingHangingSign() {
        return getVariant(Variant.HANGING_SIGN);
    }

    public Block getHangingWallSign() {
        return getVariant(Variant.HANGING_WALL_SIGN);
    }

    public Item getBoat() {
        return getItemVariant(Variant.BOAT);
    }

    public Item getChestBoat() {
        return getItemVariant(Variant.CHEST_BOAT);
    }

    public TagKey<Block> getLogsBlockTag() {
       return TagKey.create(Registries.BLOCK, IPUtil.loc(modId, RegUtil.path(getLog()) + "s"));
    }

    public TagKey<Item> getLogsItemTag() {
        return TagKey.create(Registries.ITEM, IPUtil.loc(modId, RegUtil.path(getLog()) + "s"));
    }

    public static final Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> REG_MAP;

    static {
        Map<Variant, BiFunction<DeferredHolder<Block, Block>, Properties, Block>> map = new EnumMap<>(Variant.class);

        map.put(Variant.LOG, (base, props) -> new LogBlock(props, null, base.getId().getNamespace(), base.getId().getPath().replace("_planks", "_log")));
        map.put(Variant.WOOD, (base, props) -> new LogBlock(props, null, base.getId().getNamespace(), base.getId().getPath().replace("_planks", "_wood")));
        map.put(Variant.STRIPPED_LOG, (base, props) -> new LogBlock(props));
        map.put(Variant.STRIPPED_WOOD, (base, props) -> new LogBlock(props));

        map.put(Variant.LEAVES, (base, props) -> new LeavesBlock(props.noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(RegUtil::never).isViewBlocking(RegUtil::never).strength(0.2F).pushReaction(PushReaction.DESTROY).isRedstoneConductor(RegUtil::never).sound(SoundType.GRASS)));

        map.put(Variant.BOAT, (base, props) -> new AirBlock(props));
        map.put(Variant.CHEST_BOAT, (base, props) -> new AirBlock(props));

        map.put(Variant.SLAB, (base, props) -> new SlabBlock(props));
        map.put(Variant.STAIRS, (base, props) -> new StairBlock(base.get().defaultBlockState(), props));

        map.put(Variant.BUTTON, (base, props) -> new ButtonBlock(CompatUtil.getBlockSetType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), 30, props.noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        map.put(Variant.PRESSURE_PLATE, (base, props) -> new PressurePlateBlock(CompatUtil.getBlockSetType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props.noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
        map.put(Variant.FENCE, (base, props) -> new FenceBlock(props));
        map.put(Variant.FENCE_GATE, (base, props) -> new FenceGateBlock(CompatUtil.getWoodType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props.forceSolidOn()));

        map.put(Variant.DOOR, (base, props) -> new DoorBlock(CompatUtil.getBlockSetType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props.noOcclusion()));
        map.put(Variant.TRAPDOOR, (base, props) -> new TrapDoorBlock(CompatUtil.getBlockSetType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props.noOcclusion().isValidSpawn(Blocks::never)));

        map.put(Variant.STANDING_SIGN, (base, props) -> new StandingSignBlock(CompatUtil.getWoodType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props));
        map.put(Variant.WALL_SIGN, (base, props) -> new WallSignBlock(CompatUtil.getWoodType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props));

        map.put(Variant.HANGING_SIGN, (base, props) -> new CeilingHangingSignBlock(CompatUtil.getWoodType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props));
        map.put(Variant.HANGING_WALL_SIGN, (base, props) -> new WallHangingSignBlock(CompatUtil.getWoodType(base.getId().getNamespace(), base.getId().getPath().replace("_planks", "")), props));

        REG_MAP = Collections.unmodifiableMap(map);
    }

}
