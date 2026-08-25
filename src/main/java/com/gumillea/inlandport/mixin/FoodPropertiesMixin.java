package com.gumillea.inlandport.mixin;

import com.gumillea.inlandport.InlandPortConfig;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodProperties.class)
public abstract class FoodPropertiesMixin {

    @Shadow @Final
    private float eatSeconds;

    @Inject(method = "eatDurationTicks", at = @At("HEAD"), cancellable = true)
    private void inlandPort$setEatDurationTicks(CallbackInfoReturnable<Integer> cir) {
        if (!InlandPortConfig.Common.ENABLE_CONFIGURABLE_GENERIC_EAT_DURATION.get()) return;

        float config = InlandPortConfig.Startup.GENERIC_EAT_DURATION.get().floatValue();

        if (eatSeconds == 1.6F) cir.setReturnValue(IPUtil.toTicks(config));
        if (eatSeconds == 0.8F) cir.setReturnValue((int)(config * 10));
    }
}