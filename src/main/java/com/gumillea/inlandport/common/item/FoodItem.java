package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.common.block.EdibleBlock;
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
    public final Type type;

    public FoodItem(Properties properties, SoundEvent sound, Object duration, Type type, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties, tooltip, styles);
        this.sound = sound;
        this.duration = duration;
        this.type = type;
    }

    public FoodItem.Type getType() {
        return type;
    }

    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return IPUtil.toTicks(duration);
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

        RAW_MEAT,
        COOKED_MEAT,

        RAW_FISH,
        COOKED_FISH,

        FRUIT,
        BERRY,

        COOKIE,
        BREAD,
        DESSERT,

        MEAL,
        SOUP,
        DRINK,
        SYRUP,

        GOLDEN,
        SUPER_APPLE,
        MISC
    }

}
