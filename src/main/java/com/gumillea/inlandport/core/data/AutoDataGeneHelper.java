package com.gumillea.inlandport.core.data;

import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.block.StickyBlock;
import com.gumillea.inlandport.common.block.StorageBlock;
import com.gumillea.inlandport.common.block.family.stone.StoneBaseBlock;
import com.gumillea.inlandport.common.block.family.wooden.LogBlock;
import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.common.item.IPBoatItem;
import com.gumillea.inlandport.common.item.RecordItem;
import com.gumillea.inlandport.core.data.providers.client.*;
import com.gumillea.inlandport.core.data.providers.server.IPBlockTagsProvider;
import com.gumillea.inlandport.core.data.providers.server.IPDataMapProvider;
import com.gumillea.inlandport.core.data.providers.server.IPItemTagsProvider;
import com.gumillea.inlandport.core.data.providers.server.IPLootTableProvider;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Map;

public class AutoDataGeneHelper {

   public static void autoGeneLangJson(IPLanguageProvider provider, String modId) {
        for (Block block : getBlocks(modId)) {
            if (block instanceof WallSignBlock || block instanceof WallHangingSignBlock) continue;

            switch (RegUtil.getBlockSuffix(block)) {
                case "_basket" -> provider.addBaskets(block);
                case "_sack" -> provider.addSacks(block);
                default -> provider.addBlocks(block);
            }
        }

        for (Item item : getItems(modId)) {
            switch (item) {
                case BlockItem ignored -> {}
                case IPBoatItem boat -> {
                   if (boat.hasChest) {
                       provider.addChestBoats(item);
                   } else {
                       provider.addItem(item);
                   }
                }
                case RecordItem ignored -> provider.addItem(item, "Music Disc");
                case SmithingTemplateItem ignored -> provider.addItem(item, "Smithing Template");
                default -> {
                    switch (RegUtil.getItemSuffix(item)) {
                        case "_slice" -> provider.addSlice(item);
                        default -> provider.addItem(item);
                    }
                }
            }

        }

        for (MobEffect effect : getReg(BuiltInRegistries.MOB_EFFECT, modId)) {
            provider.addEffects(effect);
        }

        for (Potion potion : getReg(BuiltInRegistries.POTION, modId)) {
            provider.addPotions(potion);
        }

        for (FluidType fluidType : getReg(NeoForgeRegistries.FLUID_TYPES, modId)) {
            provider.addFluidTypes(fluidType);
        }

        for (Attribute attribute : getReg(BuiltInRegistries.ATTRIBUTE, modId)) {
            provider.addAttributes(attribute);
        }

       for (CreativeModeTab tab : getReg(BuiltInRegistries.CREATIVE_MODE_TAB, modId)) {
           provider.addCreativeTabs(tab);
       }
    }

    public static void autoGeneItemModels(IPItemModelProvider provider, String modId) {
        for (Item item : getItems(modId)) {
            if (provider.hasExistingJson(RegUtil.path(item)) || !provider.hasExistingTexture(RegUtil.path(item))) continue;

            switch (item) {
                case DiggerItem ignored -> provider.addHandheldItem(item);
                case SwordItem ignored -> provider.addHandheldItem(item);
                case SpawnEggItem ignored -> provider.addSpawnEggItem(item);
                default -> provider.addItem(item);
            }
        }
    }

    public static void autoGeneBlockModels(IPBlockStateProvider provider, String modId) {
        for (Block block : BuiltInRegistries.BLOCK.stream().filter(item -> AutoDataGeneHelper.isSame(RegUtil.key(item), modId)).toList()) {
            String id = RegUtil.path(block);
            if (provider.hasExistingModel(id) || provider.hasExistingState(id)) continue;
            Block baseBlock;

            switch (block) {
                case LogBlock logBlock -> provider.logOrWoodBlock(logBlock);
                case StickyBlock ignored -> provider.honeyBlock(block);
                case StorageBlock storageBlock -> provider.cubeBottomTop(storageBlock);
                case RotatedPillarBlock rotatedPillarBlock -> provider.rotatedPillarBlock(rotatedPillarBlock);
                case DoorBlock door -> provider.doorBlock(door);
                case TrapDoorBlock door -> provider.trapDoorBlock(door);
                case EdibleBlock edibleBlock -> provider.edibleBlock(edibleBlock);
                case StandingSignBlock sign -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(sign), "sign");
                    provider.signBlock(sign, baseBlock);
                }
                case WallSignBlock sign -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(sign), "wall_sign");
                    provider.wallSignBlock(sign, baseBlock);
                }
                case CeilingHangingSignBlock sign -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(sign), "hanging_sign");
                    provider.signBlock(sign, baseBlock);
                }
                case WallHangingSignBlock sign -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(sign), "hanging_wall_sign");
                    provider.wallSignBlock(sign, baseBlock);
                }
                case ButtonBlock button -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(button), "button");
                    provider.buttonBlock(button, baseBlock);
                }
                case PressurePlateBlock pressurePlate -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(pressurePlate), "pressure_plate");
                    provider.pressurePlateBlock(pressurePlate, baseBlock);
                }
                case SlabBlock slab -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(slab), "slab");
                    provider.slabBlock(slab, baseBlock);
                }
                case StairBlock stairs -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(stairs), "stairs");
                    provider.stairsBlock(stairs, baseBlock);
                }
                case FenceBlock fence -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(fence), "fence");
                    provider.fenceBlock(fence, baseBlock);
                }
                case FenceGateBlock fenceGate -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(fenceGate), "fence_gate");
                    provider.fenceGateBlock(fenceGate, baseBlock);
                }
                case WallBlock wall -> {
                    baseBlock = tryFindBaseBlock(modId, RegUtil.path(wall), "wall");
                    provider.wallBlock(wall, baseBlock);
                }
                default -> {
                    if (RegUtil.path(block).startsWith("chiseled_")) {
                        provider.chiseledBlock(block);
                    } else {
                        provider.simpleBlock(block);
                    }
                }
            }
        }
    }

    public static void autoGeneBlockTags(IPBlockTagsProvider provider, String modId) {
        for (Block block : getBlocks(modId)) {
            switch (block) {
                case StoneBaseBlock family -> provider.addStoneFamily(family);
                case WoodenBaseBlock family -> provider.addWoodenFamily(family);
                case ButtonBlock ignored -> provider.tag(BlockTags.BUTTONS).add(block);
                case PressurePlateBlock ignored -> provider.tag(BlockTags.PRESSURE_PLATES).add(block);
                case SlabBlock ignored -> provider.tag(BlockTags.SLABS).add(block);
                case StairBlock ignored -> provider.tag(BlockTags.STAIRS).add(block);
                case FenceBlock ignored -> provider.tag(BlockTags.FENCES).add(block);
                case FenceGateBlock ignored -> provider.tag(BlockTags.FENCE_GATES).add(block);
                case WallBlock ignored -> provider.tag(BlockTags.WALLS).add(block);
                case LeavesBlock ignored -> provider.tag(BlockTags.LEAVES).add(block);
                case StorageBlock ignored -> {
                    if (RegUtil.path(block).endsWith("_crate")) {
                        provider.tag(BlockTags.MINEABLE_WITH_AXE).add(block);
                    } else {
                        provider.tag(BlockTags.MINEABLE_WITH_HOE).add(block);
                    }
                    provider.addIfExist(Tags.Blocks.STORAGE_BLOCKS, block);
                }
                case StickyBlock stickyBlock -> {
                    if (stickyBlock.isClimbable()) {
                        provider.tag(BlockTags.CLIMBABLE).add(block);
                    }
                    provider.addIfExist(Tags.Blocks.STORAGE_BLOCKS, block);
                }
                default -> {}
            }
        }
    }

    public static void autoGeneItemTags(IPItemTagsProvider provider, String modId) {
        for (Block block : getBlocks(modId)) {
            switch (block) {
                case WoodenBaseBlock family -> provider.addWoodenFamily(family);
                case SlabBlock ignored -> provider.copy(BlockTags.SLABS, ItemTags.SLABS);
                case StairBlock ignored -> provider.copy(BlockTags.STAIRS, ItemTags.STAIRS);
                case StorageBlock ignored ->  provider.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
                case FenceBlock ignored -> provider.copy(BlockTags.FENCES, ItemTags.FENCES);
                case FenceGateBlock ignored -> provider.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
                case WallBlock ignored -> provider.copy(BlockTags.WALLS, ItemTags.WALLS);
                case LeavesBlock ignored -> provider.copy(BlockTags.LEAVES, ItemTags.LEAVES);
                case EdibleBlock edibleBlock -> {
                    if (edibleBlock.getType() == EdibleBlock.Type.PIE) {
                        provider.addIfExist(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, edibleBlock.asItem());
                        provider.addIfExist(Tags.Items.FOODS_PIE, edibleBlock.asItem());
                        provider.addIfExist(Tags.Items.FOODS_PIE, edibleBlock.getSlice().getItem());
                    }
                }
                default -> {}
            }
        }

        for (Item item : getItems(modId)) {
            switch (item) {
                case AxeItem ignored -> provider.tag(ItemTags.AXES).add(item);
                case HoeItem ignored -> provider.tag(ItemTags.HOES).add(item);
                case PickaxeItem ignored -> provider.tag(ItemTags.PICKAXES).add(item);
                case SwordItem ignored -> provider.tag(ItemTags.SWORDS).add(item);
                case ShovelItem ignored -> provider.tag(ItemTags.SHOVELS).add(item);
                case RecordItem ignored -> provider.tag(Tags.Items.MUSIC_DISCS).add(item);
                default -> {
                }
            }
        }
    }

    public static void autoGeneDataMap(IPDataMapProvider provider, String modId) {
        for (Block block : getBlocks(modId)) {
            switch (block) {
                case WoodenBaseBlock family -> {
                    provider.addFuel(40.0F, family.getCeilingHangingSign());
                    provider.addFuel(15.0F, block, family.getWood(), family.getStrippedWood(), family.getLog(), family.getStrippedLog(), family.getStairs(), family.getPressurePlate(), family.getFenceGate(), family.getFence(), family.getTrapdoor());
                    provider.addFuel(10.0F, family.getStandingSign(), family.getDoor());
                    provider.addFuel(7.5F, family.getSlab());
                    provider.addFuel(5.0F, family.getButton());
                }
                case EdibleBlock edibleBlock -> {
                    provider.setCompostable(1.0F, edibleBlock.getItem().getItem());
                    provider.setCompostable(0.5F, edibleBlock.getSlice().getItem());
                }
                case LeavesBlock ignored -> provider.setCompostable(0.3F, block.asItem());
                default -> {}
            }
        }

        for (Item item : getItems(modId)) {
            ItemStack stack = new ItemStack(item);
            if (stack.is(Tags.Items.SEEDS)) provider.setCompostable(0.3F, item);
            if (stack.is(Tags.Items.FOODS_FRUIT)) provider.setCompostable(0.65F, item);
            if (stack.is(Tags.Items.FOODS_BREAD)) provider.setCompostable(0.85F, item);
        }
    }

    public static void autoGeneSoundJson(IPSoundDefinitionsProvider provider, String modId) {
        for (SoundEvent event : BuiltInRegistries.SOUND_EVENT) {
            if (!RegUtil.key(event).getNamespace().equals(modId)) continue;
            provider.addSound(event);
        }
    }

    public static void autoGeneBlockLoots(IPLootTableProvider.IPBlockLoot provider, String modId) {
        for (Block block : getBlocks(modId)) {
            switch (block) {
                case SlabBlock ignored -> provider.slab(block);
                case DoorBlock ignored -> provider.door(block);
                case EdibleBlock ignored -> provider.nothing(block);
                default -> provider.self(block);
            }
        }
    }

    public static Block tryFindBaseBlock(String modId, String id, String suffix) {
        String name = id.replace("_" + suffix, "");
        Block block;

        block = CompatUtil.block(modId, name);
        if (block != null) return block;

        block = CompatUtil.block(modId, name + "_planks");
        if (block != null) return block;

        block = CompatUtil.block(modId, name + "_ore");
        if (block != null) return block;

        block = CompatUtil.block(modId, name.replace("_brick", "_bricks"));
        if (block != null) return block;

        block = CompatUtil.block(modId, name.replace("_tile", "_tiles"));
        if (block != null) return block;

        return Blocks.AIR;
    }

    public static boolean isSame(ResourceLocation location, String modId) {
        return location.getNamespace().equals(modId);
    }

    public static boolean isSame(ResourceKey<?> key, String modId) {
        return key.location().getNamespace().equals(modId);
    }

    public static <T> List<T> getReg(Registry<T> reg , String modId) {
        return reg.entrySet().stream().filter(e -> isSame(e.getKey(), modId)).map(Map.Entry::getValue).toList();
    }

    public static List<Block> getBlocks(String modId) {
        return getReg(BuiltInRegistries.BLOCK, modId);
    }

    public static List<Item> getItems(String modId) {
        return getReg(BuiltInRegistries.ITEM, modId);
    }

}

