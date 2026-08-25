package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.item.ContainerFoodItem;
import com.gumillea.inlandport.common.item.FoodItem;
import com.gumillea.inlandport.common.item.RecordItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemHelper {

    private final DeferredRegister<Item> itemReg;
    private final String modId;

    public ItemHelper(String modId) {
        this.modId = modId;
        this.itemReg = DeferredRegister.createItems(modId);
    }

    public void register(IEventBus bus) {
        itemReg.register(bus);
    }

    public <T extends Item> DeferredHolder<Item, T> reg(String name, Supplier<T> supplier, Object... conditions) {
        return RegHelper.reg(itemReg, name, supplier, conditions);
    }

    public DeferredHolder<Item, Item> regFood(String name, Item.Properties properties, String tooltip, Object[] styles, Object... conditions) {
        return reg(name, () -> new FoodItem(properties, SoundEvents.GENERIC_EAT, FoodItem.getDefaultDuration(), tooltip, styles), conditions);
    }

    public DeferredHolder<Item, Item> regFood(String name, int nutrition, float saturation, String tooltip, Object[] styles, Object... conditions) {
        FoodProperties food = (new FoodProperties.Builder()).nutrition(nutrition).saturationModifier(saturation).build();
        return regFood(name, new Item.Properties().food(food), tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Item container, SoundEvent soundEvent, String tooltip, Object[] styles, Object... conditions) {
        return reg(name, () -> new ContainerFoodItem(properties.craftRemainder(container), soundEvent, ContainerFoodItem.getDefaultDuration(), tooltip, styles), conditions);
    }

    public DeferredHolder<Item, Item> regStew(String name, Item.Properties properties, String tooltip, Object[] styles, Object... conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.BOWL, SoundEvents.GENERIC_EAT, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regDrink(String name, Item.Properties properties, String tooltip, Object[] styles, Object... conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.GLASS_BOTTLE, SoundEvents.GENERIC_DRINK, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regSyrup(String name, Item.Properties properties, String tooltip, Object[] styles, Object... conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.GLASS_BOTTLE, SoundEvents.HONEY_DRINK, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regRecord(String name, Item.Properties properties, Rarity rarity, Object... conditions) {
        return reg(name, () -> new RecordItem(properties, rarity, modId, name.replace("music_disc_", "")), conditions);
    }

    public DeferredHolder<Item, Item> regRecord(String name, Item.Properties properties) {
        return regRecord(name, properties, Rarity.RARE);
    }
}
