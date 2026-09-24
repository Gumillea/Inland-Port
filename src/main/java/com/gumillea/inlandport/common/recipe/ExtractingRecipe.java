package com.gumillea.inlandport.common.recipe;

import com.gumillea.inlandport.common.recipe.builder.FluidTankRecipeBuilder;
import com.gumillea.inlandport.core.api.record.recipe.FluidTankInput;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.gumillea.inlandport.test.reg.IPRecipes;
import com.mojang.datafixers.util.Either;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public class ExtractingRecipe extends FluidTankRecipe {

    public ExtractingRecipe(String group, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, ItemStack itemResult) {
        super(IPRecipes.EXTRACTING_TYPE.get(), IPRecipes.EXTRACTING_SERIALIZER.get(), group, ingredientList, itemResult);
    }
    
    public ItemStack getToastSymbol() {
        return RegUtil.stack(Items.BUCKET);
    }

    public static FluidTankRecipeBuilder builder(RecipeCategory category, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, ItemStack itemResult){
        return FluidTankRecipeBuilder.create(category, ExtractingRecipe::new, ingredientList, itemResult);
    }

    @Override
    public boolean matches(FluidTankInput input, Level level) {
        return getItemIngredients().stream().anyMatch(ingredient -> ingredient.test(input.item())) && getFluidIngredients().stream().anyMatch(ingredient -> ingredient.test(input.fluid()));
    }

}