package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

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

    public static void swap(LivingEntity living, Holder<MobEffect> EffectA, Holder<MobEffect> EffectB) {
        if (living.hasEffect(EffectA) && (living.hasEffect(EffectA))) {
            living.forceAddEffect(new MobEffectInstance(EffectA, living.getEffect(EffectB).getDuration(), living.getEffect(EffectB).getAmplifier()), null);
            living.forceAddEffect(new MobEffectInstance(EffectB, living.getEffect(EffectA).getDuration(), living.getEffect(EffectA).getAmplifier()), null);
        }
    }

    public static void swapDuration(LivingEntity living, Holder<MobEffect> EffectA, Holder<MobEffect> EffectB) {
        if (living.hasEffect(EffectA) && (living.hasEffect(EffectA))) {
            living.forceAddEffect(new MobEffectInstance(EffectA, living.getEffect(EffectB).getDuration(), living.getEffect(EffectA).getAmplifier()), null);
            living.forceAddEffect(new MobEffectInstance(EffectB, living.getEffect(EffectA).getDuration(), living.getEffect(EffectB).getAmplifier()), null);
        }
    }

    public static void swapAmplifier(LivingEntity living, Holder<MobEffect> EffectA, Holder<MobEffect> EffectB) {
        if (living.hasEffect(EffectA) && (living.hasEffect(EffectA))) {
            living.forceAddEffect(new MobEffectInstance(EffectA, living.getEffect(EffectA).getDuration(), living.getEffect(EffectB).getAmplifier()), null);
            living.forceAddEffect(new MobEffectInstance(EffectB, living.getEffect(EffectB).getDuration(), living.getEffect(EffectA).getAmplifier()), null);
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

    public static ItemStack potion(Item item, Holder<Potion> potion) {
        ItemStack stack = PotionContents.createItemStack(item, potion);
        stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        stack.remove(DataComponents.ENCHANTMENTS);
        stack.remove(DataComponents.LORE);
        stack.remove(DataComponents.MAX_STACK_SIZE);
        stack.remove(DataComponents.RARITY);
        stack.remove(DataComponents.REPAIR_COST);
        return stack;
    }

    public static ItemStack potion(Holder<Potion> potion) {
        return potion(Items.POTION, potion);
    }

    public static ItemStack splashPotion(Holder<Potion> potion) {
      return potion(Items.SPLASH_POTION, potion);
    }

    public static ItemStack lingeringPotion(Holder<Potion> potion) {
        return potion(Items.LINGERING_POTION, potion);
    }

    public static ItemStack tippedArrowPotion(Holder<Potion> potion) {
        return potion(Items.TIPPED_ARROW, potion);
    }

}
