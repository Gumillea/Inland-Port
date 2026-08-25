package com.gumillea.inlandport.common.block;

import com.gumillea.inlandport.core.util.tags.IPItemTags;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.EntityUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

import static com.gumillea.inlandport.common.block.EdibleBlock.Type.*;

public class EdibleBlock extends Block {
    private final Type type;
    private final Supplier<Item> slice;
    private final int maxBites;
    private final VoxelShape[] shapes;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 13);

    public EdibleBlock(Properties properties, Type type , @Nullable Supplier<Item> slice, int maxBites) {
        super(properties);
        this.type = type;
        this.slice = slice;
        this.maxBites = Math.max(1, Math.min(maxBites, 14));
        this.shapes = buildShapes();
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES, 0));
    }

    public Type getType() {
        return type;
    }

    public ItemStack getSlice() {
        return slice == null ? null : new ItemStack(slice.get());
    }

    public ItemStack getItem() {
        ResourceLocation key = RegUtil.key(getSlice().getItem());
        return RegUtil.stack(Objects.requireNonNull(CompatUtil.item(key.getNamespace(), key.getPath().replace("_slice", ""))));
    }

    public int getMaxBites() {
        return maxBites > 0 ? maxBites : 1;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        Item item = stack.getItem();

        if (this.getType() == CAKE && stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
            Block var10 = Block.byItem(item);
            if (var10 instanceof CandleBlock candleblock) {
                stack.consume(1, player);
                level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, CandleCakeBlock.byCandle(candleblock));
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.ITEM_USED.get(item));
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (getSlice() != null && stack.is(IPItemTags.KNIVES)) {
            cut(level, pos, state, player);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return eat(level, pos, state, player);
    }

    protected InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        FoodProperties food = slice.get().getFoodProperties(getSlice(), player);
        int bites = state.getValue(BITES);

        if (food != null && !player.canEat(false)) {
            if (!food.canAlwaysEat()) return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            EntityUtil.eat(player, slice.get());
            level.gameEvent(player, GameEvent.EAT, pos);
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));

            if (!isOnlyOneLeft(bites)) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

        }
        return InteractionResult.SUCCESS;
    }

    protected void cut(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        int bites = state.getValue(BITES);
        if (!isOnlyOneLeft(bites)) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        popResource(player.level(), pos, getSlice());
    }

    private boolean isOnlyOneLeft(int bites) {
        return bites >= getMaxBites() - 1;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getOutputSignal(state.getValue(BITES));
    }

    public int getOutputSignal(int i) {
        return (getMaxBites() - i) * 2;
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor level, BlockPos pos, BlockPos pos1) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state1, level, pos, pos1);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    private VoxelShape[] buildShapes() {
        VoxelShape[] shapes = new VoxelShape[14];
        double bite = 14.0 / getMaxBites();

        for (int i = 0; i < getMaxBites(); i++) {
            shapes[i] = switch (type) {
                case CAKE -> Block.box(1.0 + i * bite, 0.0, 1.0, 15.0, 8.0, 15.0);
                case PIE -> Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
            };
        }

        return shapes;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int bites = state.getValue(BITES);
        if (bites >= 0 && bites < this.shapes.length && this.shapes[bites] != null) {
            return this.shapes[bites];
        }
        return Shapes.block();
    }

    public enum Type {
        CAKE,
        PIE
    }
}
