package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.item.ContainerFoodItem;
import com.gumillea.inlandport.common.item.FoodItem;
import com.gumillea.inlandport.common.item.RecordItem;
import com.gumillea.inlandport.core.api.record.RegConditions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

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

    public <T extends Item> DeferredHolder<Item, T> reg(String name, Supplier<T> supplier, Supplier<T> altsupplier, @Nullable RegConditions conditions) {
        return RegHelper.reg(itemReg, name, supplier, altsupplier, conditions);
    }

    public DeferredHolder<Item, Item> reg(String name, Supplier<Item> supplier, RegConditions conditions) {
        return RegHelper.reg(itemReg, name, supplier, () -> new Item(new Item.Properties()), conditions);
    }

    public DeferredHolder<Item, Item> reg(String name, Supplier<Item> supplier) {
        return RegHelper.reg(itemReg, name, supplier, () -> new Item(new Item.Properties()), null);
    }

    public DeferredHolder<Item, Item> regFood(String name, Item.Properties properties, FoodItem.Type type, String tooltip, Object[] styles, RegConditions conditions) {
        return reg(name, () -> new FoodItem(properties, SoundEvents.GENERIC_EAT, FoodItem.getDefaultDuration(), type, tooltip, styles), conditions);
    }

    public DeferredHolder<Item, Item> regFood(String name, Item.Properties properties, FoodItem.Type type, RegConditions conditions) {
        return regFood(name, properties, type, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regFood(String name, Item.Properties properties, FoodItem.Type type) {
        return regFood(name, properties, type, null);
    }

    public DeferredHolder<Item, Item> regFood(String name, int nutrition, float saturation, FoodItem.Type type, RegConditions conditions) {
        FoodProperties food = (new FoodProperties.Builder()).nutrition(nutrition).saturationModifier(saturation).build();
        return regFood(name, new Item.Properties().food(food), type, conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Item container, SoundEvent soundEvent, FoodItem.Type type, String tooltip, Object[] styles, RegConditions conditions) {
        return reg(name, () -> new ContainerFoodItem(properties.craftRemainder(container), soundEvent, ContainerFoodItem.getDefaultDuration(), type, tooltip, styles), conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Item container, FoodItem.Type type, RegConditions conditions) {
        return regContainerFood(name, properties, container, SoundEvents.GENERIC_EAT, type, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Item container, FoodItem.Type type) {
        return regContainerFood(name, properties, container, type, null);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Supplier<Item> container, SoundEvent soundEvent, FoodItem.Type type, String tooltip, Object[] styles, RegConditions conditions) {
        return reg(name, () -> new ContainerFoodItem(properties.craftRemainder(container.get()), soundEvent, ContainerFoodItem.getDefaultDuration(), type, tooltip, styles), conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, Supplier<Item> container, FoodItem.Type type, RegConditions conditions) {
        return regContainerFood(name, properties, container, SoundEvents.GENERIC_EAT, type, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regContainerFood(String name, Item.Properties properties, FoodItem.Type type, Supplier<Item> container) {
        return regContainerFood(name, properties, container, SoundEvents.GENERIC_EAT, type, null, null, null);
    }

    public DeferredHolder<Item, Item> regBowlFood(String name, Item.Properties properties, FoodItem.Type type, String tooltip, Object[] styles, RegConditions conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.BOWL, SoundEvents.GENERIC_EAT, type, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regBowlFood(String name, Item.Properties properties, FoodItem.Type type, RegConditions conditions) {
        return regBowlFood(name, properties, type, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regBowlFood(String name, Item.Properties properties, FoodItem.Type type) {
        return regBowlFood(name, properties, type, null);
    }

    public DeferredHolder<Item, Item> regDrink(String name, Item.Properties properties, String tooltip, Object[] styles, RegConditions conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.GLASS_BOTTLE, SoundEvents.GENERIC_DRINK, FoodItem.Type.DRINK, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regDrink(String name, Item.Properties properties, RegConditions conditions) {
        return regDrink(name, properties, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regDrink(String name, Item.Properties properties) {
        return regDrink(name, properties, null, null, null);
    }

    public DeferredHolder<Item, Item> regSyrup(String name, Item.Properties properties, String tooltip, Object[] styles, RegConditions conditions) {
        return regContainerFood(name, properties.stacksTo(16), Items.GLASS_BOTTLE, SoundEvents.HONEY_DRINK, FoodItem.Type.SYRUP, tooltip, styles, conditions);
    }

    public DeferredHolder<Item, Item> regSyrup(String name, Item.Properties properties, RegConditions conditions) {
        return regSyrup(name, properties, null, null, conditions);
    }

    public DeferredHolder<Item, Item> regSyrup(String name, Item.Properties properties) {
        return regSyrup(name, properties, null);
    }

    public DeferredHolder<Item, Item> regRecord(String name, Item.Properties properties, Rarity rarity, RegConditions conditions) {
        return reg(name, () -> new RecordItem(properties, rarity, modId, name.replace("music_disc_", "")), conditions);
    }

    public DeferredHolder<Item, Item> regRecord(String name, Item.Properties properties) {
        return regRecord(name, properties, Rarity.RARE, null);
    }
}
