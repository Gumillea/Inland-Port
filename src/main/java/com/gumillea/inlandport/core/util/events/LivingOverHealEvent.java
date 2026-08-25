package com.gumillea.inlandport.core.util.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class LivingOverHealEvent extends LivingEvent implements ICancellableEvent {
    private float amount;

    public LivingOverHealEvent(LivingEntity entity, float amount) {
        super(entity);
        this.setAmount(amount);
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}