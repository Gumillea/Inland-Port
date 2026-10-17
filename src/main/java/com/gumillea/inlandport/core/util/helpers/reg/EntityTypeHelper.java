package com.gumillea.inlandport.core.util.helpers.reg;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityTypeHelper {

    private final DeferredRegister<EntityType<?>> reg;
    private final String modId;

    public EntityTypeHelper(String modId) {
        this.modId = modId;
        this.reg = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, modId);
    }

    public void register(IEventBus bus) {
        reg.register(bus);
    }

    public <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> reg(String name, Supplier<EntityType<T>> supplier) {
        return reg.register(name, supplier);
    }
}
