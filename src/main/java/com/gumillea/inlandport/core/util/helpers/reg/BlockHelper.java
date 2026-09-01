package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.common.block.family.stone.StoneBaseBlock;
import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.common.entity.IPBoat;
import com.gumillea.inlandport.common.entity.IPChestBoat;
import com.gumillea.inlandport.common.item.EdibleBlockItem;
import com.gumillea.inlandport.common.item.IPBoatItem;
import com.gumillea.inlandport.core.api.record.RegConditions;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.ibm.icu.impl.Pair;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockHelper {
//It's kinda a mess...
    private final DeferredRegister<Block> blockReg;
    private final DeferredRegister<Item> itemReg;
    private final DeferredRegister<EntityType<?>> entityReg;
    private final String modId;

    private static final List<DeferredHolder<Block, ? extends Block>> SIGNS = new ArrayList<>();
    private static final List<DeferredHolder<Block, ? extends Block>> HANGING_SIGNS = new ArrayList<>();
    public static final List<DeferredHolder<EntityType<?>, EntityType<IPBoat>>> BOATS = new ArrayList<>();
    public static final List<DeferredHolder<EntityType<?>, EntityType<IPChestBoat>>> CHEST_BOATS = new ArrayList<>();

    public BlockHelper(String modId) {
        this.modId = modId;
        this.blockReg = DeferredRegister.createBlocks(modId);
        this.entityReg = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, modId);
        this.itemReg = DeferredRegister.createItems(modId);
    }

    public void register(IEventBus bus) {
        blockReg.register(bus);
        itemReg.register(bus);
        entityReg.register(bus);
    }

    public <T extends Block> DeferredHolder<Block, T> regWithoutItem(String name, Supplier<T> supplier, RegConditions conditions) {
        return RegHelper.reg(blockReg, name, supplier, null, conditions);
    }

    public <T extends Block> DeferredHolder<Block, T> regWithoutItem(String name, Supplier<T> supplier) {
        return regWithoutItem(name, supplier, null);
    }

    public <T extends Block> DeferredHolder<Block, T> reg(String name, Supplier<T> supplier, RegConditions conditions) {
        DeferredHolder<Block, T> block = RegHelper.reg(blockReg, name, supplier, null, conditions);
        if (block != null) RegHelper.reg(itemReg, name, () -> new BlockItem(block.value(), new Item.Properties()), () -> new Item(new Item.Properties()), conditions);
        return block;
    }

    public <T extends Block> DeferredHolder<Block, T> reg(String name, Supplier<T> supplier) {
        return reg(name, supplier, null);
    }

    public DeferredHolder<Block, Block> regPlaceableFood(String name, BlockBehaviour.Properties blockProps, Item.Properties itemProps, EdibleBlock.Type type, Supplier<Item> slice, int maxBites, RegConditions conditions) {
        DeferredHolder<Block, Block> block = regWithoutItem(name, () -> new EdibleBlock(blockProps, type, slice, maxBites), conditions);
        if (block != null) itemReg.register(name.replace("_pie_block", "_pie"), () -> new EdibleBlockItem(block.value(), itemProps));
        return block;
    }

    public DeferredHolder<Block, Block> regPlaceableFood(String name, BlockBehaviour.Properties blockProps, Item.Properties itemProps, EdibleBlock.Type type, Supplier<Item> slice, int maxBites) {
        return regPlaceableFood(name, blockProps, itemProps, type, slice, maxBites, null);
    }

    public DeferredHolder<Block, Block> regCake(String name, BlockBehaviour.Properties blockProps, Supplier<Item> slice, RegConditions conditions) {
        return regPlaceableFood(name, blockProps, new Item.Properties().stacksTo(1), EdibleBlock.Type.CAKE, slice, 8, conditions);
    }

    public DeferredHolder<Block, Block> regCake(String name, BlockBehaviour.Properties blockProps, Supplier<Item> slice) {
        return regCake(name, blockProps, slice, null);
    }

    public DeferredHolder<Block, Block> regCake(String name, MapColor color, Supplier<Item> slice, RegConditions conditions) {
        return regCake(name, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).mapColor(color), slice, conditions);
    }

    public DeferredHolder<Block, Block> regCake(String name, MapColor color, Supplier<Item> slice) {
        return regCake(name, color, slice, null);
    }

    public DeferredHolder<Block, Block> regPie(String name, BlockBehaviour.Properties blockProps, Item.Properties itemProps, Supplier<Item> slice, RegConditions conditions) {
        return regPlaceableFood(name, blockProps, itemProps, EdibleBlock.Type.PIE, slice, 4, conditions);
    }

    public DeferredHolder<Block, Block> regPie(String name, BlockBehaviour.Properties blockProps, Item.Properties itemProps, Supplier<Item> slice) {
        return regPie(name, blockProps, itemProps, slice, null);
    }

    public DeferredHolder<Block, Block> regPie(String name, MapColor color, Item.Properties itemProps, Supplier<Item> slice, RegConditions conditions) {
        return regPie(name, BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).mapColor(color), itemProps, slice, conditions);
    }

    public DeferredHolder<Block, Block> regPie(String name, MapColor color, Item.Properties itemProps, Supplier<Item> slice) {
        return regPie(name, color, itemProps, slice, null);
    }

    public DeferredHolder<EntityType<?>, EntityType<IPBoat>> regBoat(String name) {
        DeferredHolder<EntityType<?>, EntityType<IPBoat>> boat = entityReg.register(name, () -> EntityType.Builder.of(IPBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(name));
        itemReg.register(name, () -> new IPBoatItem(false, boat.getId(), new Item.Properties().stacksTo(1)));
        BOATS.add(boat);
        return boat;
    }

    public DeferredHolder<EntityType<?>, EntityType<IPChestBoat>> regChestBoat(String name) {
        DeferredHolder<EntityType<?>, EntityType<IPChestBoat>> boat = entityReg.register(name, () -> EntityType.Builder.of(IPChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(name));
        itemReg.register(name, () -> new IPBoatItem(true, boat.getId(), new Item.Properties().stacksTo(1)));
        CHEST_BOATS.add(boat);
        return boat;
    }

    public Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> regSignBlocks(String name, Supplier<? extends Block> standingSupplier, Supplier<? extends Block> wallSupplier) {
        DeferredHolder<Block, ? extends Block> standing = blockReg.register(name + "_sign", standingSupplier);
        DeferredHolder<Block, ? extends Block> wall = blockReg.register(name + "_wall_sign", wallSupplier);
        SIGNS.add(standing);
        SIGNS.add(wall);

        return Pair.of(standing, wall);
    }

    public void regSignItem(String name, Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> sign) {
        itemReg.register(name, () -> new SignItem(new Item.Properties(), sign.first.get(), sign.second.get()));
    }

    public Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> regHangingSignBlocks(String name, Supplier<? extends Block> standingSupplier, Supplier<? extends Block> wallSupplier) {
        DeferredHolder<Block, ? extends Block> standing = blockReg.register(name + "_hanging_sign", standingSupplier);
        DeferredHolder<Block, ? extends Block> wall = blockReg.register(name + "_hanging_wall_sign", wallSupplier);
        HANGING_SIGNS.add(standing);
        HANGING_SIGNS.add(wall);

        return Pair.of(standing, wall);
    }

    public void regHangingSignItem(String name, Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> sign) {
        itemReg.register(name, () -> new HangingSignItem(sign.first.get(), sign.second.get(), new Item.Properties()));
    }

    public <V extends Enum<V>, B extends Block> DeferredHolder<Block, B> regFamily(String name, BiFunction<BlockBehaviour.Properties, String, B> reg, EnumSet<V> variants, Map<V, BiFunction<DeferredHolder<Block, B>, BlockBehaviour.Properties, Block>> regMap, BlockBehaviour.Properties properties, BiFunction<V, String, String> regName, RegConditions conditions) {
        DeferredHolder<Block, B> baseBlock = reg(name, () -> reg.apply(properties, name), conditions);

        if (baseBlock != null) {
            variants.stream().filter(variant -> variant != Variant.BASE).forEach(variant -> {
                String basename = name.replace("_planks", "").replace("_ore", "");
                var factory = regMap.get(variant);
                if (factory != null) {
                    String variantName = regName.apply(variant, basename);
                    if (variant instanceof Variant v) {
                        switch (v) {
                            case BOAT -> regBoat(variantName);
                            case CHEST_BOAT -> regChestBoat(variantName);
                            case STANDING_SIGN -> {
                                BiFunction<DeferredHolder<Block, B>, BlockBehaviour.Properties, Block> standingFactory = regMap.get(Variant.STANDING_SIGN);
                                BiFunction<DeferredHolder<Block, B>, BlockBehaviour.Properties, Block> wallFactory = regMap.get(Variant.WALL_SIGN);
                                Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> sign = regSignBlocks(basename, () -> standingFactory.apply(baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock.get())), () -> wallFactory.apply(baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock.get())));
                                regSignItem(variantName, sign);
                            }
                            case HANGING_SIGN -> {
                                BiFunction<DeferredHolder<Block, B>, BlockBehaviour.Properties, Block> standingFactory = regMap.get(Variant.HANGING_SIGN);
                                BiFunction<DeferredHolder<Block, B>, BlockBehaviour.Properties, Block> wallFactory = regMap.get(Variant.HANGING_WALL_SIGN);
                                Pair<DeferredHolder<Block, ? extends Block>, DeferredHolder<Block, ? extends Block>> sign = regHangingSignBlocks(basename, () -> standingFactory.apply(baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock.get())), () -> wallFactory.apply(baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock.get())));
                                regHangingSignItem(variantName, sign);
                            }
                            default -> {
                                if (v != Variant.WALL_SIGN && v != Variant.HANGING_WALL_SIGN) {
                                    reg(variantName, () -> factory.apply(baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock.get())), conditions);
                                }
                            }
                        }
                    }
                }
            });
        }
        return baseBlock;
    }

    public DeferredHolder<Block, Block> regSimpleFamily(String name, BlockBehaviour.Properties properties, EnumSet<Variant> variants, RegConditions conditions) {
        return regFamily(name, (props, n) -> new SimpleBaseBlock(props, this.blockReg.getNamespace(), n), variants, SimpleBaseBlock.REG_MAP, properties, (variant, baseName) -> variant.getPrefix() + baseName + variant.getSuffix(), conditions);
    }

    public DeferredHolder<Block, Block> regSimpleFamily(String name, BlockBehaviour.Properties properties, RegConditions conditions) {
        return regSimpleFamily(name, properties, Variant.all(), conditions);
    }

    public DeferredHolder<Block, Block> regStoneFamily(String name, BlockBehaviour.Properties properties, EnumSet<Variant> variants, RegConditions conditions) {
        return regFamily(name, (props, n) -> new StoneBaseBlock(props, this.blockReg.getNamespace(), n), variants, StoneBaseBlock.REG_MAP, properties, (variant, baseName) -> variant.getPrefix() + baseName + variant.getSuffix(), conditions);
    }

    public DeferredHolder<Block, Block> regStoneFamily(String name, BlockBehaviour.Properties properties, RegConditions conditions) {
        return regStoneFamily(name, properties, Variant.all(), conditions);
    }

    public DeferredHolder<Block, Block> regWoodFamily(String name, BlockBehaviour.Properties properties, EnumSet<Variant> variants, RegConditions conditions) {
        return regFamily(name + "_planks", (props, n) -> new WoodenBaseBlock(props, this.blockReg.getNamespace(), n), variants, WoodenBaseBlock.REG_MAP, properties, (variant, baseName) -> variant.getPrefix() + baseName + variant.getSuffix(), conditions);
    }

    public DeferredHolder<Block, Block> regWoodFamily(String name, BlockBehaviour.Properties properties, RegConditions conditions) {
        return regWoodFamily(name, properties, Variant.all(), conditions);
    }

    public static void regBlockEntities() {
        BlockEntityType.SIGN.validBlocks = Stream.concat(BlockEntityType.SIGN.validBlocks.stream(), SIGNS.stream().map(DeferredHolder::value)).collect(Collectors.toSet());
        BlockEntityType.HANGING_SIGN.validBlocks = Stream.concat(BlockEntityType.HANGING_SIGN.validBlocks.stream(), HANGING_SIGNS.stream().map(DeferredHolder::value)).collect(Collectors.toSet());
    }

    public static void regSheets() {
        for (Holder<Block> signs : SIGNS) {
            Block sign = signs.value();
            if (sign instanceof StandingSignBlock) {
                ResourceLocation key = RegUtil.key(signs.value());
                String modId = key.getNamespace();
                String base = key.getPath().replace("_sign", "");
                Sheets.SIGN_MATERIALS.put(CompatUtil.getWoodType(modId, base), new Material(Sheets.SIGN_SHEET, IPUtil.loc(modId, "entity/signs/" + base)));
            }
        }

        for (Holder<Block> signs : HANGING_SIGNS) {
            Block sign = signs.value();
            if (sign instanceof CeilingHangingSignBlock) {
                ResourceLocation key = RegUtil.key(signs.value());
                String modId = key.getNamespace();
                String base = key.getPath().replace("_hanging_sign", "");
                Sheets.HANGING_SIGN_MATERIALS.put(CompatUtil.getWoodType(modId, base), new Material(Sheets.SIGN_SHEET, IPUtil.loc(modId, "entity/signs/hanging/" + base)));
            }
        }

    }

    public static BlockBehaviour.Properties woodProperties(BlockBehaviour.Properties properties, MapColor plankColor, MapColor barkColor, MapColor leavesColor) {
        return properties.mapColor(state -> {
            Block block = state.getBlock();
            switch (block) {
                case LeavesBlock ignored -> {
                    return leavesColor;
                }
                case RotatedPillarBlock ignored -> {
                    return state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? plankColor : barkColor;
                }
                default ->  {
                    return plankColor;
                }
            }
        });
    }

}

