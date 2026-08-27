package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.InlandPortConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ContainerFoodItem extends FoodItem {

    public ContainerFoodItem(Properties properties, SoundEvent sound, float duration, @Nullable String tooltip, @Nullable Object... styles) {
        super(properties, sound, duration, tooltip, styles);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        super.finishUsingItem(stack, level, living);

        if (living instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        ItemStack remaining = this.getCraftingRemainingItem() == null ? new ItemStack(Items.GLASS_BOTTLE) : new ItemStack(this.getCraftingRemainingItem());

        if (stack.isEmpty()) {
            return remaining;
        } else {
            if (living instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(remaining)) {
                    player.drop(remaining, false);
                }
            }
            return stack;
        }
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public SoundEvent getEatingSound() {
        return sound;
    }

    public SoundEvent getDrinkingSound() {
        return sound;
    }

    private static float defaultDuration() {
        return InlandPortConfig.Startup.ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION.get() ? InlandPortConfig.Startup.GENERIC_DRINK_DURATION.get().floatValue() : 2F;
    }

    public static float getDefaultDuration() {
        return defaultDuration();
    }

}

