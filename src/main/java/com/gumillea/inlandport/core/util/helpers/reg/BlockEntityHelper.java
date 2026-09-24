package com.gumillea.inlandport.core.util.helpers.reg;

import com.mojang.datafixers.types.Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class BlockEntityHelper {
    private final DeferredRegister<BlockEntityType<?>> blockEntityReg;
    private final String modId;

    public BlockEntityHelper(String modId) {
        this.modId = modId;
        this.blockEntityReg = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
    }

    public void register(IEventBus bus) {
        blockEntityReg.register(bus);
    }

    public <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> reg(String name, BlockEntityType.BlockEntitySupplier<? extends T> blockEntity, Supplier<Set<Block>> set, Type<?> dataType) {
        return blockEntityReg.register(name, () -> new BlockEntityType<>(blockEntity, set.get(), dataType));
    }
}
