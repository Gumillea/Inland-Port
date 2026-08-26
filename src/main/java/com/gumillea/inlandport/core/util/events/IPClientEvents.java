package com.gumillea.inlandport.core.util.events;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.entity.IPBoat;
import com.gumillea.inlandport.common.entity.IPBoatRenderer;
import com.gumillea.inlandport.common.entity.IPChestBoat;
import com.gumillea.inlandport.common.fluid.IPFluidType;
import com.gumillea.inlandport.core.util.helpers.reg.BlockHelper;
import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = InlandPort.MODID, value = Dist.CLIENT)
public class IPClientEvents {

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List<Component> tooltip = event.getToolTip();
        
        if (RegHelper.isDisabled(stack)) {
            tooltip.add(Component.translatable("tooltip." + InlandPort.MODID + ".disabled").withStyle(ChatFormatting.DARK_GRAY));
            Set<Object> reasons = RegHelper.getReasons(item);

            if (!reasons.isEmpty()) {
                tooltip.add(CommonComponents.EMPTY);
                List<Component> items = new ArrayList<>();
                List<Component> mods = new ArrayList<>();
                List<Component> tags = new ArrayList<>();

                for (Object reason : reasons) {
                    if (reason instanceof DeferredHolder<?, ?> holder) {
                        items.add(Component.literal("■ " + holder.getId()).withStyle(ChatFormatting.DARK_GRAY));
                    }
                    else if (reason instanceof String modId) {
                        mods.add(Component.literal("■ " + modId).withStyle(ChatFormatting.DARK_GRAY));
                    }
                    else if (reason instanceof TagKey<?> tagKey) {
                        tags.add(Component.literal("■ " + tagKey.location()).withStyle(ChatFormatting.DARK_GRAY));
                    }
                }

                if (!items.isEmpty()) {
                    tooltip.add(Component.translatable("tooltip." + InlandPort.MODID + ".items_disabled").withStyle(ChatFormatting.GRAY));
                    tooltip.addAll(items);
                    tooltip.add(CommonComponents.EMPTY);
                }

                if (!mods.isEmpty()) {
                    tooltip.add(Component.translatable("tooltip." + InlandPort.MODID + ".mods_required").withStyle(ChatFormatting.GRAY));
                    tooltip.addAll(mods);
                    tooltip.add(CommonComponents.EMPTY);
                }

                if (!tags.isEmpty()) {
                    tooltip.add(Component.translatable("tooltip." + InlandPort.MODID + ".tags_missing").withStyle(ChatFormatting.GRAY));
                    tooltip.addAll(tags);
                    tooltip.add(CommonComponents.EMPTY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void regClientExtensions(RegisterClientExtensionsEvent event) {
        for (FluidType type : NeoForgeRegistries.FLUID_TYPES) {
            if (type instanceof IPFluidType iPType) {
                event.registerFluidType(create(RegUtil.key(iPType).getNamespace(), iPType.getTexture()), iPType);
            }
        }
    }

    @SubscribeEvent
    public static void regRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<EntityType<?>, EntityType<IPBoat>> boat : BlockHelper.BOATS) {
            event.registerEntityRenderer(boat.get(), context -> new IPBoatRenderer(context, false));
        }
        for (DeferredHolder<EntityType<?>, EntityType<IPChestBoat>> boat : BlockHelper.CHEST_BOATS) {
            event.registerEntityRenderer(boat.get(), context -> new IPBoatRenderer(context, true));
        }
    }

    private static IClientFluidTypeExtensions create(String modId, String texture) {
        String path = "block/fluid/" + texture;
        ResourceLocation stillTexture = IPUtil.loc(modId, path);
        ResourceLocation flowingTexture = IPUtil.loc(modId, path + "_flow");
        return new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return CompatUtil.isPresent(flowingTexture) ? flowingTexture : stillTexture;
            }
        };
    }

}
