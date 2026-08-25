package com.gumillea.inlandport.core.util.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class EntityUtil {

    public static boolean isInDimension(Entity entity, ResourceKey<Level> dimension) {
        return entity.level().dimension() == dimension;
    }

    public static boolean isInOverworld(Entity entity) {
        return isInDimension(entity, Level.OVERWORLD);
    }

    public static boolean isInNether(Entity entity) {
        return isInDimension(entity, Level.NETHER);
    }

    public static boolean isInEnd(Entity entity) {
        return isInDimension(entity, Level.END);
    }

    public static boolean isInBiome(Entity entity, Holder<Biome> biome) {
        return entity.level().getBiome(entity.blockPosition()) == biome;
    }

    public static boolean isInBiome(Entity entity, TagKey<Biome> biome) {
        return entity.level().getBiome(entity.blockPosition()).is(biome);
    }

    public static int getLightLevel(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        Level level = entity.level();
        BlockPos pos = BlockPos.containing(x, y, z);
        return level.getRawBrightness(pos, level.getSkyDarken());
    }

    public static void giveExp(Entity entity, int amount) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            player.giveExperiencePoints(amount);
            player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(),1.0F, 1.0F);
        }
    }

    public static void applyShield(LivingEntity living, int amount) {
        living.setAbsorptionAmount(living.getAbsorptionAmount() + amount);
    }

    public static void addItem(Entity entity, Item item, int i) {
        if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(item, i));
        }
    }

    public static void addItem(Entity entity, Item item) {
        addItem(entity, item, 1);
    }

    public static void addLootItem(Entity entity, String modId, String name) {
        if (entity instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, IPUtil.loc(modId, name));
            LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
            LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.GIFT);

            table.getRandomItems(params, player::addItem);
        }
    }

    public static void eat(Player player, Item item) {
        ItemStack stack = new ItemStack(item);
        FoodProperties properties = item.getFoodProperties(stack, player);
        RandomSource random = player.getRandom();
        if (properties != null) {
            playSound(player, SoundEvents.GENERIC_EAT, 0.5F + 0.5F * (float) random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            player.eat(player.level(), stack, properties);
        }
    }

    public static void eat(Player player, ItemStack item) {
        eat(player, item.getItem());
    }

    public static void playSound(Entity entity, SoundEvent event, SoundSource source, float volume, float pitch) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, source, volume, pitch);
    }

    public static void playSound(Entity entity, Holder<SoundEvent> event, SoundSource source, float volume, float pitch) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), event.value(), source, volume, pitch);
    }

    public static void playSound(Entity entity, Holder<SoundEvent> event) {
        playSound(entity, event, SoundSource.NEUTRAL, 1F, 1F);
    }

    public static void playSound(Entity entity, SoundEvent event) {
        playSound(entity, event, SoundSource.NEUTRAL, 1F, 1F);
    }

    public static void playSound(Entity entity, Holder<SoundEvent> event, float volume, float pitch) {
        playSound(entity, event, SoundSource.NEUTRAL, volume, pitch);
    }

    public static void playSound(Entity entity, SoundEvent event, float volume, float pitch) {
        playSound(entity, event, SoundSource.NEUTRAL, volume, pitch);
    }

    public static void freeze(Entity entity, Object time) {
        entity.setTicksFrozen(entity.getTicksFrozen() + IPUtil.toTicks(time));
    }

    public static void burn(Entity entity, Object time) {
        entity.setRemainingFireTicks(IPUtil.toTicks(time));
    }
}
