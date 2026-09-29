package com.gumillea.inlandport.core.util.helpers.reg;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class LootHelper {

    private final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> reg;
    private final String modId;

    public LootHelper(String modId) {
        this.modId = modId;
        this.reg = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, modId);
    }

    public void register(IEventBus bus) {
        reg.register(bus);
    }

    public final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> reg(String name, Supplier<MapCodec<? extends IGlobalLootModifier>> supplier) {
        return reg.register(name, supplier);
    }


}
