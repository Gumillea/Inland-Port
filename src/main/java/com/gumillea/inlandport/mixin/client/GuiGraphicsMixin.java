package com.gumillea.inlandport.mixin.client;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.client.DisabledItemOverlay;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private void inlandPort$renderOverlay(Font font, ItemStack stack, int x, int y, String string, CallbackInfo ci) {
        if (!InlandPortConfig.Client.DISABLE_OVERLAY.get()) return;
        DisabledItemOverlay.render((GuiGraphics) (Object) this, stack, x, y);
    }

}