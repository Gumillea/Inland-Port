package com.gumillea.inlandport.common.recipe;

import com.gumillea.inlandport.common.recipe.builder.FluidTankRecipeBuilder;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.gumillea.inlandport.test.reg.IPRecipes;
import com.mojang.datafixers.util.Either;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class InjectingRecipe extends FluidTankRecipe {

    public InjectingRecipe(String group, Ingredient ingredient, List<Either<ItemStack, FluidStack>> resultList) {
        super(IPRecipes.INJECTING_TYPE.get(), IPRecipes.INJECTING_SERIALIZER.get(), group, ingredient, resultList);
    }

    public ItemStack getToastSymbol() {
        return RegUtil.stack(Items.WATER_BUCKET);
    }

    public static FluidTankRecipeBuilder builder(RecipeCategory category, Ingredient ingredient, List<Either<ItemStack, FluidStack>> resultList){
        return FluidTankRecipeBuilder.create(category, InjectingRecipe::new, ingredient, resultList);
    }


}