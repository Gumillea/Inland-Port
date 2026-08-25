package com.gumillea.inlandport.core.util.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingDodgedEvent extends LivingEvent  implements ICancellableEvent {
    private final DamageContainer container;

    public LivingDodgedEvent(LivingEntity entity, DamageContainer container) {
        super(entity);
        this.container = container;
    }

    public DamageContainer getContainer() {
        return this.container;
    }

    public DamageSource getSource() {
        return this.container.getSource();
    }
}