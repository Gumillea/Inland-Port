package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.api.NotDisabledCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IPConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, InlandPort.MODID);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<NotDisabledCondition>> NOT_DISABLED_CONDITION = CONDITION_CODECS.register("not_disabled", () -> NotDisabledCondition.CODEC);

    public static void register(IEventBus bus) {
        CONDITION_CODECS.register(bus);
    }
}
