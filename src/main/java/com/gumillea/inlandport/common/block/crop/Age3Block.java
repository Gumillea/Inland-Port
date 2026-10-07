package com.gumillea.inlandport.common.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class Age3Block extends BeetrootBlock {

    private final Supplier<Item> seed;
    private final int growSpeed;

    public Age3Block(Properties properties, int growSpeed, Supplier<Item> seed) {
        super(properties);
        this.growSpeed = growSpeed;
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seed.get();
    }

    public Item getSeed() {
        return seed.get();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(growSpeed) != 0) {
            super.randomTick(state, level, pos, random);
        }
    }

}
