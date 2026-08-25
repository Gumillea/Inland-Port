package com.gumillea.inlandport.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.Nullable;

public class SimpleMobEffect extends MobEffect {
    private final String description;

    public SimpleMobEffect(MobEffectCategory category, int color, @Nullable String description) {
        super(category, color);
        this.description = description;
    }

    public SimpleMobEffect(MobEffectCategory category, int color) {
        this(category, color, null);
    }

    public boolean hasDescription() {
        return description != null;
    }

    public String getDescription() {
        return hasDescription() ? description : null;
    }

}
