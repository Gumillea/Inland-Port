package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.item.ContainerFoodItem;
import com.gumillea.inlandport.common.item.FoodItem;
import com.gumillea.inlandport.common.item.IPArmorItem;
import com.gumillea.inlandport.common.item.RecordItem;
import com.gumillea.inlandport.core.api.record.RegConditions;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemHelper {

    private final DeferredRegister<Item> itemReg;
    protected final String modId;

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

    public DeferredHolder<Item, Item> regAxeItem(String name, Item.Properties properties, Tier tier, int damage, float attackSpeed, RegConditions conditions) {
        return reg(name, () -> new AxeItem(tier, properties.attributes(AxeItem.createAttributes(tier, damage, attackSpeed))), conditions);
    }

    public DeferredHolder<Item, Item> regSwordItem(String name, Item.Properties properties, Tier tier, int damage, float attackSpeed, RegConditions conditions) {
        return reg(name, () -> new SwordItem(tier, properties.attributes(SwordItem.createAttributes(tier, damage, attackSpeed))), conditions);
    }

    public DeferredHolder<Item, Item> regShovelItem(String name, Item.Properties properties, Tier tier, int damage, float attackSpeed, RegConditions conditions) {
        return reg(name, () -> new ShovelItem(tier, properties.attributes(ShovelItem.createAttributes(tier, damage, attackSpeed))), conditions);
    }

    public DeferredHolder<Item, Item> regPickaxeItem(String name, Item.Properties properties, Tier tier, int damage, float attackSpeed, RegConditions conditions) {
        return reg(name, () -> new PickaxeItem(tier, properties.attributes(PickaxeItem.createAttributes(tier, damage, attackSpeed))), conditions);
    }

    public DeferredHolder<Item, Item> regHoeItem(String name, Item.Properties properties, Tier tier, int damage, float attackSpeed, RegConditions conditions) {
        return reg(name, () -> new HoeItem(tier, properties.attributes(HoeItem.createAttributes(tier, damage, attackSpeed))), conditions);
    }

    public DeferredHolder<Item, Item> regArmors(String name, Item.Properties properties, DeferredHolder<ArmorMaterial, ArmorMaterial> material, int[] durability, Consumer<ItemAttributeModifiers.Builder> consumer, RegConditions conditions) {
        reg(name + "_helmet", () -> new IPArmorItem(name, modId, material, ArmorItem.Type.HELMET, properties.durability(durability[0]), consumer), conditions);
        reg(name + "_chestplate", () -> new IPArmorItem(name, modId, material, ArmorItem.Type.CHESTPLATE, properties.durability(durability[1]), consumer), conditions);
        reg(name + "_leggings", () -> new IPArmorItem(name, modId, material, ArmorItem.Type.LEGGINGS, properties.durability(durability[2]), consumer), conditions);
        return reg(name + "_boots", () -> new IPArmorItem(name, modId, material, ArmorItem.Type.BOOTS, properties.durability(durability[3]), consumer), conditions);
    }

    public DeferredHolder<Item, Item> regArmors(String name, Item.Properties properties, DeferredHolder<ArmorMaterial, ArmorMaterial> material, int[] durability, RegConditions conditions) {
        return regArmors(name,  properties, material, durability, null, conditions);
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

    public DeferredHolder<Item, Item> regUpgradeSmithingTemplate(String name, @Nullable String material, RegConditions conditions) {
        String base = "smithing_template." + name.replace("_smithing_template", ".");
        Component upgrade = Component.translatable(Util.makeDescriptionId("upgrade", IPUtil.loc(modId, name.replace("_smithing_template", "")))).withStyle(ChatFormatting.GRAY);
        Component appliesTo = Component.translatable(Util.makeDescriptionId("item", IPUtil.loc(modId, base + "applies_to"))).withStyle(ChatFormatting.BLUE);
        Component ingredients = Component.translatable(Util.makeDescriptionId("item", IPUtil.loc(modId, base + "ingredients"))).withStyle(ChatFormatting.BLUE);
        Component base_slot_description = Component.translatable(Util.makeDescriptionId("item", IPUtil.loc(modId, base + "base_slot_description")));
        Component additions_slot_description = Component.translatable(Util.makeDescriptionId("item", IPUtil.loc(modId, base + "additions_slot_description")));
        return reg(name, () -> new SmithingTemplateItem(appliesTo, ingredients, upgrade, base_slot_description, additions_slot_description, createIconList(material != null ? material : name), List.of(IPUtil.mcLoc("item/empty_armor_slot"))), conditions);
    }

    public DeferredHolder<Item, Item> regUpgradeSmithingTemplate(String name, RegConditions conditions) {
       return regUpgradeSmithingTemplate(name, null, conditions);
    }

    public DeferredHolder<Item, Item> regUpgradeSmithingTemplate(String name) {
        return regUpgradeSmithingTemplate(name, null, null);
    }

    private List<ResourceLocation> createIconList(String material) {
        List<String> armor_slots = List.of("_helmet", "_chestplate", "_leggings", "_boots");
        List<String> slots = List.of("_sword", "_shovel", "_axe", "_pickaxe", "_hoe");
        List<ResourceLocation> list = new ArrayList<>();

        for (String slot : armor_slots) {
            if (CompatUtil.item(modId, material + slot) != null) list.add(IPUtil.mcLoc("item/empty_armor_slot" + slot));
        }

        for (String slot : slots) {
            if (CompatUtil.item(modId, material + slot) != null) list.add(IPUtil.mcLoc("item/empty_slot" + slot));
        }

        return list;
    }

}
