package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.api.ITooltipItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class IPItem extends Item implements ITooltipItem {

    public final String tooltip;
    public final Object[] styles;

    protected final List<BiConsumer<ItemStack, LivingEntity>> onFinishUsingItem = new ArrayList<>();

    public IPItem(Properties properties, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties);
        this.tooltip = tooltip;
        this.styles = tooltip == null ? null : styles;
    }

    public IPItem finishAction(BiConsumer<ItemStack, LivingEntity> action) {
        onFinishUsingItem.add(action);
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack result = super.finishUsingItem(stack, level, living);

        if (!level.isClientSide()) {
            for (BiConsumer<ItemStack, LivingEntity> action : onFinishUsingItem) {
                action.accept(result, living);
            }
        }

        return result;
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
