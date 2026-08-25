package com.gumillea.inlandport.mixin;

import com.gumillea.inlandport.test.reg.IPAttributes;
import com.gumillea.inlandport.core.util.utils.AttrUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void inlandPort$setGetUseDuration(ItemStack stack, LivingEntity living, CallbackInfoReturnable<Integer> cir) {
        Holder<Attribute> ius = IPAttributes.ITEM_USAGE_SPEED;
        if (!AttrUtil.has(living, ius)) return;

        int duration = cir.getReturnValue();
        int newDuration = (int) (duration / IPAttributes.getItemUsageSpeed(living));

        cir.setReturnValue(newDuration);
    }
}