package com.gumillea.inlandport.core.data.modifiers;

import com.gumillea.inlandport.core.api.NotDisabledCondition;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.LootTableUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.gumillea.inlandport.core.util.utils.LootTableUtil.*;

public abstract class IPLootModifierProvider extends GlobalLootModifierProvider {
    protected final PackOutput output;
    protected final String modId;
    protected final HolderLookup.Provider provider;
    protected final Map<ResourceLocation, LootTable> map = new LinkedHashMap<>();

    public IPLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, String modId) {
        super(output, future, modId);
        this.output = output;
        this.modId = modId;
        this.provider = future.join();
    }

    protected void inject(String name, Object target, LootTable.Builder builder, ICondition... conditions) {
        ResourceLocation path = IPUtil.loc(modId, "inject/" + name);
        map.put(path, builder.build());

        List<LootItemCondition> list = new ArrayList<>();
        list.add(LootTableIdCondition.builder(getLoc(target)).build());



        add(name, new AddTableLootModifier(list.toArray(LootItemCondition[]::new), ResourceKey.create(Registries.LOOT_TABLE, path)), conditions);
    }

    protected void inject(Item item, Object target, LootTable.Builder inject, ICondition... conditions) {
        String path = getLoc(target).getPath();
        inject("add_" + RegUtil.path(item) + "_to_" + RegUtil.getBaseName(path), target, inject, ArrayUtils.addAll(new ICondition[] {new NotDisabledCondition(RegUtil.key(item))}, conditions));
    }

    protected void inject(Item item, Object target, float chance, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootItemRandomChanceCondition.randomChance(chance)).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void inject(Item item, Object target, int min, int max, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootTableUtil.hasSilkTouch(provider).invert()).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectWithFortuneChance(Item item, Object target, float[] chances, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootTableUtil.hasSilkTouch(provider).invert()).when(fortuneChances(provider, chances)).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectIntoPlant(Item item, Object target, LootItemCondition.Builder lootCond, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootTableUtil.hasSilkTouchOrShears(provider).invert()).when(lootCond).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectIntoPlant(Item item, Object target, float[] chances, ICondition... conditions) {
        injectIntoPlant(item, target, fortuneChances(provider, chances), conditions);
    }

    protected void injectIntoPlant(Item item, Object target, ICondition... conditions) {
        injectIntoPlant(item, target, FRUIT, conditions);
    }

    protected void injectIntoTallPlant(Item item, Block target, LootItemCondition.Builder lootCond, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootTableUtil.hasSilkTouchOrShears(provider).invert()).when(isUpperPart(target)).when(hasLowerPart(target)).when(lootCond).add(LootItem.lootTableItem(item)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootTableUtil.hasSilkTouchOrShears(provider).invert()).when(isLowerPart(target)).when(hasUpperPart(target)).when(lootCond).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectIntoGrassLikeBlock(Item item, Block shortTarget, Block tallTarget ,LootItemCondition.Builder lootCond, ICondition... conditions) {
        injectIntoPlant(item, shortTarget, lootCond, conditions);
        injectIntoTallPlant(item, tallTarget, lootCond, conditions);
    }

    protected void injectIntoGrass(Item item, LootItemCondition.Builder lootCond, ICondition... conditions) {
        injectIntoGrassLikeBlock(item, Blocks.SHORT_GRASS, Blocks.TALL_GRASS, lootCond, conditions);
    }

    protected void injectIntoGrass(Item item, float chance, ICondition... conditions) {
        injectIntoGrass(item, LootItemRandomChanceCondition.randomChance(chance), conditions);
    }

    protected void injectIntoFern(Item item, LootItemCondition.Builder lootCond, ICondition... conditions) {
        injectIntoGrassLikeBlock(item, Blocks.FERN, Blocks.LARGE_FERN, lootCond, conditions);
    }

    protected void injectIntoFern(Item item, float chance, ICondition... conditions) {
        injectIntoFern(item, LootItemRandomChanceCondition.randomChance(chance), conditions);
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider provider) {
        CompletableFuture<?> future = super.run(output, provider);

        return future.thenCompose(o -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            Path root = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(modId).resolve("loot_table");

            for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet()) {
                futures.add(DataProvider.saveStable(output, provider, LootTable.DIRECT_CODEC, entry.getValue(), root.resolve(entry.getKey().getPath() + ".json")));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private ResourceLocation getLoc(Object object) {
        switch (object) {
            case Block block -> {
                ResourceLocation location = RegUtil.key(block);
                return IPUtil.loc(location.getNamespace(), "blocks/" + location.getPath());
            }
            case ResourceLocation location -> {
                return location;
            }
            case ResourceKey<?> key -> {
                return key.location();
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }



}