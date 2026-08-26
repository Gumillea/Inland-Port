package com.gumillea.inlandport.core.api;

import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class NotDisabledCondition implements ICondition {
    private final ResourceLocation location;
    public static final MapCodec<NotDisabledCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(ResourceLocation.CODEC.fieldOf("item").forGetter(NotDisabledCondition::getLoc)).apply(builder, NotDisabledCondition::new));

    public NotDisabledCondition(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation getLoc() {
        return location;
    }

    @Override
    public boolean test(IContext context) {
        return !RegHelper.isDisabled(location);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}