package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.helpers.reg.AttrHelper;
import com.gumillea.inlandport.core.util.helpers.reg.BlockHelper;
import com.gumillea.inlandport.core.util.utils.AttrUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IPAttributes {
    public static final AttrHelper HELPER = new AttrHelper(InlandPort.MODID);

    public static final DeferredHolder<Attribute, Attribute> DODGE_CHANCE = HELPER.regRanged("dodge_chance", 1.0, 0, 2.0);
    public static final DeferredHolder<Attribute, Attribute> DEBUFF_RESISTANCE = HELPER.regRanged("debuff_resistance", 1.0, 0, 2.0);
    public static final DeferredHolder<Attribute, Attribute> HEALING_EFFICIENCY = HELPER.regRanged("healing_efficiency", 1.0, 0, 1024.0);
    public static final DeferredHolder<Attribute, Attribute> ITEM_USAGE_SPEED = HELPER.regRanged("item_usage_speed", 1.0, 0, 1024.0);

    public static double getDodgeChance(LivingEntity living) {
        return AttrUtil.get(living, DODGE_CHANCE) - 1;
    }

    public static double getDebuffResistance(LivingEntity living) {
        return AttrUtil.get(living, DEBUFF_RESISTANCE) - 1;
    }

    public static double getHealingEfficiency(LivingEntity living) {
        return AttrUtil.get(living, HEALING_EFFICIENCY);
    }

    public static double getItemUsageSpeed(LivingEntity living) {
        return AttrUtil.get(living, ITEM_USAGE_SPEED);
    }
}
