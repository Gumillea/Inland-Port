package com.gumillea.inlandport.common.block.entity;

import com.gumillea.inlandport.test.reg.IPBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IPBrushableBlockEntity extends BrushableBlockEntity {

    public IPBrushableBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return IPBlockEntityTypes.BRUSHABLE_BLOCK.get();
    }

}


