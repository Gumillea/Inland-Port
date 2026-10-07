package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.loot.modifier.ReplaceItemLootModifier;
import com.gumillea.inlandport.core.util.helpers.reg.LootHelper;
import com.gumillea.inlandport.common.loot.modifier.ReplaceTableLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IPLootModifiers {
    public static final LootHelper HELPER = new LootHelper(InlandPort.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> REPLACE_TABLE = HELPER.reg("replace_table", () -> ReplaceTableLootModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> REPLACE_ITEM = HELPER.reg("replace_item", () -> ReplaceItemLootModifier.CODEC);
}
