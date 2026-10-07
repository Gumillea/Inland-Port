package com.gumillea.inlandport.common.loot.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.ApiStatus;

public class ReplaceTableLootModifier extends LootModifier {
    @ApiStatus.Internal
    public static final MapCodec<ReplaceTableLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions), ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(m -> m.table)).apply(inst, ReplaceTableLootModifier::new));
    private final ResourceKey<LootTable> table;

    public ReplaceTableLootModifier(LootItemCondition[] conditions, ResourceKey<LootTable> table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> replacement = new ObjectArrayList<>();
        context.getResolver().get(Registries.LOOT_TABLE, this.table).ifPresent(replacementTable -> replacementTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), replacement::add)));

        return replacement;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}