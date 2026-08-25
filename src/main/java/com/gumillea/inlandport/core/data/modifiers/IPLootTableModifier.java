package com.gumillea.inlandport.core.data.modifiers;

import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
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

import static com.gumillea.inlandport.core.util.utils.LootTableUtil.FRUIT;
import static com.gumillea.inlandport.core.util.utils.LootTableUtil.fortuneChances;

public abstract class IPLootTableModifier extends GlobalLootModifierProvider {
    private final PackOutput output;
    private final String modId;
    private final HolderLookup.Provider provider;
    private final Map<ResourceLocation, LootTable> map = new LinkedHashMap<>();

    public IPLootTableModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> future, String modId) {
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
        inject("add_" + RegUtil.path(item) + "_to_" + RegUtil.getBaseName(path), target, inject, conditions);
    }

    protected void inject(Item item, Object target, float chance, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootItemRandomChanceCondition.randomChance(chance)).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectWithFortune(Item item, Object target, float[] chances, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(fortuneChances(provider, chances)).add(LootItem.lootTableItem(item))), conditions);
    }

    protected void injectFruit(Item item, Object target, ICondition... conditions) {
        inject(item, target, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(fortuneChances(provider, FRUIT)).add(LootItem.lootTableItem(item))), conditions);
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
            case Item item -> {
                return RegUtil.key(item);
            }
            case Block block -> {
                return RegUtil.key(block);
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