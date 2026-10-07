package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.core.util.utils.CompatUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IPArmorItem extends ArmorItem {

    private final String name;
    private final String modId;
    private final Consumer<ItemAttributeModifiers.Builder> consumer;

    public IPArmorItem(String name, String modId,Holder<ArmorMaterial> material, Type type, Properties properties, Consumer<ItemAttributeModifiers.Builder> consumer) {
        super(material, type, properties);
        this.name = name;
        this.modId = modId;
        this.consumer = consumer;
    }

    public Item getHelmet() {
        return CompatUtil.item(modId, name + "_helmet");
    }

    public Item getChestplate() {
        return CompatUtil.item(modId, name + "_chestplate");
    }

    public Item getLeggings() {
        return CompatUtil.item(modId, name + "_leggings");
    }

    public Item getBoots() {
        return CompatUtil.item(modId, name + "_boots");
    }

    public List<Item> getArmors() {
        List<Item> armors = new ArrayList<>();

        if (getBoots() != null) armors.add(getBoots());
        if (getLeggings() != null) armors.add(getLeggings());
        if (getChestplate() != null) armors.add(getChestplate());
        if (getHelmet() != null) armors.add(getHelmet());

        return armors;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);

        if (consumer == null) return modifiers;

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        consumer.accept(builder);

        return builder.build();
    }
}
