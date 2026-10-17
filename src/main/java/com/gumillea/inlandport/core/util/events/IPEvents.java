package com.gumillea.inlandport.core.util.events;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.helpers.reg.CreativeTabHelper;
import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import com.gumillea.inlandport.core.util.tags.IPDamageTypeTags;
import com.gumillea.inlandport.core.util.utils.*;
import com.gumillea.inlandport.test.reg.IPAttributes;
import com.gumillea.inlandport.test.reg.IPNetworkHandler;
import com.gumillea.inlandport.test.reg.IPSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = InlandPort.MODID)
public class IPEvents {

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        if (RegHelper.isHardMode()) return;
        if (event.getEntity().level().isClientSide()) return;

        var container = event.getContainer();
        for (int i = 0; i < container.slots.size(); i++) {
            var slot = container.getSlot(i);
            ItemStack stack = slot.getItem();

            if (!stack.isEmpty() && RegHelper.isDisabled(stack)) {
                slot.set(RegUtil.stack(Items.APPLE, stack.getCount()));
                slot.setChanged();
            }
        }
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        if (RegHelper.isHardMode()) return;
        if (event.getPlayer().level().isClientSide()) return;

        ItemEntity entity = event.getItemEntity();
        ItemStack stack = entity.getItem();

        if (RegHelper.isDisabled(stack)) {
            entity.setItem(RegUtil.stack(Items.APPLE, stack.getCount()));
        }
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        if (InlandPortConfig.Startup.ENABLE_ITEM_USAGE_SPEED.get()) event.add(EntityType.PLAYER, IPAttributes.ITEM_USAGE_SPEED);

        event.getTypes().forEach(type -> {
            if (InlandPortConfig.Startup.ENABLE_DODGE_CHANCE.get()) event.add(type, IPAttributes.ACCURACY_RATE);
            if (InlandPortConfig.Startup.ENABLE_DODGE_CHANCE.get()) event.add(type, IPAttributes.DODGE_CHANCE);
            if (InlandPortConfig.Startup.ENABLE_MANA_RESISTANCE.get()) event.add(type, IPAttributes.MANA_RESISTANCE);
            if (InlandPortConfig.Startup.ENABLE_DEBUFF_RESISTANCE.get()) event.add(type, IPAttributes.DEBUFF_RESISTANCE);
            if (InlandPortConfig.Startup.ENABLE_HEALING_EFFICIENCY.get()) event.add(type, IPAttributes.HEALING_EFFICIENCY);
            if (InlandPortConfig.Startup.ENABLE_MANA_INFUSION.get()) event.add(type, IPAttributes.MANA_INFUSION);
        });
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getEntity().getLastDamageSource();
        float damage = event.getAmount();
        Level level = event.getEntity().level();
        if (event.getSource().is(IPDamageTypeTags.BYPASSES_DODGE)) return;

        if (AttrUtil.has(target, IPAttributes.DODGE_CHANCE) && IPUtil.random(level, IPAttributes.getDodgeChance(target))) {
            LivingDodgedEvent dodgeEvent = new LivingDodgedEvent(target, event.getContainer());
            NeoForge.EVENT_BUS.post(dodgeEvent);

            if (!dodgeEvent.isCanceled()) {
                event.setCanceled(true);
            }
        }

        if (source != null && source.is(DamageTypes.MAGIC) && AttrUtil.has(target, IPAttributes.MANA_RESISTANCE)) {
            event.setAmount((float) (damage * (1 - IPAttributes.getManaResistance(target))));
        }
    }

    @SubscribeEvent
    public static void onLivingDodged(LivingDodgedEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.level().isClientSide()) {
            EntityUtil.playSound(entity, IPSoundEvents.ATTACK_MISS);
        }
    }

    private static final String MI_COOLDOWN_KEY = InlandPort.MODID + "_mana_infusion_cd";
    private static final int MI_COOLDOWN_TICKS = 40;

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();

        if (level.isClientSide()) return;

        DamageSource source = event.getSource();

        if (source.getEntity() instanceof LivingEntity attacker) {
            if (!source.is(DamageTypes.MAGIC) && target.isAlive() && AttrUtil.has(attacker, IPAttributes.MANA_INFUSION) && IPAttributes.getManaInfusion(attacker) > 0) {
                long gameTime = level.getGameTime();
                CompoundTag compoundTag = attacker.getPersistentData();

                if (compoundTag.contains(MI_COOLDOWN_KEY)) {
                    long lastTriggerTime = compoundTag.getLong(MI_COOLDOWN_KEY);
                    if (gameTime - lastTriggerTime < MI_COOLDOWN_TICKS) return;
                }

                target.hurt(attacker.damageSources().magic(), (float) IPAttributes.getManaInfusion(attacker) * 2);

                if (level instanceof ServerLevel serverLevel) {
                    RandomSource random = level.getRandom();

                    for (int i = 0; i < 5; ++i) {
                        serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX() + random.nextDouble(), target.getEyeY(), target.getZ() + random.nextDouble(), 1, (random.nextDouble() - 0.5) * 0.3, (random.nextDouble() - 0.8) * 0.1, (random.nextDouble() - 0.5) * 0.3, 0.05);
                    }
                }

                compoundTag.putLong(MI_COOLDOWN_KEY, gameTime);
            }
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
            double dr = IPAttributes.getDebuffResistance(living);
            EventUtil.schedule(level, 1, () -> {
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
    public static void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeTabHelper.autoInsert(event, InlandPort.MODID);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendPacket(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sendPacket(event.getEntity());
    }

    private static void sendPacket(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            for (String tag : player.getTags()) {
                if (tag.startsWith(InlandPort.MODID)) IPNetworkHandler.sendEntityTagPacket(player, tag);
            }
        }
    }

}

