package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.item.EdibleBlockItem;
import com.gumillea.inlandport.common.item.FoodItem;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CreativeTabHelper {
    private final DeferredRegister<CreativeModeTab> cTReg;
    private final String modId;

    public CreativeTabHelper(String modId) {
        this.modId = modId;
        this.cTReg = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, modId);
    }

    public void register(IEventBus bus) {
        cTReg.register(bus);
    }
    
    public DeferredHolder<CreativeModeTab, CreativeModeTab> regTab(@Nullable String name, Object icon, CreativeModeTab.DisplayItemsGenerator generator) {
        String title = name == null ? "item_group." + modId : "item_group." + modId + "_" + name;
        name = name == null ? modId : modId + "_" + name;
        return cTReg.register(name, () -> CreativeModeTab.builder().title(Component.translatable(title)).icon(() -> getIcon(icon)).displayItems(generator).build());
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoTab(String name, Object icon, Predicate<Item> exclusion) {
        return regTab(name, icon, (parameters, output) -> BuiltInRegistries.ITEM.stream().filter(item -> AutoDataGeneHelper.isSame(RegUtil.key(item), modId)).filter(item -> !exclusion.test(item)).forEach(output::accept));
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoTab(String name, Object icon) {
        return regAutoTab(name, icon, RegHelper::isDisabled);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoTabWithDisabled(String name, Object icon) {
        return regAutoTab(name, icon, item -> true);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoItemTab(String name, Object icon) {
        return regAutoTab(name, icon, item -> RegHelper.isDisabled(item) || RegUtil.isBlock(item));
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoItemTabWithDisabled(String name, Object icon) {
        return regAutoTab(name, icon, RegUtil::isBlock);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoBlockTab(String name, Object icon) {
        return regAutoTab(name, icon, item -> RegHelper.isDisabled(item) || !RegUtil.isBlock(item));
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoBlockTabWithDisabled(String name, Object icon) {
        return regAutoTab(name, icon, item -> !RegUtil.isBlock(item));
    }

    private static ItemStack getIcon(Object icon) {
        switch (icon) {
            case ItemStack stack -> {
                return stack;
            }
            case Supplier<?> supplier -> {
               if (supplier.get() instanceof Item item) return RegUtil.stack(item);
               if (supplier.get() instanceof Block block) return RegUtil.stack(block.asItem());
            }
            default -> {}
        }
        return RegUtil.stack(Items.APPLE);
    }

    private static final Set<Item> ADDED = new HashSet<>();

    public static void autoInsert(BuildCreativeModeTabContentsEvent event, String modId, @Nullable Runnable task) {
        if (task != null) task.run();

        List<Item> items = new ArrayList<>(BuiltInRegistries.ITEM.stream().filter(item -> AutoDataGeneHelper.isSame(RegUtil.key(item), modId)).toList());
        Collections.reverse(items);

        for (Item item : items) {
            ItemStack stack = RegUtil.stack(item);
            if (RegHelper.isDisabled(stack)) continue;

            ResourceKey<CreativeModeTab> tab = event.getTabKey();

            if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                if (stack.is(ItemTags.BOATS)) insertAfter(event, Items.CHERRY_CHEST_BOAT, stack);
                if (stack.is(Tags.Items.MUSIC_DISCS)) insertAfter(event, Items.MUSIC_DISC_PIGSTEP, stack);
            }

            if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
                if (stack.is(ItemTags.SMALL_FLOWERS)) insertAfter(event, Items.LILY_OF_THE_VALLEY, stack);
                if (stack.is(ItemTags.TALL_FLOWERS)) insertAfter(event, Items.PEONY, stack);
                if (stack.is(Tags.Items.SEEDS)) insertAfter(event, Items.BEETROOT_SEEDS, stack);
            }

            if (tab == CreativeModeTabs.FOOD_AND_DRINKS) {
                if (stack.is(ItemTags.MEAT)) insertAfter(event, Items.COOKED_RABBIT, stack);
                if (stack.is(ItemTags.FISHES)) {
                    if (stack.is(Tags.Items.FOODS_FOOD_POISONING)) {
                        insertAfter(event, Items.PUFFERFISH, stack);
                    } else {
                        insertAfter(event, Items.TROPICAL_FISH, stack);
                    }
                }

                if (stack.is(Tags.Items.FOODS_FRUIT)) insertAfter(event, Items.MELON_SLICE, stack);
                if (stack.is(Tags.Items.FOODS_VEGETABLE)) insertAfter(event, Items.BEETROOT, stack);
                if (stack.is(Tags.Items.FOODS_BERRY)) insertAfter(event, Items.GLOW_BERRIES, stack);
                if (stack.is(Tags.Items.FOODS_FOOD_POISONING)) insertAfter(event, Items.SPIDER_EYE, stack);
                if (stack.is(Tags.Items.FOODS_COOKIE)) insertAfter(event, Items.COOKIE, stack);
                if (stack.is(Tags.Items.FOODS_BREAD)) insertAfter(event, Items.BREAD, stack);
                if (stack.is(Tags.Items.DRINKS_MILK)) insertAfter(event, Items.MILK_BUCKET, stack);

                switch (stack.getItem()) {
                    case FoodItem food -> {
                        switch (food.getType()) {
                            case MAGIC_APPLE -> insertAfter(event, Items.ENCHANTED_GOLDEN_APPLE, stack);
                            case SNACK -> insertBefore(event, Items.BREAD, stack);
                            case DESSERT -> insertAfter(event, Items.PUMPKIN_PIE, stack);
                            case SOUP -> insertBefore(event, Items.RABBIT_STEW, stack);
                            case MEAL -> insertAfter(event, Items.RABBIT_STEW, stack);
                            case SYRUP -> insertBefore(event, Items.HONEY_BOTTLE, stack);
                            case DRINK -> insertAfter(event, Items.HONEY_BOTTLE, stack);
                            case MAGIC, SLICE -> {}
                            default -> insertAfter(event, Items.SPIDER_EYE, stack);
                        }
                    }
                    case EdibleBlockItem edibleBlockItem -> {
                        EdibleBlock block = (EdibleBlock) edibleBlockItem.getBlock();
                        switch (block.getType()) {
                            case CAKE -> insertAfter(event, Items.CAKE, stack);
                            case PIE -> insertAfter(event, Items.PUMPKIN_PIE, stack);
                        }
                    }
                    default -> {}
                }
            }

            if (tab == CreativeModeTabs.INGREDIENTS) {
                if (stack.is(Tags.Items.RAW_MATERIALS)) insertAfter(event, Items.RAW_GOLD, stack);
                if (stack.is(Tags.Items.GEMS)) insertAfter(event, Items.DIAMOND, stack);
                if (stack.is(Tags.Items.NUGGETS)) insertAfter(event, Items.GOLD_NUGGET, stack);
                if (stack.is(Tags.Items.INGOTS)) insertAfter(event, Items.GOLD_INGOT, stack);
                if (stack.is(Tags.Items.BRICKS)) insertAfter(event, Items.NETHER_BRICK, stack);
                if (stack.is(Tags.Items.LOOM_PATTERNS)) insertAfter(event, Items.GUSTER_BANNER_PATTERN, stack);
                if (stack.is(ItemTags.DECORATED_POT_SHERDS)) insertAfter(event, Items.SNORT_POTTERY_SHERD, stack);
                if (stack.is(ItemTags.TRIM_TEMPLATES)) insertAfter(event, Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, stack);
            }
        }

        ADDED.clear();
    }

    public static void autoInsert(BuildCreativeModeTabContentsEvent event, String modId) {
        autoInsert(event, modId, null);
    }

    public static void insertAfter(BuildCreativeModeTabContentsEvent event, Item target, ItemStack... stacks) {
        ItemStack current = target.getDefaultInstance();
        for (ItemStack stack : stacks) {
            Item item = stack.getItem();
            if (ADDED.contains(item)) continue;

            event.insertAfter(current, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            ADDED.add(item);

            current = stack;
        }
    }

    public static void insertAfter(BuildCreativeModeTabContentsEvent event, Item target, Item... items) {
        insertAfter(event, target, Arrays.stream(items).map(RegUtil::stack).toArray(ItemStack[]::new));
    }

    public static void insertBefore(BuildCreativeModeTabContentsEvent event, Item target, ItemStack... stacks) {
        ItemStack current = target.getDefaultInstance();
        for (ItemStack stack : stacks) {
            Item item = stack.getItem();
            if (ADDED.contains(item)) continue;

            event.insertBefore(current, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            ADDED.add(item);

            current = stack;
        }
    }

    public static void insertBefore(BuildCreativeModeTabContentsEvent event, Item target, Item... items) {
        insertBefore(event, target, Arrays.stream(items).map(RegUtil::stack).toArray(ItemStack[]::new));
    }

}
