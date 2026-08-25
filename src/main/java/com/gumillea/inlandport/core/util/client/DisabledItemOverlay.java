package com.gumillea.inlandport.core.util.client;

import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DisabledItemOverlay {
    private static final ResourceLocation BARRIER = IPUtil.mcLoc("item/barrier");

    public static boolean isDisabled(ItemStack stack) {
        return RegHelper.isDisabled(stack);
    }

    public static void render(GuiGraphics graphics, ItemStack stack, int x, int y) {
        if (!isDisabled(stack)) return;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 160);
        graphics.blit(x, y, 0, 16, 16, Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(BARRIER));
        graphics.pose().popPose();
    }
}

