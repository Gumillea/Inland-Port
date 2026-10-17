package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.block.entity.IPBrushableBlockEntity;
import com.gumillea.inlandport.core.util.helpers.reg.BlockEntityHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;

public class IPBlockEntityTypes {

    public static final BlockEntityHelper HELPER = new BlockEntityHelper(InlandPort.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IPBrushableBlockEntity>> BRUSHABLE_BLOCK = HELPER.reg("brushable_block", IPBrushableBlockEntity::new, () -> Set.of(IPBlocks.SUSPICIOUS_CLAY.get(), IPBlocks.SUSPICIOUS_RED_SAND.get(), IPBlocks.SUSPICIOUS_SOUL_SAND.get()), null);


}
