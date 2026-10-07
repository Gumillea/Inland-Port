package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

public class SimpleHatItem extends Item implements Equipable {

    public SimpleHatItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemAttributeModifiers createAttributes(Holder<Attribute> attribute, int amount) {
        return ItemAttributeModifiers.builder().add(attribute, new AttributeModifier(IPUtil.loc(InlandPort.MODID, RegUtil.path(attribute.value()) + ".hat"), amount, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD).build();
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity living) {
        return slot == EquipmentSlot.HEAD;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        return swapWithEquipmentSlot(this, p_40395_, p_40396_, p_40397_);
    }

    public Holder<SoundEvent> getEquipSound() {
        return ArmorMaterials.LEATHER.value().equipSound();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
