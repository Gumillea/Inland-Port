package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.api.ITooltipItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class TooltipItem extends Item implements ITooltipItem {

    public final String tooltip;
    public final Object[] styles;

    public TooltipItem(Properties properties, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties);
        this.tooltip = tooltip;
        this.styles = styles;
    }

    @Override
    public boolean hasTooltip() {
        return tooltip != null;
    }

    @Override
    public String getTooltip() {
        return hasTooltip() ? tooltip : null;
    }

    @Override
    public @Nullable Object[] getStyles() {
        return styles;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (!InlandPortConfig.Client.ENABLE_EFFECT_TOOLTIP.get()) return;
        this.appendTooltip(stack, components);
    }
}
