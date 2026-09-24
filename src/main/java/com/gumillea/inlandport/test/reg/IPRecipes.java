package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.recipe.ExtractingRecipe;
import com.gumillea.inlandport.common.recipe.FluidTankRecipe;
import com.gumillea.inlandport.common.recipe.InjectingRecipe;
import com.gumillea.inlandport.core.util.helpers.reg.RecipeHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IPRecipes {
    public static final RecipeHelper HELPER = new RecipeHelper(InlandPort.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<InjectingRecipe>> INJECTING_TYPE = HELPER.regType("injecting");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InjectingRecipe>> INJECTING_SERIALIZER = HELPER.regSerializer("injecting", () -> new FluidTankRecipe.Serializer<>(InjectingRecipe::new));

    public static final DeferredHolder<RecipeType<?>, RecipeType<ExtractingRecipe>> EXTRACTING_TYPE = HELPER.regType("extracting");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExtractingRecipe>> EXTRACTING_SERIALIZER = HELPER.regSerializer("extracting", () -> new FluidTankRecipe.Serializer<>(ExtractingRecipe::new));
}
