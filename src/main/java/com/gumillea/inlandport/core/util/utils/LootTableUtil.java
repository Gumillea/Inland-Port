package com.gumillea.inlandport.core.util.utils;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.List;

public class LootTableUtil {
    public static final float[] SAPLING = new float[] {0.05F, 0.0625F, 0.083333336F, 0.1F};
    public static final float[] FRUIT = new float[] {0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F};

    public static LootItemCondition.Builder hasEnchantment(HolderLookup.Provider provider, ResourceKey<Enchantment> enchantment) {
        HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
       return MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(lookup.getOrThrow(enchantment), MinMaxBounds.Ints.atLeast(1))))));
    }

    public static LootItemCondition.Builder hasTool(ItemLike item) {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(item));
    }

    public static LootItemCondition.Builder hasTool(TagKey<Item> item) {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(item));
    }


    public static LootItemCondition.Builder hasSilkTouch(HolderLookup.Provider provider) {
        return hasEnchantment(provider, Enchantments.SILK_TOUCH);
    }

    public static LootItemCondition.Builder hasSilkTouchOrShears(HolderLookup.Provider provider) {
        return hasEnchantment(provider, Enchantments.SILK_TOUCH).or(hasTool(Items.SHEARS));
    }

    public static LootItemCondition.Builder fortuneChances(HolderLookup.Provider provider, float[] chances) {
        HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        return BonusLevelTableCondition.bonusLevelFlatChance(lookup.getOrThrow(Enchantments.FORTUNE), chances);
    }

    public static LootItemCondition.Builder lootingChances(HolderLookup.Provider provider, float[] chances) {
        HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
        return BonusLevelTableCondition.bonusLevelFlatChance(lookup.getOrThrow(Enchantments.LOOTING), chances);
    }

    public static LootItemCondition.Builder saplingChances(HolderLookup.Provider provider) {
        return fortuneChances(provider, SAPLING);
    }

    public static LootItemCondition.Builder fruitChances(HolderLookup.Provider provider) {
        return fortuneChances(provider, FRUIT);
    }

    public static LootItemCondition.Builder hasProperties(Block block, Property<Integer> prop, int i) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, i));
    }

    public static LootItemCondition.Builder hasProperties(Block block, Property<?> prop, String string) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, string));
    }

    public static LootItemCondition.Builder cropAge(Block block, int maxAge, int age) {
        return hasProperties(block, IntegerProperty.create("age", 0, maxAge), age);
    }

    public static LootItemCondition.Builder checkLocation(Block target, Property<?> prop, String string, BlockPos pos) {
        return LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(target).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, string))), pos);
    }

    public static LootItemCondition.Builder hasUpperPart(Block target) {
        return checkLocation(target, BlockStateProperties.HALF, "upper", new BlockPos(0, 1, 0));
    }

    public static LootItemCondition.Builder isUpperPart(Block block) {
        return hasProperties(block, BlockStateProperties.HALF, "upper");
    }

    public static LootItemCondition.Builder hasLowerPart(Block target) {
        return checkLocation(target, BlockStateProperties.HALF, "lower", new BlockPos(0, -1, 0));
    }

    public static LootItemCondition.Builder isLowerPart(Block block) {
        return hasProperties(block, BlockStateProperties.HALF, "lower");
    }

    public static float[] scale(float[] chances, float multiplier) {
        int length = chances.length;
        float[] newChances = new float[length];

        for (int i = 0; i < length; i++) {
            newChances[i] = chances[i] * multiplier;
        }

        return newChances;
    }

    public static ResourceKey<LootTable> get(String modId, String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, IPUtil.loc(modId, name));
    }

    public static ResourceKey<LootTable> get(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, IPUtil.mcLoc(name));
    }

}
