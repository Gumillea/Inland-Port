package com.gumillea.inlandport.core.api;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.utils.ClientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface ITooltipItem {

    @Nullable
    String getTooltip();

    @Nullable
    Object[] getStyles();

    default boolean hasTooltip() {
        return getTooltip() != null;
    }

    default void appendTooltip(ItemStack stack, List<Component> components) {
        if (!InlandPortConfig.Client.ENABLE_EFFECT_TOOLTIP.get()) return;
        ClientUtil.addTip(stack, components);
    }
}
