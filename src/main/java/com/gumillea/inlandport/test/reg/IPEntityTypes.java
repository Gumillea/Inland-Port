package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.entity.IPThrowableItemProjectile;
import com.gumillea.inlandport.core.util.helpers.reg.EntityTypeHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IPEntityTypes {

    public static final EntityTypeHelper HELPER = new EntityTypeHelper(InlandPort.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<IPThrowableItemProjectile>> ITEM_PROJECTILE = HELPER.reg("item_projectile", () -> EntityType.Builder.<IPThrowableItemProjectile>of((type, level) -> new IPThrowableItemProjectile(level), MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("item_projectile"));

}
