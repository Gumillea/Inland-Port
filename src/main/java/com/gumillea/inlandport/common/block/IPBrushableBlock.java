package com.gumillea.inlandport.common.block;

import com.gumillea.inlandport.common.block.entity.IPBrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class IPBrushableBlock extends BrushableBlock {

    public IPBrushableBlock(Block block, SoundEvent event, SoundEvent event1, Properties properties) {
        super(block, event, event1, properties);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IPBrushableBlockEntity(pos, state);
    }
}
