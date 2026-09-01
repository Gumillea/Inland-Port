package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.item.EdibleBlockItem;
import com.gumillea.inlandport.common.item.FoodItem;
import com.gumillea.inlandport.core.api.record.TabContent;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
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

import static net.minecraft.world.item.CreativeModeTabs.*;

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

    public static void autoInsert(BuildCreativeModeTabContentsEvent event, String modId, @Nullable Runnable extra) {
        if (extra != null) extra.run();

        List<Item> items = new ArrayList<>(BuiltInRegistries.ITEM.stream().filter(item -> AutoDataGeneHelper.isSame(RegUtil.key(item), modId)).toList());
        Collections.reverse(items);

        for (Item item : items) {
            ItemStack stack = RegUtil.stack(item);
            if (RegHelper.isDisabled(stack)) continue;

            tab(event, add(TOOLS_AND_UTILITIES, () -> {
                        insertAfter(event, ItemTags.BOATS, Items.CHERRY_CHEST_BOAT, stack);
                        insertAfter(event, Tags.Items.MUSIC_DISCS, Items.MUSIC_DISC_PIGSTEP, stack);}),

                    add(NATURAL_BLOCKS, () -> {
                        insertAfter(event, ItemTags.SMALL_FLOWERS, Items.LILY_OF_THE_VALLEY, stack);
                        insertAfter(event, ItemTags.TALL_FLOWERS, Items.PEONY, stack);
                        insertAfter(event, Tags.Items.SEEDS, Items.BEETROOT_SEEDS, stack);}),

                    add(FOOD_AND_DRINKS, () -> {
                        insertAfter(event, ItemTags.MEAT, Items.COOKED_RABBIT, stack);
                        if (stack.is(ItemTags.FISHES)) {
                            insertAfter(event, Tags.Items.FOODS_FOOD_POISONING, Items.PUFFERFISH, stack);
                            insertAfter(event, Items.TROPICAL_FISH, stack);
                        }

                        insertAfter(event, Tags.Items.FOODS_FRUIT, Items.MELON_SLICE, stack);
                        insertAfter(event, Tags.Items.FOODS_VEGETABLE, Items.BEETROOT, stack);
                        insertAfter(event, Tags.Items.FOODS_BERRY, Items.GLOW_BERRIES, stack);
                        insertAfter(event, Tags.Items.FOODS_FOOD_POISONING, Items.SPIDER_EYE, stack);
                        insertAfter(event, Tags.Items.FOODS_COOKIE, Items.COOKIE, stack);
                        insertAfter(event, Tags.Items.FOODS_BREAD, Items.BREAD, stack);
                        insertAfter(event, Tags.Items.DRINKS_MILK, Items.MILK_BUCKET, stack);

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
                        }}),

                    add(INGREDIENTS, () -> {
                        insertAfter(event, Tags.Items.RAW_MATERIALS, Items.RAW_GOLD, stack);
                        insertAfter(event, Tags.Items.GEMS, Items.DIAMOND, stack);
                        insertAfter(event, Tags.Items.NUGGETS, Items.GOLD_NUGGET, stack);
                        insertAfter(event, Tags.Items.INGOTS, Items.GOLD_INGOT, stack);
                        insertAfter(event, Tags.Items.BRICKS, Items.NETHER_BRICK, stack);
                        insertAfter(event, Tags.Items.LOOM_PATTERNS, Items.GUSTER_BANNER_PATTERN, stack);
                        insertAfter(event, ItemTags.DECORATED_POT_SHERDS, Items.SNORT_POTTERY_SHERD, stack);
                        insertAfter(event, ItemTags.TRIM_TEMPLATES, Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, stack);
                    })
            );
        }

        ADDED.clear();
    }

    public static void autoInsert(BuildCreativeModeTabContentsEvent event, String modId) {
        autoInsert(event, modId, null);
    }

    public static void insertAfter(BuildCreativeModeTabContentsEvent event, @Nullable TagKey<Item> tag, Item target, ItemStack... stacks) {
        ItemStack current = target.getDefaultInstance();
        for (ItemStack stack : stacks) {
            if (tag != null && !stack.is(tag)) continue;
            Item item = stack.getItem();
            if (ADDED.contains(item)) continue;

            event.insertAfter(current, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            ADDED.add(item);

            current = stack;
        }
    }

    public static void insertAfter(BuildCreativeModeTabContentsEvent event, Item target, ItemStack... stacks) {
        insertAfter(event, null, target, stacks);
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

    public static void ignore(Item... items) {
        ADDED.addAll(Arrays.asList(items));
    }

    public static TabContent add(ResourceKey<CreativeModeTab> tabKey, Runnable task) {
        return new TabContent(tabKey, task);
    }

    public static void tab(BuildCreativeModeTabContentsEvent event, TabContent... contents) {
        for (TabContent content : contents) {
            if (content.tabKey() == event.getTabKey()) {
                content.task().run();
            }
        }
    }

}
