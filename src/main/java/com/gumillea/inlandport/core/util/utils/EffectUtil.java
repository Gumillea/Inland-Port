package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectUtil {

    public static void add(Entity entity, Holder<MobEffect> effect, Object duration, int amplifier) {
        if (entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(effect, IPUtil.toTicks(duration), amplifier));
    }

    public static void add(Entity entity, Holder<MobEffect> effect, Object duration) {
        add(entity, effect, duration, 0);
    }

    public static void removeAllBeneficial(LivingEntity living) {
        living.getActiveEffects().stream().map(MobEffectInstance::getEffect).filter(effect -> effect.value().isBeneficial()).toList().forEach(living::removeEffect);
    }

    public static void removeAllHarmful(LivingEntity living) {
        living.getActiveEffects().stream().map(MobEffectInstance::getEffect).filter(EffectUtil::isHarmful).toList().forEach(living::removeEffect);
    }

    public static void removeAllNeutral(LivingEntity living) {
        living.getActiveEffects().stream().map(MobEffectInstance::getEffect).filter(EffectUtil::isNeutral).toList().forEach(living::removeEffect);
    }

    public static void adjustDuration(LivingEntity living, MobEffectInstance inst, int i) {
        MobEffectInstance newInst = new MobEffectInstance(inst.getEffect(), Math.max(inst.getDuration() + i, 20), inst.getAmplifier(), inst.isAmbient(), inst.isVisible(), inst.showIcon());
        living.forceAddEffect(newInst, null);
    }

    public static void adjustAmplifier(LivingEntity living, MobEffectInstance inst, int i) {
        MobEffectInstance newInst = new MobEffectInstance(inst.getEffect(), inst.getDuration(), Math.max(inst.getAmplifier() + i, 0), inst.isAmbient(), inst.isVisible(), inst.showIcon());
        living.forceAddEffect(newInst, null);
    }

    public static void covert(LivingEntity living, Holder<MobEffect> oldEffect, Holder<MobEffect> newEffect) {
        if (living.hasEffect(oldEffect)) {
            living.addEffect(new MobEffectInstance(newEffect, living.getEffect(oldEffect).getDuration(), living.getEffect(oldEffect).getAmplifier()));
            living.removeEffect(oldEffect);
        }
    }

    public static boolean isHarmful(Holder<MobEffect> effect) {
        return effect.value().getCategory() == MobEffectCategory.HARMFUL;
    }

    public static boolean isNeutral(Holder<MobEffect> effect) {
        return effect.value().getCategory() == MobEffectCategory.NEUTRAL;
    }

    public static boolean isInfinite(MobEffectInstance inst) {
        return inst.getDuration() <= -1;
    }
}
