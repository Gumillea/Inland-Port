package com.gumillea.inlandport.core.api.record.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidTankInput(ItemStack[] items, FluidStack fluid) implements RecipeInput {

    public ItemStack getItem(int index) {
        if (index < 0 || index >= items.length) {
            throw new IllegalArgumentException("No item for index " + index);
        }

        return items[index];
    }

    public ItemStack item() {
        return items.length > 0 ? items[0] : ItemStack.EMPTY;
    }

    public int size() {
        return items.length;
    }
}