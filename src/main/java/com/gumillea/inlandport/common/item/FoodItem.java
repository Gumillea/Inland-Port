package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.utils.ClientUtil;
import com.gumillea.inlandport.core.util.utils.EffectUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodItem extends IPItem {
    public final SoundEvent sound;
    public final Object duration;
    public final Type type;

    protected List<Holder<MobEffect>> cleans = new ArrayList<>();
    protected List<Holder<MobEffect>> coverts = new ArrayList<>();
    protected List<Holder<MobEffect>> enhances = new ArrayList<>();

    public FoodItem(Properties properties, SoundEvent sound, Object duration, Type type, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties, tooltip, styles);
        this.sound = sound;
        this.duration = duration;
        this.type = type;
    }

    public FoodItem(Properties properties, FoodItem.Type type) {
        this(properties, SoundEvents.GENERIC_EAT, getDefaultDuration(), type, null);
    }

    public FoodItem cleans(Holder<MobEffect> effect) {
        onFinishUsingItem.add((stack, living) -> living.removeEffect(effect));
        cleans.add(effect);
        return this;
    }

    public FoodItem cleansByCategory(MobEffectCategory category, int count) {
        onFinishUsingItem.add((stack, living) -> {
           List<Holder<MobEffect>> effects = living.getActiveEffects().stream().map(MobEffectInstance::getEffect).filter(effect -> category == null || effect.value().getCategory().equals(category)).toList();
           if (effects.isEmpty()) return;

           int size = effects.size();
           int finalCount = count <= 0 ? size : Math.min(count, size);

           for(int i = 0; i < finalCount; ++i) {
               living.removeEffect(effects.get(living.getRandom().nextInt(size)));
            }

        });
        return this;
    }

    public FoodItem cleansAll() {
        cleansByCategory(null, 0);
        return this;
    }

    public FoodItem coverts(Holder<MobEffect> effect, Holder<MobEffect> newEffect) {
        onFinishUsingItem.add((stack, living) -> {
            if (living.hasEffect(effect)) {
                int a = Objects.requireNonNull(living.getEffect(effect)).getAmplifier();
                int d = Objects.requireNonNull(living.getEffect(effect)).getDuration();

                living.addEffect(new MobEffectInstance(newEffect, d, a));
                living.removeEffect(effect);
            }
        });
        return this;
    }

    public FoodItem enhances(Holder<MobEffect> effect) {
        onFinishUsingItem.add((stack, living) -> {
            if (living.hasEffect(effect)) {
                MobEffectInstance inst = living.getEffect(effect);

                if (inst == null) return;

                if (inst.getAmplifier() < 2) {
                    EffectUtil.adjustAmplifier(living, inst, 1);
                } else if (inst.getDuration() < 1200) {
                    EffectUtil.adjustDuration(living, inst, 600);
                }

            } else {
                living.addEffect(new MobEffectInstance(effect, 600, 0));
            }
        });
        return this;
    }

    public FoodItem.Type getType() {
        return type;
    }

    public int getUseDuration(ItemStack stack, LivingEntity living) {
        int ticks = IPUtil.toTicks(duration);
        return ticks == (defaultDuration() * 20) ? super.getUseDuration(stack,living) : ticks;
    }

    public SoundEvent getEatingSound() {
        return sound;
    }

    private static float defaultDuration() {
        return InlandPortConfig.Startup.ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION.get() ? InlandPortConfig.Startup.GENERIC_EAT_DURATION.get().floatValue() : 1.6F;
    }

    public static float getDefaultDuration() {
        return defaultDuration();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);
        ClientUtil.addEffectTip(stack, components::add);


    }

    public enum Type {
        VEGETABLE,
        GRAIN,
        NUT,

        RAW_MEAT,
        COOKED_MEAT,

        RAW_FISH,
        COOKED_FISH,

        FRUIT,
        BERRY,

        COOKIE,
        BREAD,
        DESSERT,
        SLICE,

        MEAL,
        SNACK,

        SOUP,
        DRINK,
        SYRUP,

        MAGIC,
        MAGIC_APPLE,
        MISC
    }

}
