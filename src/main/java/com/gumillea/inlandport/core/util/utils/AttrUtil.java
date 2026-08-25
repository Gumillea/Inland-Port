package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttrUtil {
    public static boolean has(LivingEntity living, Holder<Attribute> attribute) {
        return living.getAttributes().hasAttribute(attribute);
    }

    public static double get(LivingEntity living, Holder<Attribute> attribute) {
        return has(living, attribute) ? living.getAttributeValue(attribute) : 0;
    }

    public static void add(Holder<MobEffect> effect, Holder<Attribute> attribute, String mod, double d, AttributeModifier.Operation operation) {
        effect.value().addAttributeModifier(attribute, IPUtil.loc(mod, RegUtil.path(effect.value()) + "_" + RegUtil.path(attribute.value())), d, operation);
    }
}
