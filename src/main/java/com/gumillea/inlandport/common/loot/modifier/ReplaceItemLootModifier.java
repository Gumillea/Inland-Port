package com.gumillea.inlandport.common.loot.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public class ReplaceItemLootModifier extends LootModifier {
    @ApiStatus.Internal
    public static final MapCodec<ReplaceItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions), ResourceKey.codec(Registries.ITEM).fieldOf("item").forGetter(m -> m.item), ResourceKey.codec(Registries.ITEM).fieldOf("replacement").forGetter(m -> m.replacement)).apply(inst, ReplaceItemLootModifier::new));
    private final ResourceKey<Item> item;
    private final ResourceKey<Item> replacement;

    public ReplaceItemLootModifier(LootItemCondition[] conditions, ResourceKey<Item> item, ResourceKey<Item> replacement) {
        super(conditions);
        this.item = item;
        this.replacement = replacement;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Optional<Holder.Reference<Item>> target = context.getResolver().get(Registries.ITEM, item);
        Optional<Holder.Reference<Item>> replacementItem = context.getResolver().get(Registries.ITEM, replacement);

        if (target.isEmpty() || replacementItem.isEmpty()) return generatedLoot;

        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);

            if (stack.is(target.get().value())) {
                generatedLoot.set(i, new ItemStack(replacementItem.get().value(), stack.getCount()));
            }
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}