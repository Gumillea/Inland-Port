package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeHelper {
    private final DeferredRegister<RecipeSerializer<?>> serializerReg;
    private final DeferredRegister<RecipeType<?>> typeReg;
    private final String modId;

    public RecipeHelper(String modId) {
        this.modId = modId;
        this.serializerReg = DeferredRegister.create(Registries.RECIPE_SERIALIZER, modId);
        this.typeReg = DeferredRegister.create(Registries.RECIPE_TYPE, modId);
    }

    public void register(IEventBus bus) {
        serializerReg.register(bus);
        typeReg.register(bus);
    }

    public <T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> regSerializer(String name, Supplier<RecipeSerializer<T>> supplier) {
        return serializerReg.register(name, supplier);
    }

    public <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> regType(String name, Supplier<RecipeType<T>> supplier) {
        return typeReg.register(name, supplier);
    }

    public <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> regType(String name) {
        return regType(name, () -> RecipeType.simple(IPUtil.loc(modId, name)));
    }

}
