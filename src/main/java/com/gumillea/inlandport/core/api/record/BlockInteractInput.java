package com.gumillea.inlandport.core.api.record;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record BlockInteractInput(ItemStack item, BlockPos pos, BlockState state) implements RecipeInput {

    public BlockInteractInput(ItemStack item, Level level, BlockPos pos) {
        this(item, pos, level.getBlockState(pos));
    }

    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        } else {
            return this.item;
        }
    }

    public int size() {
        return 1;
    }
}