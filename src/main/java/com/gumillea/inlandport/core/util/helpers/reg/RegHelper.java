package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.api.record.RegConditions;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class RegHelper {
    private static final Map<ResourceLocation, Set<Object>> DISABLED = getMap();

    public static boolean isSoftMode(){
        return InlandPortConfig.Startup.DISABLE_MODE.get();
    }

    public static boolean isHardMode(){
        return !isSoftMode();
    }

    public static boolean isDisabled(ResourceLocation location) {
        return DISABLED.containsKey(location);
    }

    public static boolean isDisabled(Item item) {
        return isDisabled(RegUtil.key(item));
    }

    public static boolean isDisabled(ItemStack stack) {
        return isDisabled(stack.getItem());
    }

    public static boolean isDisabled(Block block) {
        return isDisabled(block.asItem());
    }

    public static Set<Object> getReasons(Item item) {
        return DISABLED.get(RegUtil.key(item));
    }

    public static <R, T extends R> DeferredHolder<R, T> reg(DeferredRegister<R> reg, String name, Supplier<T> supplier, @Nullable Supplier<T> altSupplier, @Nullable RegConditions regConditions) {
        if (regConditions == null) return reg.register(name, supplier);

        ResourceLocation location = IPUtil.loc(reg.getNamespace(), name);
        Set<Object> absent = new HashSet<>();

        for (Object condition : regConditions.objects()) {
            if (condition instanceof Boolean b && !b) {
                DISABLED.put(location, Collections.emptySet());
                break;
            }
            if (condition instanceof DeferredHolder<?, ?> holder && isDisabled(holder.getId())) absent.add(holder);
            if (condition instanceof String modId && !CompatUtil.mod(modId)) absent.add(modId);
            if (condition instanceof TagKey<?> tagKey && CompatUtil.isItemTagEmpty((TagKey<Item>) tagKey)) absent.add(tagKey);
        }

        if (!absent.isEmpty()) DISABLED.put(location, absent);

        if (isDisabled(location)) {
            if (isHardMode()) {
                return null;
            } else if (altSupplier != null) {
                supplier = altSupplier;
            }
        }

        return reg.register(name, supplier);
    }

    private static Map<ResourceLocation, Set<Object>> getMap() {
        Map<ResourceLocation, Set<Object>> map = new HashMap<>();
        List<? extends String> configList = InlandPortConfig.Startup.DISABLED_ITEMS.get();

        for (String id : configList) {
            ResourceLocation loc = ResourceLocation.tryParse(id);
            if (loc != null) {
                map.put(loc, Collections.emptySet());
            }
        }

        return map;
    }

}
