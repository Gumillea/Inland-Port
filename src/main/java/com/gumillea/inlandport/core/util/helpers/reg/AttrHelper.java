package com.gumillea.inlandport.core.util.helpers.reg;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AttrHelper {
    private final DeferredRegister<Attribute> attrReg;
    private final String modId;

    public AttrHelper(String modId) {
        this.modId = modId;
        this.attrReg = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, modId);
    }

    public void register(IEventBus bus) {
        attrReg.register(bus);
    }

    public DeferredHolder<Attribute, Attribute> regRanged(String name, double defaultValue, double minValue, double maxValue) {
        return attrReg.register("generic." + name, () -> new RangedAttribute(attrReg.getNamespace() + "." + "generic." + name, defaultValue, minValue, maxValue).setSyncable(true));
    }

    public DeferredHolder<Attribute, Attribute> regRanged(String name, double defaultValue, double minValue, double maxValue, boolean isSyncable) {
        return attrReg.register("generic." + name, () -> new RangedAttribute(attrReg.getNamespace() + "." + "generic." + name, defaultValue, minValue, maxValue).setSyncable(isSyncable));
    }
}
