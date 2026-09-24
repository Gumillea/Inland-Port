package com.gumillea.inlandport.common.recipe.builder;

import com.gumillea.inlandport.common.recipe.FluidTankRecipe;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FluidTankRecipeBuilder implements RecipeBuilder {
    protected final RecipeCategory category;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    protected Ingredient ingredient;
    protected ItemStack itemResult;

    protected List<Either<Ingredient, SizedFluidIngredient>> ingredientList;
    protected List<Either<ItemStack, FluidStack>> resultList;

    @Nullable
    protected String group;
    @Nullable
    protected FluidTankRecipe.ExtractingFactory<?> exFactory;
    @Nullable
    protected FluidTankRecipe.InjectingFactory<?> inFactory;

    protected FluidTankRecipeBuilder(RecipeCategory category, FluidTankRecipe.ExtractingFactory<?> factory, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, ItemStack itemResult) {
        this.category = category;
        this.exFactory = factory;
        this.ingredientList = ingredientList;
        this.itemResult = itemResult;
    }

    protected FluidTankRecipeBuilder(RecipeCategory category, FluidTankRecipe.InjectingFactory<?> factory, Ingredient ingredient, List<Either<ItemStack, FluidStack>> resultList) {
        this.category = category;
        this.inFactory = factory;
        this.ingredient = ingredient;
        this.resultList = resultList;
    }

    public static FluidTankRecipeBuilder create(RecipeCategory category, FluidTankRecipe.ExtractingFactory<?> factory, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, ItemStack result) {
        return new FluidTankRecipeBuilder(category, factory, ingredientList, result);
    }

    public static FluidTankRecipeBuilder create(RecipeCategory category, FluidTankRecipe.InjectingFactory<?> factory, Ingredient ingredient, List<Either<ItemStack, FluidStack>> resultList) {
        return new FluidTankRecipeBuilder(category, factory, ingredient, resultList);
    }

    @Override
    public FluidTankRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public FluidTankRecipeBuilder group(@Nullable String group) {
        this.group = group == null ? "" : group;
        return this;
    }

    @Override
    public Item getResult() {
        if (!itemResult.isEmpty()) {
            return itemResult.getItem();
        }
        for (Either<ItemStack, FluidStack> either : resultList) {
            if (either.left().isPresent()) {
                return either.left().get().getItem();
            }
        }
        return itemResult.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation location) {
        Advancement.Builder unlocks = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(unlocks::addCriterion);

        FluidTankRecipe recipe = exFactory != null ? exFactory.create(group, ingredientList, itemResult) : inFactory.create(group, ingredient, resultList);
        output.accept(location, recipe, unlocks.build(location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

}