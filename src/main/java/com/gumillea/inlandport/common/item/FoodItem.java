package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.utils.ClientUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FoodItem extends TooltipItem {
    public final SoundEvent sound;
    public final Object duration;

    public FoodItem(Properties properties, SoundEvent sound, Object duration, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties, tooltip, styles);
        this.sound = sound;
        this.duration = duration;
    }

    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return IPUtil.toTicks(duration);
    }

    public SoundEvent getEatingSound() {
        return sound;
    }

    private static float defaultDuration() {
        return InlandPortConfig.Startup.GENERIC_EAT_DURATION.get().floatValue();
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

}
