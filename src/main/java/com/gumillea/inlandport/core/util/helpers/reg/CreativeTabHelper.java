package com.gumillea.inlandport.core.util.helpers.reg;

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

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoItemTab(String name, Object icon) {
        return regAutoTab(name, icon, item -> RegHelper.isDisabled(item) || RegUtil.isBlock(item));
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> regAutoBlockTab(String name, Object icon) {
        return regAutoTab(name, icon, item -> RegHelper.isDisabled(item) || !RegUtil.isBlock(item));
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

    public static void autoInsert(BuildCreativeModeTabContentsEvent event, String modId) {
        for (Item item : AutoDataGeneHelper.getItems(modId)) {
            ItemStack stack = RegUtil.stack(item);
            if (RegHelper.isDisabled(stack)) return;

            ResourceKey<CreativeModeTab> tab = event.getTabKey();
            if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                if (stack.is(ItemTags.BOATS)) insertAfter(event, Items.CHERRY_CHEST_BOAT, stack);
                if (stack.is(Tags.Items.MUSIC_DISCS)) insertAfter(event, Items.MUSIC_DISC_PIGSTEP, stack);
            }

            if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
                if (stack.is(Tags.Items.SEEDS)) insertAfter(event, Items.PITCHER_POD, stack);
            }

            if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
                if (stack.is(Tags.Items.FOODS_VEGETABLE)) insertAfter(event, Items.BEETROOT, stack);
                if (stack.is(Tags.Items.FOODS_BERRY)) insertAfter(event, Items.GLOW_BERRIES, stack);
            }
        }
    }

    private static void insertAfter(BuildCreativeModeTabContentsEvent event, Item target, ItemStack stack) {
        event.insertAfter(target.getDefaultInstance(), stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

}
