package com.gumillea.inlandport.common.entity;

import com.gumillea.inlandport.test.reg.IPEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class IPThrowableItemProjectile extends ThrowableItemProjectile {

    private final List<BiConsumer<IPThrowableItemProjectile, EntityHitResult>> onHitEntity = new ArrayList<>();
    private final List<BiConsumer<IPThrowableItemProjectile, HitResult>> onHit = new ArrayList<>();

    public IPThrowableItemProjectile(Level level) {
        super(IPEntityTypes.ITEM_PROJECTILE.get(), level);
    }

    public IPThrowableItemProjectile(LivingEntity living, Level level) {
        super(IPEntityTypes.ITEM_PROJECTILE.get(), living, level);
    }

    public IPThrowableItemProjectile(double x, double y, double z, Level level) {
        super(IPEntityTypes.ITEM_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }

    public IPThrowableItemProjectile hitEntityAction(BiConsumer<IPThrowableItemProjectile, EntityHitResult> action) {
        onHitEntity.add(action);
        return this;
    }

    public IPThrowableItemProjectile hitAction(BiConsumer<IPThrowableItemProjectile, HitResult> action) {
        onHit.add(action);
        return this;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!level().isClientSide) {
            for (BiConsumer<IPThrowableItemProjectile, EntityHitResult> action : onHitEntity) {
                action.accept(this, result);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!level().isClientSide) {
            for (BiConsumer<IPThrowableItemProjectile, HitResult> action : onHit) {
                action.accept(this, result);
            }
            level().broadcastEntityEvent(this, (byte)3);
            discard();
        }
    }
}