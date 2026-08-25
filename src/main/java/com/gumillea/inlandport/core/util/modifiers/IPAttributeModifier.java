package com.gumillea.inlandport.core.util.modifiers;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.test.reg.IPAttributes;
import com.gumillea.inlandport.core.util.utils.AttrUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class IPAttributeModifier {

    public static void addAttributeModifiers() {
        addAttributeModifier();
    }

    private static void addAttributeModifier() {
        AttrUtil.add(MobEffects.HEALTH_BOOST, IPAttributes.HEALING_EFFICIENCY, InlandPort.MODID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AttrUtil.add(MobEffects.WEAKNESS, IPAttributes.HEALING_EFFICIENCY, InlandPort.MODID, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        AttrUtil.add(MobEffects.DAMAGE_RESISTANCE, IPAttributes.DEBUFF_RESISTANCE, InlandPort.MODID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        AttrUtil.add(MobEffects.MOVEMENT_SPEED, IPAttributes.DODGE_CHANCE, InlandPort.MODID, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AttrUtil.add(MobEffects.LUCK, IPAttributes.DODGE_CHANCE, InlandPort.MODID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AttrUtil.add(MobEffects.MOVEMENT_SLOWDOWN, IPAttributes.DODGE_CHANCE, InlandPort.MODID, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AttrUtil.add(MobEffects.UNLUCK, IPAttributes.DODGE_CHANCE, InlandPort.MODID, -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        AttrUtil.add(MobEffects.DIG_SPEED, IPAttributes.ITEM_USAGE_SPEED, InlandPort.MODID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AttrUtil.add(MobEffects.DIG_SLOWDOWN, IPAttributes.ITEM_USAGE_SPEED, InlandPort.MODID, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
