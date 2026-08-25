package com.gumillea.inlandport.common.entity;

import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class IPBoat extends Boat {
    private static final EntityDataAccessor<String> DATA_ID_CUSTOM_TYPE = SynchedEntityData.defineId(IPBoat.class, EntityDataSerializers.STRING);

    public IPBoat(EntityType<? extends IPBoat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_CUSTOM_TYPE, "");
    }

    public void setCustomType(ResourceLocation location) {
        this.entityData.set(DATA_ID_CUSTOM_TYPE, location.toString());
    }

    public ResourceLocation getCustomType() {
        String typeStr = this.entityData.get(DATA_ID_CUSTOM_TYPE);
        return typeStr.isEmpty() ? IPUtil.mcLoc("oak") : ResourceLocation.parse(typeStr);
    }

    public boolean canAddPassenger(Entity entity) {
        return this.getPassengers().size() < getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 2;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.getCustomType().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Type")) {
            this.setCustomType(ResourceLocation.parse(compound.getString("Type")));
        }
    }

    @Override
    public Item getDropItem() {
        return BuiltInRegistries.ITEM.get(EntityType.getKey(this.getType()));
    }
}