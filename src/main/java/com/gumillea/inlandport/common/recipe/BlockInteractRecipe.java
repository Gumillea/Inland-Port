package com.gumillea.inlandport.common.recipe;

import com.gumillea.inlandport.core.api.record.BlockInteractInput;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockInteractRecipe implements Recipe<BlockInteractInput> {
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    private final String group;
    private final BlockState state;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final boolean isStrict;

    public BlockInteractRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, BlockState state, Ingredient ingredient, ItemStack result, boolean isStrict) {
        this.type = type;
        this.serializer = serializer;
        this.group = group == null ? "" : group;
        this.state = state == null ? Blocks.AIR.defaultBlockState() : state;
        this.ingredient = ingredient;
        this.result = result;
        this.isStrict = isStrict;
    }

    public RecipeType<?> getType() {
        return this.type;
    }

    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.copy();
    }

    public BlockState getState() {
        return this.state;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public boolean isStrict() {
        return this.isStrict;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public boolean canCraftInDimensions(int p_44424_, int p_44425_) {
        return true;
    }

    private boolean isMatchedBlock(BlockInteractInput input) {
        return isStrict ? input.state().equals(state) : input.state().is(state.getBlock());
    }

    @Override
    public boolean matches(BlockInteractInput input, Level level) {
        return isMatchedBlock(input) && ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(BlockInteractInput input, HolderLookup.Provider provider) {
        return result.copy();
    }

    public static class Serializer<T extends BlockInteractRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(BlockInteractRecipe.Factory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(BlockInteractRecipe::getGroup),
                    BlockState.CODEC.fieldOf("state").forGetter(BlockInteractRecipe::getState),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BlockInteractRecipe::getIngredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(BlockInteractRecipe::getResult),
                    Codec.BOOL.optionalFieldOf("strict", false).forGetter(BlockInteractRecipe::isStrict)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, BlockInteractRecipe::getGroup,
                    ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), BlockInteractRecipe::getState,
                    Ingredient.CONTENTS_STREAM_CODEC, BlockInteractRecipe::getIngredient,
                    ItemStack.STREAM_CODEC, BlockInteractRecipe::getResult,
                    ByteBufCodecs.BOOL, BlockInteractRecipe::isStrict,
                    factory::create
            );
        }

        public Serializer(BlockInteractRecipe.SimpleFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(BlockInteractRecipe::getGroup),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BlockInteractRecipe::getIngredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(BlockInteractRecipe::getResult)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, BlockInteractRecipe::getGroup,
                    Ingredient.CONTENTS_STREAM_CODEC, BlockInteractRecipe::getIngredient,
                    ItemStack.STREAM_CODEC, BlockInteractRecipe::getResult,
                    factory::create
            );
        }

        @Override
        public MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }

    public interface Factory<T extends BlockInteractRecipe> {
        T create(String group, BlockState state, Ingredient ingredient, ItemStack result, boolean isStrict);
    }

    public interface SimpleFactory<T extends BlockInteractRecipe> {
        T create(String group, Ingredient ingredient, ItemStack result);
    }
}