package com.gumillea.inlandport.core.util.utils;

import com.gumillea.inlandport.core.api.ITooltipItem;
import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class ClientUtil {

    public static void addTip(ItemStack stack, List<Component> components) {
        Item item = stack.getItem();
        if (RegHelper.isDisabled(item)) return;
        if (item instanceof ITooltipItem tooltipItem && tooltipItem.hasTooltip()) {
            ResourceLocation key = RegUtil.key(item);
            Object[] styles = tooltipItem.getStyles();
            String tooltip = tooltipItem.getTooltip();
            MutableComponent component = tooltip.startsWith("tooltip.") ? Component.translatable(tooltip) : Component.translatable("tooltip." + key.getNamespace() + "." + key.getPath());
            if (styles != null) {
                for (Object style : styles) {
                    switch (style) {
                        case ChatFormatting formatting -> component = component.withStyle(formatting);
                        case Style s -> component = component.withStyle(s);
                        case Integer i -> component = component.withColor(i);
                        default -> throw new IllegalStateException("Unexpected value: " + style);
                    }
                }
                components.add(component);
            } else {
                components.add(component.withStyle(ChatFormatting.BLUE));
            }

        }
    }

    public static void addShiftableTip(List<Component> tooltip, Component outer, Component inner) {
        if (Screen.hasShiftDown()) {
            tooltip.add(inner);
        } else {
            tooltip.add(outer);
        }
    }

    public static void addEffectTip(ItemStack stack, Consumer<Component> consumer) {
        FoodProperties properties = stack.get(DataComponents.FOOD);

        if (properties == null || properties.effects().isEmpty()) return;

        for (FoodProperties.PossibleEffect possibleEffect : properties.effects()) {
            MobEffectInstance effect = possibleEffect.effect();
            float probability = possibleEffect.probability();
            MutableComponent component = Component.translatable(effect.getDescriptionId());
            Holder<MobEffect> holder = effect.getEffect();

            if (effect.getAmplifier() > 0) {
                component = Component.translatable("potion.withAmplifier", component, Component.translatable("potion.potency." + effect.getAmplifier()));
            }

            if (!effect.endsWithin(20)) {
                component = Component.translatable("potion.withDuration", component, MobEffectUtil.formatDuration(effect, 1.0F, 20.0F));
            }

            if (probability < 1.0F) {
                component.append(Component.literal(" (" + Math.round(probability * 100) + "%)"));
            }

            consumer.accept(component.withStyle(holder.value().getCategory().getTooltipFormatting()));
        }
    }

    public static void addClientMessage(Player player, String modId, String message, boolean actionBar) {
        player.displayClientMessage(Component.translatable("message."+ modId + "." + message), actionBar);
    }

    public static void addClientMessage(Player player, String modId, String message) {
        addClientMessage(player, modId, message, true);
    }

}


