package com.gumillea.inlandport.common.item;


import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.utils.ClientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EdibleBlockItem extends BlockItem {

    public EdibleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        int setting = InlandPortConfig.Common.PLACEABLE_FOOD_SETTING.get();

        if (setting == 2) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        return super.use(level, player, hand);
    }

    public @NotNull InteractionResult place(BlockPlaceContext context) {
        Player player = context.getPlayer();
        int setting = InlandPortConfig.Common.PLACEABLE_FOOD_SETTING.get();
        ItemStack stack = new ItemStack(this);

        if (stack.get(DataComponents.FOOD) != null) {
            if (setting == 0) return InteractionResult.FAIL;
            if (setting == 1 && player != null && !player.isShiftKeyDown()) return InteractionResult.FAIL;
        }

        return super.place(context);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (!InlandPortConfig.Client.EFFECT_TOOLTIP.get()) return;

        int setting = InlandPortConfig.Common.PLACEABLE_FOOD_SETTING.get();

        if (stack.get(DataComponents.FOOD) == null) return;

        if (setting != 0) {
            MutableComponent placeableSetting = setting == 1 ? Component.translatable("tooltip." + InlandPort.MODID + ".placeable_while_sneaking") : Component.translatable("tooltip." + InlandPort.MODID + ".placeable");
            components.add(placeableSetting.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }

        if (setting != 2) {
            ClientUtil.addEffectTip(stack, components::add);
        }
    }

}
