package com.gumillea.inlandport;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.logging.LogManager;

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
        public static ModConfigSpec.ConfigValue<List<? extends String>> DISABLED;
        public static ModConfigSpec.BooleanValue DISABLE_MODE;

        public static ModConfigSpec.DoubleValue GENERIC_EAT_DURATION;
        public static ModConfigSpec.DoubleValue GENERIC_DRINK_DURATION;

        Startup(ModConfigSpec.Builder builder) {
            DISABLED = builder.comment("List of item registry names to disable (e.g. 'cosmopolitan:wildberry')").defineList("disabled", List.of(), Object -> Object instanceof String);
            DISABLE_MODE = builder.comment("If true, items are hard-disabled (not registered at all). If false, soft-disabled (converted to apple on pickup, kept visible in JEI with tooltip).").define("useHardDisable", true);

            GENERIC_EAT_DURATION = builder.comment("Defines the interval in ticks between each Gulime regeneration check").defineInRange("genericEatDuration", 1.6D, 0, Double.MAX_VALUE);
            GENERIC_DRINK_DURATION = builder.comment("Defines the interval in ticks between each Gulime regeneration check").defineInRange("genericDrinkDuration", 2.0D, 0, Double.MAX_VALUE);
        }
    }

    public static class Common {
        public static ModConfigSpec.IntValue PLACEABLE_FOOD_SETTING;
        public static ModConfigSpec.BooleanValue ENABLE_INLAND_PORT_ATTRIBUTES;
        public static ModConfigSpec.BooleanValue ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION;

        Common(ModConfigSpec.Builder builder) {
            PLACEABLE_FOOD_SETTING = builder.comment("Defines the usage modes for placeable food (0 - Can only be eaten as an item; 1 - Can be eaten as an item, or placed as a block while sneaking; 2 - Can only be placed as a block).").defineInRange("placeableFoodSetting", 1, 0, 2);
            ENABLE_INLAND_PORT_ATTRIBUTES = builder.comment("Allows Gulime and its biome varieties.").define("inlandPortAttributes", true);
            ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION = builder.comment("Allows Gulime and its biome varieties.").define("gulime", true);
        }
    }

    public static class Client {
        public static ModConfigSpec.BooleanValue EFFECT_TOOLTIP;

        Client(ModConfigSpec.Builder builder) {
            EFFECT_TOOLTIP = builder.comment("Allows food items to display their mob effects in tooltips.").define("effectTooltip",  true);
        }
    }
}
