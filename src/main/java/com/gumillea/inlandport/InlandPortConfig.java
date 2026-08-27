package com.gumillea.inlandport;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class InlandPortConfig {
    public static final ModConfigSpec STARTUP_SPEC;
    public static final InlandPortConfig.Startup STARTUP;

    public static final ModConfigSpec COMMON_SPEC;
    public static final InlandPortConfig.Common COMMON;

    public static final ModConfigSpec CLIENT_SPEC;
    public static final InlandPortConfig.Client CLIENT;

    static {
        final Pair<InlandPortConfig.Startup, ModConfigSpec> startupSpecPair = new ModConfigSpec.Builder().configure(InlandPortConfig.Startup::new);
        STARTUP_SPEC = startupSpecPair.getRight();
        STARTUP = startupSpecPair.getLeft();

        final Pair<InlandPortConfig.Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(InlandPortConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        Pair<InlandPortConfig.Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(InlandPortConfig.Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static class Startup {
        public static ModConfigSpec.ConfigValue<List<? extends String>> DISABLED_ITEMS;
        public static ModConfigSpec.BooleanValue DISABLE_MODE;

        public static ModConfigSpec.BooleanValue ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION;
        public static ModConfigSpec.DoubleValue GENERIC_EAT_DURATION;
        public static ModConfigSpec.DoubleValue GENERIC_DRINK_DURATION;

        public static ModConfigSpec.BooleanValue ENABLE_DODGE_CHANCE;
        public static ModConfigSpec.BooleanValue ENABLE_DEBUFF_RESISTANCE;
        public static ModConfigSpec.BooleanValue ENABLE_HEALING_EFFICIENCY;
        public static ModConfigSpec.BooleanValue ENABLE_ITEM_USAGE_SPEED;


        Startup(ModConfigSpec.Builder builder) {
            builder.push("Disabling");
            DISABLED_ITEMS = builder.comment("List of item registry names to disable (e.g. 'cosmopolitan:wildberry')").defineList("disabledItems", List.of(), Object -> Object instanceof String);
            DISABLE_MODE = builder.comment("If true, items are soft-disabled (kept registered with a tooltip showing the reason for being disabled). If false, items are hard-disabled (not registered at all).").define("disableMode", true);
            builder.pop();
            builder.push("ItemUseDuration");
            ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION = builder.comment("Enables configurable durations for generic eating and drinking.").define("configurableEatDuration", true);
            GENERIC_EAT_DURATION = builder.comment("Defines the use duration for generic food.").defineInRange("genericEatDuration", 1.6D, 0, Double.MAX_VALUE);
            GENERIC_DRINK_DURATION = builder.comment("Defines the use duration for generic drink.").defineInRange("genericDrinkDuration", 2.0D, 0, Double.MAX_VALUE);
            builder.pop();
            builder.push("Attribute");
            ENABLE_DODGE_CHANCE = builder.comment("Enables the dodge chance attribute.").define("dodgeChance", true);
            ENABLE_DEBUFF_RESISTANCE = builder.comment("Enables the debuff resistance attribute.").define("debuffResistance", true);
            ENABLE_HEALING_EFFICIENCY = builder.comment("Enables the healing efficiency attribute.").define("healingEfficiency", true);
            ENABLE_ITEM_USAGE_SPEED = builder.comment("Enables the item usage speed attribute.").define("itemUsageSpeed", true);
            builder.pop();
        }
    }

    public static class Common {
        public static ModConfigSpec.IntValue PLACEABLE_FOOD_SETTING;
        Common(ModConfigSpec.Builder builder) {
            builder.push("Block");
            PLACEABLE_FOOD_SETTING = builder.comment("Defines the usage modes for placeable food (0 - Can only be eaten as an item; 1 - Can be eaten as an item, or placed as a block while sneaking; 2 - Can only be placed as a block).").defineInRange("placeableFoodSetting", 1, 0, 2);
            builder.pop();
        }
    }

    public static class Client {
        public static ModConfigSpec.BooleanValue ENABLE_EFFECT_TOOLTIP;
        public static ModConfigSpec.BooleanValue ENABLE_DISABLE_REASON_TOOLTIP;
        public static ModConfigSpec.BooleanValue ENABLE_DISABLE_OVERLAY;

        Client(ModConfigSpec.Builder builder) {
            ENABLE_EFFECT_TOOLTIP = builder.comment("Allows food items to display their mob effects in tooltips.").define("effectTooltip",  true);
            ENABLE_DISABLE_REASON_TOOLTIP = builder.comment("Allows disabled items to display the reason for being disabled in tooltips.").define("disableReasonTooltip",  true);
            ENABLE_DISABLE_OVERLAY = builder.comment("Enables the icon overlay displayed for disabled items.").define("disabledItemOverlay",  true);
        }
    }
}
