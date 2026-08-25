package com.gumillea.inlandport.core.util.utils;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.List;

public class LootTableUtil {
    public static final float[] SAPLING = new float[] {0.05F, 0.0625F, 0.083333336F, 0.1F};
    public static final float[] FRUIT = new float[] {0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F};

    public static LootItemCondition.Builder hasEnchantment(HolderLookup.Provider provider, ResourceKey<Enchantment> enchantment) {
        HolderLookup.RegistryLookup<Enchantment> lookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
       return MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(lookup.getOrThrow(enchantment), MinMaxBounds.Ints.atLeast(1))))));
    }

    public static LootItemCondition.Builder hasSilkTouch(HolderLookup.Provider provider) {
        return hasEnchantment(provider, Enchantments.SILK_TOUCH);
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

    public static float[] scale(float[] chances, float multiplier) {
        int length = chances.length;
        float[] newChances = new float[length];

        for (int i = 0; i < length; i++) {
            newChances[i] = chances[i] * multiplier;
        }

        return newChances;
    }

}
