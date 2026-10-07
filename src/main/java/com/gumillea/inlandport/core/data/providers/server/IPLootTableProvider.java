package com.gumillea.inlandport.core.data.providers.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper.autoGeneBlockLoots;
import static com.gumillea.inlandport.core.util.utils.LootTableUtil.cropAge;

public abstract class IPLootTableProvider extends LootTableProvider {

    protected final String modId;

    public IPLootTableProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> future) {
        this(output, modId, future, IPBlockLoot::new);
    }

    public IPLootTableProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> future, BiFunction<HolderLookup.Provider, String, IPBlockLoot> factory) {
        this(output, modId, future, List.of(new SubProviderFactory(provider -> factory.apply(provider, modId), LootContextParamSets.BLOCK)));
    }

    public IPLootTableProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> future, SubProviderFactory... factories) {
        this(output, modId, future, Arrays.asList(factories));
    }

    public IPLootTableProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> future, List<SubProviderFactory> factories) {
        super(output, Collections.emptySet(), factories.stream().map(factory -> new LootTableProvider.SubProviderEntry(factory.provider(), factory.set())).toList(), future);
        this.modId = modId;
    }

    public record SubProviderFactory(Function<HolderLookup.Provider, LootTableSubProvider> provider, LootContextParamSet set) {}

    public static class IPBlockLoot extends BlockLootSubProvider {
        public final String modId;
        private final Set<Block> blocks = new HashSet<>();
        private boolean isAutoGenerating = false;

        protected IPBlockLoot(HolderLookup.Provider provider, String modId) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
            this.modId = modId;
        }

        @Override
        public void generate() {
            addManualBlockLoots();

            isAutoGenerating = true;
            autoGeneBlockLoots(this, modId);
            isAutoGenerating = false;
            blocks.clear();
        }

        public void addManualBlockLoots() {}

        public void nothing(Block block) {
            this.add(block, noDrop());
        }

        public void self(Block block) {
            super.dropSelf(block);
        }

        public void slab(Block block) {
            this.add(block, this::createSlabItemTable);
        }

        public void door(Block block) {
            this.add(block, this::createDoorTable);
        }

        public void crop(Block crop, Item fruit, Item seed, int maxAge, int age) {
            add(crop, createCropDrops(crop, fruit, seed, cropAge(crop, maxAge, age)));
        }

        public void crop(Block crop, Item fruit, Item seed, int age) {
            crop(crop, fruit, seed, age, age);
        }

        public void ore(Block block, Item drop) {
            add(block, createOreDrop(block, drop));
        }

        public void ore(Block block, Item drop, float min, float max) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
            add(block, createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
        }

        public void shulkerBox(Block block) {
            add(block, createShulkerBoxDrop(block));
        }

        public void silkTouch(Block block) {
            this.add(block, this::createSilkTouchOnlyTable);
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            if (this.isAutoGenerating) {
                if (this.blocks.contains(block)) {
                    return;
                }
            } else {
                this.blocks.add(block);
            }
            super.add(block, builder);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK.entrySet().stream().filter(e -> e.getKey().location().getNamespace().equals(modId)).map(Map.Entry::getValue).toList();
        }

    }


}