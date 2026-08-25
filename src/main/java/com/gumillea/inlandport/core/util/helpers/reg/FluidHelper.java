package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.fluid.IPFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FluidHelper {
    private final DeferredRegister<FluidType> fluidTypeReg;
    private final DeferredRegister<Fluid> fluidReg;
    private final String modId;

    public FluidHelper(String modId) {
        this.modId = modId;
        this.fluidTypeReg = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, modId);
        this.fluidReg = DeferredRegister.create(BuiltInRegistries.FLUID, modId);
    }

    public void register(IEventBus bus) {
        fluidTypeReg.register(bus);
        fluidReg.register(bus);
    }

    public DeferredHolder<FluidType, FluidType> regFluidType(String name, Supplier<FluidType> supplier) {
        return fluidTypeReg.register(name, supplier);
    }

    public <T extends Fluid> DeferredHolder<Fluid, T> regFluid(String name, Supplier<T> supplier) {
        return fluidReg.register(name, supplier);
    }

    public DeferredHolder<FluidType, FluidType> reg(String name, Supplier<FluidType> supplier) {
        DeferredHolder<FluidType, FluidType> fluidType = regFluidType(name, supplier);
        final BaseFlowingFluid.Properties[] properties = new BaseFlowingFluid.Properties[1];
        DeferredHolder<Fluid, BaseFlowingFluid.Source> source = regFluid(name, () -> new BaseFlowingFluid.Source(properties[0]));
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing = regFluid("flowing_" + name, () -> new BaseFlowingFluid.Flowing(properties[0]));
        properties[0] = new BaseFlowingFluid.Properties(fluidType, source, flowing);

        return fluidType;
    }

    public DeferredHolder<FluidType, FluidType> reg(String name) {
        return reg(name, () -> new IPFluidType(name));
    }

}
