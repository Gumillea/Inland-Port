package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EventUtil {
    public static void random(Level level, double chance, Runnable task) {
        if (IPUtil.random(level, chance)) task.run();
    }

    public static void tickRandom(Level level, int tick, double chance, Runnable task) {
        if (IPUtil.tickRandom(level, tick ,chance)) task.run();
    }

    public static void schedule(ServerLevel level, Object delay, Runnable task) {
        if (level == null) return;
        int targetTick = level.getServer().getTickCount() + IPUtil.toTicks(delay);
        level.getServer().tell(new TickTask(targetTick, task));
    }

    public static void schedule(Level level, Object delay, Runnable task) {
        if (level instanceof ServerLevel serverLevel) {
            schedule(serverLevel, delay, task);
        }
    }

    public static void blockInteract(PlayerInteractEvent.RightClickBlock event, Predicate<BlockState> targetFilter, @Nullable Predicate<ItemStack> itemFilter, @Nullable Holder.Reference<GameEvent> gameEvent, Runnable task) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack inHand = player.getItemInHand(hand);
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (!level.isClientSide && targetFilter.test(state)) {
            if (itemFilter == null || itemFilter.test(inHand)) {
                player.swing(hand);
                task.run();

                if (gameEvent != null) level.gameEvent(gameEvent, pos, GameEvent.Context.of(player));
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    public static void blockInteract(PlayerInteractEvent.RightClickBlock event, Predicate<BlockState> targetFilter, Predicate<ItemStack> itemFilter, Runnable task) {
        blockInteract(event, targetFilter, itemFilter, null, task);
    }

    public static void blockInteract(PlayerInteractEvent.RightClickBlock event, Predicate<BlockState> targetFilter, Runnable task) {
        blockInteract(event, targetFilter, null, null, task);
    }

    public static void entityInteract(PlayerInteractEvent.EntityInteract event, Predicate<Entity> targetFilter, @Nullable Predicate<ItemStack> itemFilter, @Nullable Holder.Reference<GameEvent> gameEvent, Runnable task) {
        Level level = event.getLevel();
        Entity entity = event.getTarget();
        ItemStack stack = event.getItemStack();

        if (!level.isClientSide && targetFilter.test(entity)) {
            if (itemFilter == null || itemFilter.test(stack)) {
                task.run();

                if (gameEvent != null) level.gameEvent(entity, gameEvent, event.getPos());
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    public static void entityInteract(PlayerInteractEvent.EntityInteract event, Predicate<Entity> targetFilter, @Nullable Predicate<ItemStack> itemFilter, Runnable task) {
        entityInteract(event, targetFilter, itemFilter, null, task);
    }

    public static void entityInteract(PlayerInteractEvent.EntityInteract event, Predicate<Entity> targetFilter, Runnable task) {
        entityInteract(event, targetFilter, null, null, task);
    }

    public static void addTrades(VillagerTradesEvent event, VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if (event.getType() == profession) {
            for (VillagerTrades.ItemListing trade : trades) {
                event.getTrades().get(level).add(trade);
            }
        }
    }

    public static void addTrades(WandererTradesEvent event, boolean isRare, VillagerTrades.ItemListing... trades) {
        for (VillagerTrades.ItemListing trade : trades) {
            if (isRare) {
                event.getRareTrades().add(trade);
            } else {
                event.getGenericTrades().add(trade);
            }
        }
    }

    public static VillagerTrades.ItemListing trade(ItemStack cost, ItemStack stack, int maxTrades, int exp, float priceMult) {
        return new BasicItemListing(cost, stack, maxTrades, exp, priceMult);
    }

    public static VillagerTrades.ItemListing trade(int cost, ItemStack stack, int maxTrades, int exp) {
        return new BasicItemListing(cost, stack, maxTrades, exp);
    }

}
