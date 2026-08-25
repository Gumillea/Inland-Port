package com.gumillea.inlandport.common.fluid;

import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class IPFluidType extends FluidType {

    private final String texture;

    public IPFluidType(String texture) {
        super(FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
        this.texture = texture;
    }

    public String getTexture() {
        return this.texture;
    }

    public Fluid getFluid() {
        ResourceLocation key = RegUtil.key(this);
        return CompatUtil.fluid(key.getNamespace(), key.getPath());
    }
}
