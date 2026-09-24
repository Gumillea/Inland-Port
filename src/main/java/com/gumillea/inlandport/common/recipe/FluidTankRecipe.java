package com.gumillea.inlandport.common.recipe;

import com.gumillea.inlandport.core.api.record.recipe.FluidTankInput;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FluidTankRecipe implements Recipe<FluidTankInput> {
    protected final RecipeType<?> type;
    protected final RecipeSerializer<?> serializer;
    protected final String group;

    protected final Ingredient ingredient;
    protected final ItemStack itemResult;

    protected final List<Either<Ingredient, SizedFluidIngredient>> ingredientList;
    protected final List<Either<ItemStack, FluidStack>> resultList;

    public FluidTankRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, Ingredient ingredient, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, List<Either<ItemStack, FluidStack>> resultList, ItemStack itemResult) {
        this.type = type;
        this.serializer = serializer;
        this.group = group == null ? "" : group;
        this.ingredientList = ingredientList != null ? ingredientList : Collections.emptyList();
        this.resultList = resultList != null ? resultList : Collections.emptyList();
        this.ingredient = ingredient != null ? ingredient : Ingredient.EMPTY;
        this.itemResult = itemResult != null ? itemResult : ItemStack.EMPTY;
    }

    public FluidTankRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, List<Either<Ingredient, SizedFluidIngredient>> ingredientList, ItemStack itemResult) {
        this(type, serializer, group, Ingredient.EMPTY, ingredientList, Collections.emptyList(), itemResult);
    }

    public FluidTankRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, Ingredient ingredient, List<Either<ItemStack, FluidStack>> resultList) {
        this(type, serializer, group, ingredient, Collections.emptyList(), resultList, ItemStack.EMPTY);
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    public String getGroup() {
        return group;
    }

    public List<Either<Ingredient, SizedFluidIngredient>> getIngredientList() {
        return ingredientList;
    }

    public List<Either<ItemStack, FluidStack>> getResultList() {
        return resultList;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getItemResult() {
        return itemResult;
    }

    public List<Ingredient> getItemIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Either<Ingredient, SizedFluidIngredient> either : ingredientList) {
            either.left().ifPresent(ingredients::add);
        }

        return ingredients;
    }

    public List<SizedFluidIngredient> getFluidIngredients() {
        List<SizedFluidIngredient> ingredients = new ArrayList<>();

        for (Either<Ingredient, SizedFluidIngredient> either : ingredientList) {
            either.right().ifPresent(ingredients::add);
        }

        return ingredients;
    }

    public List<ItemStack> getItemResults() {
        List<ItemStack> results = new ArrayList<>();

        if (!itemResult.isEmpty()) results.add(itemResult);

        for (Either<ItemStack, FluidStack> either : resultList) {
            either.left().ifPresent(results::add);
        }

        return results;
    }

    public List<FluidStack> getFluidResults() {
        List<FluidStack> results = new ArrayList<>();
        for (Either<ItemStack, FluidStack> either : resultList) {
            either.right().ifPresent(results::add);
        }
        return results;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        if (ingredient != null && ingredient != Ingredient.EMPTY) nonNullList.add(ingredient);
        nonNullList.addAll(getItemIngredients());
        return nonNullList;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        if (!itemResult.isEmpty()) return itemResult.copy();
        List<ItemStack> items = getItemResults();
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public boolean matches(FluidTankInput input, Level level) {
        return getIngredient().test(input.item());
    }

    @Override
    public ItemStack assemble(FluidTankInput input, HolderLookup.Provider provider) {
        return getResultItem(provider);
    }

    public static class Serializer<T extends FluidTankRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(ExtractingFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(FluidTankRecipe::getGroup),
                    INGREDIENT_CODEC.listOf().fieldOf("ingredients").forGetter(FluidTankRecipe::getIngredientList),
                    ItemStack.CODEC.fieldOf("result").forGetter(FluidTankRecipe::getItemResult)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, FluidTankRecipe::getGroup,
                    INGREDIENT_STREAM_CODEC.apply(ByteBufCodecs.list()), FluidTankRecipe::getIngredientList,
                    ItemStack.STREAM_CODEC, FluidTankRecipe::getItemResult,
                    factory::create
            );
        }

        public Serializer(InjectingFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(FluidTankRecipe::getGroup),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(FluidTankRecipe::getIngredient),
                    RESULT_CODEC.listOf().fieldOf("results").forGetter(FluidTankRecipe::getResultList)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, FluidTankRecipe::getGroup,
                    Ingredient.CONTENTS_STREAM_CODEC, FluidTankRecipe::getIngredient,
                    RESULT_STREAM_CODEC.apply(ByteBufCodecs.list()), FluidTankRecipe::getResultList,
                    factory::create
            );
        }

        @Override
        public MapCodec<T> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return streamCodec;
        }
    }

    public interface ExtractingFactory<T extends FluidTankRecipe> {
        T create(String group, List<Either<Ingredient, SizedFluidIngredient>> ingredients, ItemStack itemResult);
    }

    public interface InjectingFactory<T extends FluidTankRecipe> {
        T create(String group, Ingredient ingredient, List<Either<ItemStack, FluidStack>> results);
    }

    public static final Codec<Either<Ingredient, SizedFluidIngredient>> INGREDIENT_CODEC = Codec.either(Ingredient.CODEC, SizedFluidIngredient.FLAT_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, Either<Ingredient, SizedFluidIngredient>> INGREDIENT_STREAM_CODEC = ByteBufCodecs.either(Ingredient.CONTENTS_STREAM_CODEC, SizedFluidIngredient.STREAM_CODEC);

    public static final Codec<Either<ItemStack, FluidStack>> RESULT_CODEC = Codec.either(ItemStack.CODEC, FluidStack.CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, Either<ItemStack, FluidStack>> RESULT_STREAM_CODEC = ByteBufCodecs.either(ItemStack.STREAM_CODEC, FluidStack.STREAM_CODEC);
}