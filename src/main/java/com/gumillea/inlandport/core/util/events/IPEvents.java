package com.gumillea.inlandport.core.util.events;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.tags.IPDamageTypeTags;
import com.gumillea.inlandport.core.util.utils.*;
import com.gumillea.inlandport.test.reg.IPAttributes;
import com.gumillea.inlandport.test.reg.IPSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = InlandPort.MODID)
public class IPEvents {

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, IPAttributes.ITEM_USAGE_SPEED);

        event.getTypes().forEach(type -> {
            event.add(type, IPAttributes.DODGE_CHANCE);
            event.add(type, IPAttributes.DEBUFF_RESISTANCE);
            event.add(type, IPAttributes.HEALING_EFFICIENCY);
        });
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Level level = event.getEntity().level();
        if (event.getSource().is(IPDamageTypeTags.BYPASSES_DODGE)) return;
        if (AttrUtil.has(target, IPAttributes.DODGE_CHANCE) && IPUtil.random(level, IPAttributes.getDodgeChance(target))) {
            LivingDodgedEvent dodgeEvent = new LivingDodgedEvent(target, event.getContainer());
            NeoForge.EVENT_BUS.post(dodgeEvent);

            if (!dodgeEvent.isCanceled()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDodged(LivingDodgedEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.level().isClientSide()) {
            EntityUtil.playSound(entity, IPSoundEvents.ATTACK_MISS);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity living = event.getEntity();
        Level level = living.level();
        MobEffectInstance inst = event.getEffectInstance();
        Holder<MobEffect> effect = inst.getEffect();
        int duration = inst.getDuration();

        if (EffectUtil.isHarmful(effect) && !EffectUtil.isInfinite(inst) && AttrUtil.has(living, IPAttributes.DEBUFF_RESISTANCE)) {
            if (level instanceof ServerLevel serverLevel) {
                double dr = IPAttributes.getDebuffResistance(living);
                EventUtil.schedule(serverLevel, 1, () -> {
                    if (living.isAlive()) {
                        if (dr >= 1) {
                            if (!level.isClientSide()) {
                                EntityUtil.playSound(living, IPSoundEvents.DEBUFF_IMMUNE);
                            }
                            living.removeEffect(effect);
                        } else {
                            EffectUtil.adjustDuration(living, inst, (int) -(duration * (IPAttributes.getDebuffResistance(living))));
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHealed(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        float amount = event.getAmount();

        if (AttrUtil.has(living, IPAttributes.HEALING_EFFICIENCY)) {
            double he = IPAttributes.getHealingEfficiency(living);
            if (he <= 0) {
                event.setCanceled(true);
            } else {
                event.setAmount((float) (amount * he));
            }
        }

        float overAmount = (amount + living.getHealth()) - living.getMaxHealth();
        if (overAmount > 0) {
            NeoForge.EVENT_BUS.post(new LivingOverHealEvent(living, overAmount));
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        EventUtil.entityInteract(event, e -> e instanceof Cat, i -> i.is(Items.APPLE), () -> {
            EffectUtil.add(target, MobEffects.WEAKNESS, 100);
        });
    }

}

