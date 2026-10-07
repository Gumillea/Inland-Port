package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialHelper {
    private final DeferredRegister<ArmorMaterial> reg;
    private final String modId;

    public ArmorMaterialHelper(String modId) {
        this.modId = modId;
        this.reg = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, modId);
    }

    public void register(IEventBus bus) {
        reg.register(bus);
    }

    public DeferredHolder<ArmorMaterial, ArmorMaterial> reg(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngridient, List<ArmorMaterial.Layer> layers, float toughness, float knockbackResistance) {
        return reg.register(name, () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, defense[0]);
                    map.put(ArmorItem.Type.LEGGINGS, defense[1]);
                    map.put(ArmorItem.Type.CHESTPLATE, defense[2]);
                    map.put(ArmorItem.Type.HELMET, defense[3]);
                    map.put(ArmorItem.Type.BODY, defense[4]);
                }),
                enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance
        ));
    }

    public DeferredHolder<ArmorMaterial, ArmorMaterial> reg(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngridient, float toughness, float knockbackResistance) {
        return reg(name, defense, enchantmentValue, equipSound, repairIngridient, List.of(new ArmorMaterial.Layer(IPUtil.loc(modId, name)), new ArmorMaterial.Layer(IPUtil.loc(modId, name))), toughness, knockbackResistance);
    }


}
