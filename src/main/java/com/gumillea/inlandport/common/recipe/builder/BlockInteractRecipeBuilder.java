package com.gumillea.inlandport.common.recipe.builder;

import com.gumillea.inlandport.common.recipe.BlockInteractRecipe;
import com.gumillea.inlandport.core.util.utils.RegUtil;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockInteractRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private BlockState state;
    private boolean isStrict = false;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    @Nullable
    private BlockInteractRecipe.Factory<?> factory;
    @Nullable
    private BlockInteractRecipe.SimpleFactory<?> simpleFactory;

    private BlockInteractRecipeBuilder(RecipeCategory category, @Nullable BlockInteractRecipe.Factory<?> factory, BlockState state, Ingredient ingredient, ItemLike result, int count, boolean isStrict) {
        this.category = category;
        this.factory = factory;
        this.state = state;
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.count = count;
        this.isStrict = isStrict;
    }

    private BlockInteractRecipeBuilder(RecipeCategory category, @Nullable BlockInteractRecipe.SimpleFactory<?> simpleFactory, Ingredient ingredient, ItemLike result, int count) {
        this.category = category;
        this.simpleFactory = simpleFactory;
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.count = count;
    }

    public static BlockInteractRecipeBuilder create(RecipeCategory category, BlockInteractRecipe.Factory<?> factory, BlockState state, Ingredient ingredient, ItemLike result, int count, boolean isStrict) {
        return new BlockInteractRecipeBuilder(category, factory, state, ingredient, result, count, isStrict);
    }

    public static BlockInteractRecipeBuilder create(RecipeCategory category, BlockInteractRecipe.SimpleFactory<?> simpleFactory, Ingredient ingredient, ItemLike result, int count) {
        return new BlockInteractRecipeBuilder(category, simpleFactory, ingredient, result, count);
    }


    public BlockInteractRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public BlockInteractRecipeBuilder group(@Nullable String group) {
        this.group = group == null ? "" : group;
        return this;
    }

    public BlockInteractRecipeBuilder state(BlockState state) {
        this.state = state;
        return this;
    }

    public BlockInteractRecipeBuilder strict(boolean isStrict) {
        this.isStrict = isStrict;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation location) {
        ensureValid(location);

        Advancement.Builder unlocks = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(unlocks::addCriterion);

        BlockInteractRecipe recipe = factory != null ? factory.create(group, state, ingredient, RegUtil.stack(result, count), isStrict) : simpleFactory.create(group, ingredient, RegUtil.stack(result, count));
        output.accept(location, recipe, unlocks.build(location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation location) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }
}