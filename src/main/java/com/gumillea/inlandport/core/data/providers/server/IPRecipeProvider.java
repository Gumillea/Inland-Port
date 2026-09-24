package com.gumillea.inlandport.core.data.providers.server;

import com.baisylia.cookscollection.recipe.OvenRecipe;
import com.baisylia.cookscollection.recipe.OvenShapedRecipe;
import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.block.StorageBlock;
import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.common.block.family.stone.StoneBaseBlock;
import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.common.recipe.ExtractingRecipe;
import com.gumillea.inlandport.common.recipe.InjectingRecipe;
import com.gumillea.inlandport.core.api.NotDisabledCondition;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.IPCompat;
import com.gumillea.inlandport.core.util.tags.IPItemTags;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.*;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class IPRecipeProvider extends RecipeProvider {

    protected final Set<ResourceLocation> LOCATIONS = new HashSet<>();
    protected final String modId;

    protected String no(String modId){
        return "no:" + modId;
    }

    public IPRecipeProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
        this.modId = modId;
    }

    public void simpleFamilies(RecipeOutput output, Block... blocks) {
        for (Block block : blocks) {
            simpleFamily(output, block);
        }
    }

    public void simpleFamily(RecipeOutput output, Block baseBlock, String... modIds) {
        if (baseBlock instanceof SimpleBaseBlock family) {
            Block chiseledBlock = family.getChiseled();
            Block stairs = family.getStairs();
            Block slab = family.getSlab();
            Block wall = family.getWall();

            if (slab != null){
                slabRecipe(output, slab, family, modIds);
                if (chiseledBlock != null) chiseledBlockRecipe(output, chiseledBlock, slab, modIds);
            }
            if (stairs != null) stairRecipe(output, stairs, family, modIds);
            if (wall != null) wallRecipe(output, wall, family, modIds);
        }
    }

    public void stoneFamilies(RecipeOutput output, Block... blocks) {
        for (Block block : blocks) {
            stoneFamily(output, block);
        }
    }

    public void stoneFamily(RecipeOutput output, Block block, String... modIds) {
        if (block instanceof StoneBaseBlock family) {
            Block baseBlock;
            String modId = RegUtil.key(block).getNamespace();

            Block chiseledBlock = family.getChiseled();
            Block polishedBlock = family.getPolishedBlock();
            Block bricks = family.getBricks();
            Block tiles = family.getTiles();
            Block slab = family.getSlab();
            Block mossyBlock = family.getMossyBlock();
            Block mossyBricks = family.getMossyBricks();
            Block crackedBricks = family.getCrackedBricks();
            Block crackedTiles = family.getCrackedTiles();
            Block pillar = family.getPillar();

            if (slab != null && chiseledBlock != null) {
                chiseledBlockRecipe(output, chiseledBlock, slab, modIds);
                stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, chiseledBlock, 1, block, modIds);
            }

            if (polishedBlock != null) {
                buildingBlock2x2Recipe(output, polishedBlock, block, modIds);
                stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, polishedBlock, 1, block, modIds);
            }

            if (mossyBlock != null) {
                shapelessRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossyBlock, 1, new Object[]{block, Ingredient.of(Blocks.MOSS_BLOCK, Blocks.VINE)}, modIds);
                Block mossySlab = family.getMossySlab();
                if (mossySlab != null) {
                    slabRecipe(output, mossySlab, mossyBlock, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossySlab, 2, mossyBlock, modIds);
                }
                Block mossyStairs = family.getMossyStairs();
                if (mossyStairs != null) {
                    stairRecipe(output, mossyStairs, mossyBlock, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossyStairs, 1, mossyBlock, modIds);
                }
                Block mossyWall = family.getMossyWall();
                if (mossyWall != null) {
                    wallRecipe(output, mossyWall, mossyBlock, modIds);
                    stonecuttingRecipe(output, RecipeCategory.DECORATIONS, mossyWall, 1, mossyBlock, modIds);
                }
            }

            if (bricks != null) {
                stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, bricks, 1, block, modIds);
                if (polishedBlock != null) {
                    buildingBlock2x2Recipe(output, bricks, polishedBlock, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, bricks, 1, polishedBlock, modIds);
                } else {
                    buildingBlock2x2Recipe(output, bricks, block, modIds);
                }

                if (crackedBricks != null) smeltingRecipe(output, RecipeCategory.MISC, crackedBricks, 0.1F, 10, bricks, modIds);

                if (mossyBricks != null) {
                    shapelessRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossyBricks, 1, new Object[]{bricks, Ingredient.of(Blocks.MOSS_BLOCK, Blocks.VINE)}, modIds);

                    Block mossyBrickSlab = family.getMossyBrickSlab();
                    if (mossyBrickSlab != null) {
                        slabRecipe(output, mossyBrickSlab, mossyBricks, modIds);
                        stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossyBrickSlab, 2, mossyBricks, modIds);
                    }

                    Block mossyBrickStairs = family.getMossyBrickStairs();
                    if (mossyBrickStairs != null) {
                        stairRecipe(output, mossyBrickStairs, mossyBricks, modIds);
                        stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, mossyBrickStairs, 1, mossyBricks, modIds);
                    }

                    Block mossyBrickWall = family.getMossyBrickWall();
                    if (mossyBrickWall != null) {
                        wallRecipe(output, mossyBrickWall, mossyBricks, modIds);
                        stonecuttingRecipe(output, RecipeCategory.DECORATIONS, mossyBrickWall, 1, mossyBricks, modIds);
                    }
                }
            }

            if (tiles != null) {
                stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, tiles, 1, block, modIds);
                if (bricks != null) {
                    buildingBlock2x2Recipe(output, tiles, bricks, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, tiles, 1, bricks, modIds);
                } else if (polishedBlock != null) {
                    buildingBlock2x2Recipe(output, tiles, polishedBlock, modIds);
                }

                if (crackedTiles != null) smeltingRecipe(output, RecipeCategory.MISC, crackedTiles, 0.1F, 10, tiles, modIds);
            }

            if (pillar != null) {
                Block polishedSlab = family.getPolishedSlab();
                Block brickSlab = family.getBrickSlab();

                stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, pillar, 1, block, modIds);
                if (polishedSlab != null){
                    chiseledBlockRecipe(output, pillar, polishedSlab, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, pillar, 1, polishedBlock, modIds);
                } else if (brickSlab != null) {
                    chiseledBlockRecipe(output, pillar, brickSlab, modIds);
                    stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, pillar, 1, bricks, modIds);
                }

            }

            for (Block variant : Variant.getAllBlocks(family, StoneBaseBlock.REG_MAP)) {
                switch (variant) {
                    case SlabBlock slabBlock -> {
                        baseBlock = AutoDataGeneHelper.tryFindBaseBlock(modId, RegUtil.path(slabBlock), "slab");
                        slabRecipe(output, slabBlock, baseBlock, modIds);
                        stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, slabBlock, 2, baseBlock, modIds);
                        if (block != baseBlock) stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, slabBlock, 2, block, modIds);
                    }
                    case StairBlock stairBlock -> {
                        baseBlock = AutoDataGeneHelper.tryFindBaseBlock(modId, RegUtil.path(stairBlock), "stairs");
                        stairRecipe(output, stairBlock, baseBlock, modIds);
                        stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, stairBlock, 1, baseBlock, modIds);
                        if (block != baseBlock) stonecuttingRecipe(output, RecipeCategory.BUILDING_BLOCKS, stairBlock, 1, block, modIds);
                    }
                    case WallBlock wallBlock -> {
                        baseBlock = AutoDataGeneHelper.tryFindBaseBlock(modId, RegUtil.path(wallBlock), "wall");
                        wallRecipe(output, wallBlock, baseBlock, modIds);
                        stonecuttingRecipe(output, RecipeCategory.DECORATIONS, wallBlock, 1, baseBlock, modIds);
                        if (block != baseBlock) stonecuttingRecipe(output, RecipeCategory.DECORATIONS, wallBlock, 1, block, modIds);
                    }
                    default -> {}
                }
            }
        }
    }

    public void woodFamilies(RecipeOutput output, Block... baseBlocks) {
        for (Block block : baseBlocks){
            woodFamily(output, block);
        }
    }

    public void woodFamily(RecipeOutput output, Block baseBlock, String... modIds) {
        if (baseBlock instanceof WoodenBaseBlock family) {
            List<ItemLike> list = new ArrayList<>();

            Block log = family.getLog();
            Block wood = family.getWood();
            Block strippedLog = family.getStrippedLog();
            Block strippedWood = family.getStrippedWood();
            Block stairs = family.getStairs();
            Block slab = family.getSlab();
            Block fence = family.getFence();
            Block fenceGate = family.getFenceGate();
            Block button = family.getButton();
            Block pressurePlate = family.getPressurePlate();
            Block door = family.getDoor();
            Block trapDoor = family.getTrapdoor();
            Block sign = family.getStandingSign();
            Block hangingSign = family.getCeilingHangingSign();
            Item boat = family.getBoat();
            Item chestBoat = family.getChestBoat();

            shapelessRecipe(output, RecipeCategory.BUILDING_BLOCKS, family, 4, new Object[]{family.getLogsItemTag()} ,modIds);
            if (log != null && wood != null) {
                woodRecipe(output, wood, log, modIds);
                if (strippedLog != null && strippedWood != null) {
                    woodRecipe(output, strippedWood, strippedLog, modIds);
                    logCutting(output, strippedLog, log, modIds);
                    logCutting(output, strippedWood, wood, modIds);
                }
            }
            if (slab != null) slabRecipe(output, slab, family, modIds);
            if (stairs != null) stairRecipe(output, stairs, family, modIds);
            if (button != null) {
                shapelessRecipe(output, RecipeCategory.REDSTONE, button, 1, new Object[] {baseBlock}, modIds);
                list.add(button);
            }
            if (pressurePlate != null) {
                pressurePlateRecipe(output, pressurePlate, family, modIds);
                list.add(pressurePlate);
            }
            if (fence != null) {
                woodenFenceRecipe(output, fence, family, modIds);
                list.add(fence);
            }
            if (fenceGate != null) {
                fenceGateRecipe(output, fenceGate, family, modIds);
                list.add(fenceGate);
            }
            if (trapDoor != null) {
                trapDoorRecipe(output, trapDoor, family, modIds);
                list.add(trapDoor);
            }
            if (door != null) {
                doorRecipe(output, door, family, modIds);
                list.add(door);
            }
            if (sign != null) {
                signRecipe(output, sign, family, modIds);
                list.add(sign);
            }
            if (hangingSign != null) {
                if (strippedLog != null) hangingSignRecipe(output, hangingSign, strippedLog, modIds);
                list.add(hangingSign);
            }
            if (boat != null) {
                boatRecipe(output, boat, family, modIds);
                list.add(boat);
                if (chestBoat != null) {
                    shapelessRecipe(output, RecipeCategory.TRANSPORTATION, chestBoat, 1, new Object[] {Blocks.CHEST.asItem(), boat}, modIds);
                    cuttingRecipe(output, boat, 1,1F, chestBoat, ItemTags.HOES, modIds, b -> b.addResult(Blocks.CHEST.asItem()));
                }
            }

            if (!list.isEmpty()) {
                Ingredient ingredient = Ingredient.of(list.toArray(new ItemLike[0]));
                planksCutting(output, family, ingredient, modIds);
            }
        }
    }

    //Mixed
    public void stewRecipe(RecipeOutput output, ItemLike result, int count, int fDCount, Object[] objects, boolean coexist, String... modIds){
        String[] cond = coexist ? modIds : ArrayUtils.addAll(new String[] {no(IPCompat.FD)}, modIds);
        String[] fDCond = coexist ? modIds : ArrayUtils.addAll(new String[] {IPCompat.FD}, modIds);
        Object[] newObjects = ArrayUtils.addAll(new Object[] {Items.BOWL}, objects);

        shapelessRecipe(output, RecipeCategory.FOOD, result, count, handleFDObjects(newObjects), cond);
        simpleFDCooking(output, CookingPotRecipeBookTab.MEALS, result, fDCount, Items.BOWL, objects, fDCond);
    }

    public void stewRecipe(RecipeOutput output, ItemLike result, Object[] objects, String... modIds){
        stewRecipe(output, result, 1, 1, objects, false, modIds);
    }

    public void stewCompatRecipe(RecipeOutput output, ItemLike result, Object[] objects, String... modIds){
        stewRecipe(output, result, 1, 2, objects, true, modIds);
    }

    public void pieRecipe(RecipeOutput output, ItemLike result, int count, int fDCount, String[] pattern, Map<Character, Object> map, boolean coexist, String... modIds) {
        String[] cond = coexist ? modIds : ArrayUtils.addAll(new String[] {no(IPCompat.FD)}, modIds);
        String[] fDCond = coexist ? modIds : ArrayUtils.addAll(new String[] {IPCompat.FD, no(IPCompat.CC)}, modIds);

        shapelessRecipe(output, RecipeCategory.FOOD, result, count, handleFDObjects(getObjects(pattern, map, true)), cond);
        shapedRecipe(output, RecipeCategory.FOOD, result, fDCount, pattern, map, fDCond);
        bakingCCRecipe(output, result, fDCount, 200, getObjects(pattern, map, false), modIds);

        if (result instanceof EdibleBlock edibleBlock) {
            Item slice = edibleBlock.getSlice().getItem();
            cuttingRecipe(output, slice, 4,1F, edibleBlock, IPItemTags.KNIVES, modIds, null);
            shapelessRecipe(output, RecipeCategory.FOOD, result, 1, new Object[]{slice, slice, slice, slice}, modIds);
        }
    }

    public void pieRecipe(RecipeOutput output, ItemLike result, String[] pattern, Map<Character, Object> map, String... modIds) {
        pieRecipe(output, result, 1, 1, pattern, map, false, modIds);
    }

    public void pieCompatRecipe(RecipeOutput output, ItemLike result, String[] pattern, Map<Character, Object> map, String... modIds) {
        pieRecipe(output, result, 1, 2, pattern, map, true, modIds);
    }

    public void doughRecipe(RecipeOutput output, ItemLike result, int count, int fDCount, Object[] objects, boolean coexist, String... modIds) {
        String[] cond = coexist ? modIds : ArrayUtils.addAll(new String[] {no(IPCompat.CC)}, modIds);
        String[] cCCond = coexist ? modIds : ArrayUtils.addAll(new String[] {IPCompat.CC}, modIds);
        Object[] newObjects = ArrayUtils.addAll(new Object[] {IPItemTags.DOUGH}, objects);

        shapelessRecipe(output, RecipeCategory.FOOD, result, count, handleFDObjects(newObjects), cond);
        bakingCCRecipe(output, result, fDCount, 200, newObjects, cCCond);
    }

    public void cookieRecipe(RecipeOutput output, ItemLike result, int count, int fDCount, Object[] objects, boolean coexist, String... modIds) {
        String[] cond = coexist ? modIds : ArrayUtils.addAll(new String[] {no(IPCompat.CC)}, modIds);
        String[] cCCond = coexist ? modIds : ArrayUtils.addAll(new String[] {IPCompat.CC}, modIds);
        Object[] newObjects = ArrayUtils.addAll(new Object[] {IPItemTags.DOUGH}, objects);

        shapelessRecipe(output, RecipeCategory.FOOD, result, count, handleFDObjects(newObjects), cond);
        bakingCCRecipe(output, result, fDCount, 200, ArrayUtils.addAll(new Object[] {Items.SUGAR}, newObjects), cCCond);
    }

    public void cookieRecipe(RecipeOutput output, ItemLike result, Object[] objects, String... modIds) {
        cookieRecipe(output, result, 8, 8, objects, false, modIds);
    }

    public void cookieCompatRecipe(RecipeOutput output, ItemLike result, Object[] objects, String... modIds) {
        cookieRecipe(output, result, 8, 10, objects, true, modIds);
    }

    //CB - Baking
    public void bakingShapedCCRecipe(RecipeOutput output, ItemLike result, int count, int cookingTime, String[] pattern, Map<Character, Object> map, String... modIds) {
        if (!IPCompat.cc) return;
        Map<Character, Ingredient> ingredients = new HashMap<>();

        map.forEach((character, object) -> {
            switch (object) {
                case ItemLike item -> ingredients.put(character, Ingredient.of(item));
                case Ingredient ingredient -> ingredients.put(character, ingredient);
                case TagKey<?> tag -> ingredients.put(character, Ingredient.of((TagKey<Item>) tag));
                default -> throw new IllegalStateException("Unexpected value: " + object);
            }
        });

        ShapedRecipePattern patttern = ShapedRecipePattern.of(ingredients, pattern);
        output.accept(IPUtil.loc(modId, "cookscollection/baking/" + RegUtil.path(result.asItem())), new OvenShapedRecipe(patttern, RegUtil.stack(result.asItem(), count), cookingTime), null, toConds(result, modIds, getObjects(pattern, map, true)));
    }

    public void bakingCCRecipe(RecipeOutput output, ItemLike result, int count, int cookingTime, Object[] objects, String... modIds) {
        if (!IPCompat.cc) return;
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (Object object : objects) {
            ingredients.add(toIngredient(object));
        }

        output.accept(IPUtil.loc(modId, "cookscollection/baking/" + RegUtil.path(result.asItem())), new OvenRecipe(ingredients, RegUtil.stack(result.asItem(), count), cookingTime), null, toConds(result, modIds, objects));
    }

    public void bakingCCRecipe(RecipeOutput output, ItemLike result, int count, float seconds, Object[] objects, String... modIds) {
        bakingCCRecipe(output, result, count, IPUtil.secondsToTicks(seconds), objects, modIds);
    }

    public void bakingCCRecipe(RecipeOutput output, ItemLike result, Object[] objects, String... modIds) {
        bakingCCRecipe(output, result, 1, 10F, objects, modIds);
    }

    //FD - Cooking
    public void bowlMealCooking(RecipeOutput output, ItemLike result, int count, Object[] objects, String... modIds) {
        cookingRecipe(output, CookingPotRecipeBookTab.MEALS, result, count, 200, 1, Items.BOWL, objects, modIds);
    }

    public void simpleFDCooking(RecipeOutput output, CookingPotRecipeBookTab tab, ItemLike result, int count, float seconds, int exp, @Nullable ItemLike container, Object[] objects, String... modIds) {
        cookingRecipe(output, tab, result, count, IPUtil.secondsToTicks(seconds), exp, container, objects, modIds);
    }

    public void simpleFDCooking(RecipeOutput output, CookingPotRecipeBookTab tab, ItemLike result, int count, @Nullable ItemLike container, Object[] objects, String... modIds) {
        simpleFDCooking(output, tab, result, count, 10, 1, container, objects, modIds);
    }

    //FD - Cutting
    public void logCutting(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        cuttingRecipe(output, result, 1,1F, object, ItemTags.AXES, modIds, b -> b.addResult(CompatUtil.fDItem("tree_bark")));
    }

    public void planksCutting(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        simpleCuttingWithChance(output, result, 0.75F, object, ItemTags.AXES, modIds);
    }

    public void simpleCutting(RecipeOutput output, ItemLike result, Object object, Object tool, String... modIds) {
        cuttingRecipe(output, result, 1,1F, object, tool, modIds, null);
    }

    public void simpleCuttingWithChance(RecipeOutput output, ItemLike result, float chance, Object object, Object tool, String... modIds) {
        cuttingRecipe(output, result, 1, chance, object, tool, modIds, null);
    }

    //Fluid
    public void fluidRecipe(RecipeOutput output, ItemStack result, Object fluid, int amount, ItemStack object, @Nullable String... modIds) {
        injectingRecipe(output, result, fluid, amount, object, modIds);
        extractingRecipe(output, object, fluid, amount, result, modIds);
    }

    public void fluidBottleRecipe(RecipeOutput output, Object fluid, ItemStack object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.GLASS_BOTTLE, 1), fluid, 250, object, modIds);
    }

    public void fluidBottleRecipe(RecipeOutput output, Object fluid, ItemLike object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.GLASS_BOTTLE, 1), fluid, 250, RegUtil.stack(object), modIds);
    }

    public void fluidBowlRecipe(RecipeOutput output, Object fluid, ItemStack object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.BOWL, 1), fluid, 250, object, modIds);
    }

    public void fluidBowlRecipe(RecipeOutput output, Object fluid, ItemLike object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.BOWL, 1), fluid, 250, RegUtil.stack(object), modIds);
    }

    public void fluidBucketRecipe(RecipeOutput output, Object fluid, ItemStack object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.BUCKET, 1), fluid, 1000, object, modIds);
    }

    public void fluidBucketRecipe(RecipeOutput output, Object fluid, ItemLike object, @Nullable String... modIds) {
        fluidRecipe(output, RegUtil.stack(Items.BUCKET, 1), fluid, 1000, RegUtil.stack(object), modIds);
    }

    public void fluidBottleAndBucketRecipe(RecipeOutput output, Object fluid, ItemStack bottle, ItemStack bucket, @Nullable String... modIds) {
        fluidBottleRecipe(output, fluid, bottle, modIds);
        fluidBucketRecipe(output, fluid, bucket, modIds);
    }

    public void fluidBottleAndBucketRecipe(RecipeOutput output, Object fluid, ItemLike bottle, ItemLike bucket, @Nullable String... modIds) {
        fluidBottleAndBucketRecipe(output, fluid, RegUtil.stack(bottle), RegUtil.stack(bucket), modIds);
    }

    //Smelting
    public void oreSmeltingRecipe(RecipeOutput output, ItemLike result, float exp, float seconds, Object object, String... modIds) {
        smeltingRecipe(output, RecipeCategory.MISC, result, exp, seconds, object, modIds);
        blastingRecipe(output, RecipeCategory.MISC, result, exp, seconds / 2, object, modIds);
    }

    public void oreSmeltingRecipe(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        oreSmeltingRecipe(output, result, 0.7F, 10, object, modIds);
    }

    public void foodCookingRecipe(RecipeOutput output, ItemLike result, float exp, float seconds, Object object, String... modIds) {
        smeltingRecipe(output, RecipeCategory.FOOD, result, exp, seconds, object, modIds);
        smokingRecipe(output, RecipeCategory.FOOD, result, exp, seconds / 2, object, modIds);
        campfireCookingRecipe(output, RecipeCategory.FOOD, result, exp, seconds * 3, object, modIds);
        bakingCCRecipe(output, result, 3, seconds, new Object[]{object, object, object}, modIds);
    }

    public void foodCookingRecipe(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        foodCookingRecipe(output, result, 0.35F, 10, object, modIds);
    }

    //Shaped
    public void magicAppleRecipe(RecipeOutput output, ItemLike result, int count, Object ingredient, String... modIds) {
        magicFoodRecipe(output, result, count, ingredient, Items.APPLE, modIds);
    }

    public void magicFoodRecipe(RecipeOutput output, ItemLike result, int count, Object ingredient, Object fruit, String... modIds) {
        enclosedRecipe(output, RecipeCategory.FOOD, result, count, ingredient, fruit, modIds);
    }

    public void enclosedRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object ingredient, Object core, String... modIds) {
        shapedRecipe(output, category, result, count, new String[]{"###", "#f#", "###"}, Map.of('#', ingredient, 'f', core), modIds);
    }

    public void shaped2x2Recipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object ingredient, String... modIds) {
        shapedRecipe(output, category, result, count, new String[]{"## ", "## "}, Map.of('#', ingredient), modIds);
    }

    public void shaped3x3Recipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object ingredient, String... modIds) {
        shapedRecipe(output, category, result, count, new String[]{"###", "###", "###"}, Map.of('#', ingredient), modIds);
    }

    public void buildingBlock2x2Recipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.BUILDING_BLOCKS, result, 4, new String[]{"## ", "## "}, Map.of('#', ingredient), modIds);
    }

    public void woodRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.BUILDING_BLOCKS, result, 3, new String[]{"## ", "## "}, Map.of('#', ingredient), modIds);
    }

    public void boatRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.TRANSPORTATION, result, 1, new String[]{"# #", "###"}, Map.of('#', ingredient), modIds);
    }

    public void storageBlockRecipe(RecipeOutput output, Block result, String... modIds) {
        if (result instanceof StorageBlock storageBlock){
            storageBlock3x3Recipe(output, result.asItem(), storageBlock.getItem(), modIds);
        }
    }

    public void allStorageBlockRecipes(RecipeOutput output, String... modIds) {
        for (Block block : AutoDataGeneHelper.getBlocks(modId)) {
            if (block instanceof StorageBlock){
                storageBlockRecipe(output, block, modIds);
            }
        }
    }

    public void stickyBlock2x2Recipe(RecipeOutput output, ItemLike result, ItemLike ingredient, String... modIds) {
        shaped2x2Recipe(output, RecipeCategory.REDSTONE, result, 1, ingredient, modIds);
        shapelessRecipe(output, RecipeCategory.MISC, ingredient, 4, new Object[] {result, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE}, modIds);
    }

    public void storageBlock3x3Recipe(RecipeOutput output, ItemLike result, ItemLike ingredient, String... modIds) {
        shaped3x3Recipe(output, RecipeCategory.BUILDING_BLOCKS, result, 1, ingredient, modIds);
        shapelessRecipe(output, RecipeCategory.MISC, ingredient, 9, new Object[] {result}, modIds);
    }

    public void chiseledBlockRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.BUILDING_BLOCKS, result, 1, new String[]{"# ", "# "}, Map.of('#', ingredient), modIds);
    }

    public void pressurePlateRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.REDSTONE, result, 1, new String[]{"## "}, Map.of('#', ingredient), modIds);
    }

    public void slabRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.BUILDING_BLOCKS, result, 6, new String[]{"###"}, Map.of('#', ingredient), modIds);
    }

    public void stairRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.BUILDING_BLOCKS, result, 4, new String[]{"#  ", "## ", "###"}, Map.of('#', ingredient), modIds);
    }

    public void wallRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.DECORATIONS, result, 6, new String[]{"###", "###"}, Map.of('#', ingredient), modIds);
    }

    public void woodenFenceRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.MISC, result, 3, new String[]{"#s#", "#s#"}, Map.of('#', ingredient, 's', Items.STICK), modIds);
    }

    public void signRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.MISC, result, 3, new String[]{"###", "###", " s "}, Map.of('#', ingredient, 's', Items.STICK), modIds);
    }

    public void hangingSignRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.MISC, result, 6, new String[]{"c c", "###", "###"}, Map.of('#', ingredient, 'c', Blocks.CHAIN), modIds);
    }

    public void fenceGateRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.REDSTONE, result, 3, new String[]{"#s#", "#s#"}, Map.of('s', ingredient, '#', Items.STICK), modIds);
    }

    public void trapDoorRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.REDSTONE, result, 2, new String[]{"###", "###"}, Map.of('#', ingredient), modIds);
    }

    public void doorRecipe(RecipeOutput output, ItemLike result, Object ingredient, String... modIds) {
        shapedRecipe(output, RecipeCategory.REDSTONE, result, 3, new String[]{"## ", "## ", "## "}, Map.of('#', ingredient), modIds);
    }

    //base
    public void cuttingRecipe(RecipeOutput output, ItemLike result, int count, float chance, Object object, Object tool, @Nullable String[] modIds, @Nullable Consumer<CuttingBoardRecipeBuilder> extraResults) {
        if (!IPCompat.fd) return;
        regRecipe(output, CuttingBoardRecipeBuilder.cuttingRecipe(toIngredient(object), toIngredient(tool), result, count, chance), result, b -> {
            if (extraResults != null) extraResults.accept(b);
        }, modIds, IPCompat.FD + "/cutting", object, tool);
    }

    public void cookingRecipe(RecipeOutput output, CookingPotRecipeBookTab tab, ItemLike result, int count, int cookingTime, float exp, @Nullable ItemLike container, Object[] objects, @Nullable String... modIds) {
        if (!IPCompat.fd) return;
        regRecipe(output, CookingPotRecipeBuilder.cookingPotRecipe(result, count, cookingTime, exp, container), result, b -> {
            for (Object object : objects) {
                b.setRecipeBookTab(tab);
                b.addIngredient(toIngredient(object));
                if (object instanceof ItemLike itemLike) b.unlockedByAnyIngredient(itemLike);
            }
        }, modIds, IPCompat.FD + "/cooking", objects);
    }

    public void extractingRecipe(RecipeOutput output, ItemStack result, Object fluid, int amount, Object object, @Nullable String... modIds) {
        Fluid finalFluid = fluid instanceof Fluid fluidd ? fluidd : CompatUtil.getFluid((FluidType) fluid);
        regRecipe(output, ExtractingRecipe.builder(RecipeCategory.MISC, List.of(Either.left(toIngredient(object)),Either.right(SizedFluidIngredient.of(finalFluid, amount))), result), result.getItem(), (b) -> {}, modIds, "fluid/extracting", object);
    }

    public void extractingRecipe(RecipeOutput output, ItemLike result, int count, Object fluid, int amount, Object object, @Nullable String... modIds) {
        extractingRecipe(output, RegUtil.stack(result, count), fluid instanceof Fluid fluidd ? fluidd : CompatUtil.getFluid((FluidType) fluid), amount, toIngredient(object), modIds);
    }

    public void injectingRecipe(RecipeOutput output, ItemStack result, Object fluid, int amount, Object object, @Nullable String... modIds) {
        Fluid finalFluid = fluid instanceof Fluid fluidd ? fluidd : CompatUtil.getFluid((FluidType) fluid);
        regRecipe(output, InjectingRecipe.builder(RecipeCategory.MISC, toIngredient(object), List.of(Either.left(result), Either.right(new FluidStack(finalFluid, amount)))), finalFluid, (b) -> {}, modIds, "fluid/injecting", object);
    }

    public void injectingRecipe(RecipeOutput output, ItemLike result, int count, Object fluid, int amount, Object object, @Nullable String... modIds) {
        injectingRecipe(output, RegUtil.stack(result, count), fluid instanceof Fluid fluidd ? fluidd : CompatUtil.getFluid((FluidType) fluid), amount, toIngredient(object), modIds);
    }

    public void stonecuttingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object object, @Nullable String... modIds) {
        regRecipe(output, SingleItemRecipeBuilder.stonecutting(toIngredient(object), category, result, count), result, b -> {}, modIds, "stonecutting", object);
    }

    public void blastingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String... modIds) {
        blastingRecipe(output, category, result, exp, IPUtil.secondsToTicks(seconds), object, modIds);
    }

    public void blastingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, int time, Object object, @Nullable String... modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.blasting(toIngredient(object), category, result, exp, time), result, b -> {}, modIds, "cooking/blasting", object);
    }

    public void campfireCookingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String... modIds) {
        campfireCookingRecipe(output, category, result, exp, IPUtil.secondsToTicks(seconds), object, modIds);
    }

    public void campfireCookingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, int time, Object object, @Nullable String... modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.campfireCooking(toIngredient(object), category, result, exp, time), result, b -> {}, modIds, "cooking/campfire_cooking", object);
    }

    public void smeltingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String... modIds) {
        smeltingRecipe(output, category, result, exp, IPUtil.secondsToTicks(seconds), object, modIds);
    }

    public void smeltingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, int time, Object object, @Nullable String... modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.smelting(toIngredient(object), category, result, exp, time), result, b -> {}, modIds, "cooking/smelting", object);
    }

    public void smokingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String... modIds) {
        smokingRecipe(output, category, result, exp, IPUtil.secondsToTicks(seconds), object, modIds);
    }

    public void smokingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, int time, Object object, @Nullable String... modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.smoking(toIngredient(object), category, result, exp, time), result, b -> {}, modIds, "cooking/smoking", object);
    }

    public void shapelessRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object[] objects, String... modIds) {
        regRecipe(output, ShapelessRecipeBuilder.shapeless(category, result, count), result, b -> {
            for (Object object : objects) {
                b.requires(toIngredient(object));
            }
        }, modIds, "shapeless", objects);
    }

    @SuppressWarnings("unchecked")
    public void shapedRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, String[] pattern, Map<Character, Object> map, @Nullable String... modIds) {
        regRecipe(output, ShapedRecipeBuilder.shaped(category, result, count), result, b -> {
            for (String row : pattern) b.pattern(row);
            map.forEach((character, object) -> {
                switch (object) {
                    case ItemLike item -> b.define(character, item);
                    case ItemStack stack -> b.define(character, toIngredient(stack));
                    case Ingredient ingredient -> b.define(character, ingredient);
                    case TagKey<?> tag -> b.define(character, (TagKey<Item>) tag);
                    default -> throw new IllegalStateException("Unexpected value: " + object);
                }
            });
        }, modIds, "shaped", map.values().toArray());
    }

    @SuppressWarnings("unchecked")
    public <B extends RecipeBuilder> void regRecipe(RecipeOutput output, B builder, Object result, Consumer<B> consumer, String[] modIds, String type, Object... objects) {
        Set<ItemLike> itemUnlocks = new LinkedHashSet<>();
        Set<TagKey<Item>> tagUnlocks = new LinkedHashSet<>();
        Set<Fluid> fluids = new LinkedHashSet<>();
        Set<ICondition> conditions = new LinkedHashSet<>();
        Map<ItemLike, String> potionMap = new HashMap<>();

        ResourceLocation resultKey;
        String resultPath;
        String potion = "";

        switch (result) {
            case ItemLike item -> {
                resultKey = RegUtil.key(item);
                resultPath = RegUtil.path(item);
            }
            case ItemStack stack -> {
                resultKey = RegUtil.key(stack.getItem());
                resultPath = RegUtil.path(stack.getItem());
            }
            case Fluid fluid -> {
                resultKey = RegUtil.key(fluid);
                resultPath = RegUtil.path(fluid);
            }
            case FluidStack stack -> {
                resultKey = RegUtil.key(stack.getFluid());
                resultPath = RegUtil.path(stack.getFluid());
            }
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }

        if (modIds != null) {
            for (String modId : modIds) {
                if (modId != null) {
                    if (modId.startsWith("no:")){
                        conditions.add(new NotCondition(new ModLoadedCondition(modId.replace("no:", ""))));
                    } else {
                        conditions.add(new ModLoadedCondition(modId));
                    }
                }
            }
        }

        for (Object object : objects) {
            switch (object) {
                case ItemLike item -> {
                    if (isNewCond(item) && !itemUnlocks.contains(item)) conditions.add(new NotDisabledCondition(RegUtil.key(item.asItem())));
                    itemUnlocks.add(item);
                }
                case ItemStack stack -> {
                    Item item = stack.getItem();
                    if (isNewCond(item) && !itemUnlocks.contains(item)) conditions.add(new NotDisabledCondition(RegUtil.key(item)));
                    itemUnlocks.add(item);

                    PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                    if (contents != null && contents.potion().isPresent()) {
                        ResourceLocation loc = contents.potion().get().unwrapKey().map(ResourceKey::location).orElse(null);
                        if (loc != null) potionMap.put(item, loc.getPath());
                    }
                }
                case TagKey<?> tagKey -> {
                    TagKey<Item> tag = (TagKey<Item>) tagKey;
                    if (isNewCond(tag) && !tagUnlocks.contains(tag)) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                    tagUnlocks.add(tag);
                }
                case Fluid fluid -> fluids.add(fluid);
                case FluidStack fluid -> fluids.add(fluid.getFluid());
                case SizedFluidIngredient stack -> fluids.add(Arrays.stream(stack.getFluids()).findFirst().orElse(null).getFluid());
                case Ingredient ingredient -> {
                    if (ingredient.isCustom()) {
                        break;
                    }

                    for (Ingredient.Value value : ingredient.getValues()) {
                        switch (value) {
                            case Ingredient.ItemValue(ItemStack item) -> {
                                ItemLike itemm = item.getItem();
                                if (isNewCond(itemm) && !itemUnlocks.contains(itemm)) conditions.add(new NotDisabledCondition(RegUtil.key(itemm.asItem())));
                                itemUnlocks.add(itemm);
                            }
                            case Ingredient.TagValue(TagKey<Item> tag) -> {
                                if (isNewCond(tag)&& !tagUnlocks.contains(tag)) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                                tagUnlocks.add(tag);
                            }
                            default -> {}
                        }
                    }
                }
                default -> {}
            }
        }

        if (CompatUtil.item(modId, resultPath) != null) conditions.add(new NotDisabledCondition(resultKey));
        consumer.accept(builder);

        for (ItemLike item : itemUnlocks) {
            String path = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
            builder.unlockedBy("has_" + path, RecipeProvider.has(item));
        }

        for (TagKey<Item> tag : tagUnlocks) {
            builder.unlockedBy("has_" + tag.location().getPath(), RecipeProvider.has(tag));
        }

        Set<ItemLike> exclusions = Set.of(Items.GLASS_BOTTLE, Items.BOWL, Items.BUCKET, Items.STICK);

        String path = type + "/" + resultPath;
        ResourceLocation location = IPUtil.loc(modId, path);

        if (result instanceof Fluid || LOCATIONS.contains(location)) {
            String newPath = path;
            if (!itemUnlocks.isEmpty()) {
                ItemLike first = result instanceof Fluid ? itemUnlocks.stream().filter(i -> !exclusions.contains(i.asItem())).findFirst().orElse(null) : itemUnlocks.stream().findFirst().orElse(null);

                for (Map.Entry<ItemLike, String> entry : potionMap.entrySet()) {
                    ItemLike item = entry.getKey();
                    if (item == first) {
                        potion = "_" + entry.getValue();
                        break;
                    }
                }
                newPath = path + "_from_" + RegUtil.path(first) + potion;
            } else if (!fluids.isEmpty()) {
                Fluid first = fluids.stream().findFirst().orElse(null);
                newPath = path + "_from_" + RegUtil.path(first);
            }

            location = IPUtil.loc(modId, newPath.replace("_" + resultPath + "_", "_"));
        }

        LOCATIONS.add(location);
        builder.save(output.withConditions(conditions.toArray(new ICondition[0])), location);
    }

    public void overrideBlasting(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, String path) {
        overrideRecipe(output, SimpleCookingRecipeBuilder.blasting(toIngredient(object), category, result, exp, IPUtil.secondsToTicks(seconds)), b -> {}, path + "_from_blasting", result, object);
    }

    public void overrideBlasting(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object) {
        overrideBlasting(output, category, result, exp, seconds, object, RegUtil.path(result));
    }

    public void overrideCampfireCooking(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, String path) {
        overrideRecipe(output, SimpleCookingRecipeBuilder.campfireCooking(toIngredient(object), category, result, exp, IPUtil.secondsToTicks(seconds)), b -> {}, path + "_from_campfire_cooking", result, object);
    }

    public void overrideCampfireCooking(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object) {
        overrideCampfireCooking(output, category, result, exp, seconds, object, RegUtil.path(result));
    }

    public void overrideSmoking(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, String path) {
        overrideRecipe(output, SimpleCookingRecipeBuilder.smoking(toIngredient(object), category, result, exp, IPUtil.secondsToTicks(seconds)), b -> {}, path + "_from_smoking", result, object);
    }

    public void overrideSmoking(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object) {
        overrideSmoking(output, category, result, exp, seconds, object, RegUtil.path(result) );
    }

    public void overrideSmelting(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, String path) {
        overrideRecipe(output, SimpleCookingRecipeBuilder.smelting(toIngredient(object), category, result, exp, IPUtil.secondsToTicks(seconds)), b -> {}, path, result, object);
    }

    public void overrideSmelting(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object) {
        overrideSmelting(output, category, result, exp, seconds, object, RegUtil.path(result));
    }

    public void overrideShapeless(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object[] objects, String path) {
        overrideRecipe(output, ShapelessRecipeBuilder.shapeless(category, result, count), b -> {
            for (Object object : objects) {
                b.requires(toIngredient(object));
            }
        }, path, result, objects);
    }

    public void overrideShapeless(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object[] objects) {
        overrideShapeless(output, category, result, count, objects, RegUtil.path(result));
    }

    @SuppressWarnings("unchecked")
    public void overrideShaped(RecipeOutput output, RecipeCategory category, ItemLike result, int count, String[] pattern, Map<Character, Object> map, String path) {
        overrideRecipe(output, ShapedRecipeBuilder.shaped(category, result, count), b -> {
            for (String row : pattern) b.pattern(row);
            map.forEach((character, object) -> {
                switch (object) {
                    case ItemLike item -> b.define(character, item);
                    case ItemStack stack -> b.define(character, toIngredient(stack));
                    case Ingredient ingredient -> b.define(character, ingredient);
                    case TagKey<?> tag -> b.define(character, (TagKey<Item>) tag);
                    default -> throw new IllegalStateException("Unexpected value: " + object);
                }
            });
        }, path, result, map.values().toArray());
    }

    public void overrideShaped(RecipeOutput output, RecipeCategory category, ItemLike result, int count, String[] pattern, Map<Character, Object> map) {
        overrideShaped(output, category, result, count, pattern, map, RegUtil.path(result));
    }

    public <B extends RecipeBuilder> void overrideRecipe(RecipeOutput output, B builder, Consumer<B> consumer, String modId, String path, Object result, Object... objects) {
        Set<ItemLike> itemUnlocks = new LinkedHashSet<>();
        Set<TagKey<Item>> tagUnlocks = new LinkedHashSet<>();
        Set<ICondition> conditions = new LinkedHashSet<>();
        ResourceLocation location = IPUtil.loc(modId, path);
        ResourceLocation resultKey;
        String resultPath;

        switch (result) {
            case ItemLike item -> {
                resultKey = RegUtil.key(item);
                resultPath = RegUtil.path(item);
            }
            case ItemStack stack -> {
                resultKey = RegUtil.key(stack.getItem());
                resultPath = RegUtil.path(stack.getItem());
            }
            case Fluid fluid -> {
                resultKey = RegUtil.key(fluid);
                resultPath = RegUtil.path(fluid);
            }
            case FluidStack stack -> {
                resultKey = RegUtil.key(stack.getFluid());
                resultPath = RegUtil.path(stack.getFluid());
            }
            default -> throw new IllegalStateException("Unexpected value: " + result);
        }
        for (Object object : objects) {
            switch (object) {
                case ItemLike item -> {
                    if (isNewCond(item) && !itemUnlocks.contains(item)) conditions.add(new NotDisabledCondition(RegUtil.key(item.asItem())));
                    itemUnlocks.add(item);
                }
                case ItemStack stack -> {
                    Item item = stack.getItem();
                    if (isNewCond(item) && !itemUnlocks.contains(item)) conditions.add(new NotDisabledCondition(RegUtil.key(item)));
                    itemUnlocks.add(item);
                }
                case TagKey<?> tagKey -> {
                    TagKey<Item> tag = (TagKey<Item>) tagKey;
                    if (isNewCond(tag) && !tagUnlocks.contains(tag)) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                    tagUnlocks.add(tag);
                }
                default -> {}
            }
        }

        if (CompatUtil.item(modId, resultPath) != null) conditions.add(new NotDisabledCondition(resultKey));
        consumer.accept(builder);

        for (ItemLike item : itemUnlocks) {
            String iPath = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
            builder.unlockedBy("has_" + iPath, RecipeProvider.has(item));
        }

        for (TagKey<Item> tag : tagUnlocks) {
            builder.unlockedBy("has_" + tag.location().getPath(), RecipeProvider.has(tag));
        }

        LOCATIONS.add(location);
        builder.save(output.withConditions(conditions.toArray(new ICondition[0])), location);
    }

    public <B extends RecipeBuilder> void overrideRecipe(RecipeOutput output, B builder, Consumer<B> consumer, String path, Object result, Object... objects) {
        overrideRecipe(output, builder, consumer, "minecraft", path, result, objects);
    }

    public <B extends RecipeBuilder> void overrideRecipe(RecipeOutput output, B builder, Consumer<B> consumer, ItemLike result, Object... objects) {
        overrideRecipe(output, builder, consumer, "minecraft", RegUtil.path(result), objects);
    }

    protected void disableRecipe(RecipeOutput output, ResourceLocation location) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.KNOWLEDGE_BOOK).requires(Items.KNOWLEDGE_BOOK).unlockedBy("disabled", has(Items.KNOWLEDGE_BOOK)).save(output.withConditions(FalseCondition.INSTANCE), location);
    }

    protected void disableRecipe(RecipeOutput output, String path) {
        disableRecipe(output, IPUtil.mcLoc(path));
    }

    protected void disableRecipes(RecipeOutput output, String... paths) {
        for (String path : paths) {
            disableRecipe(output, IPUtil.mcLoc(path));
        }
    }

    protected void disableRecipes(RecipeOutput output, ItemLike... items) {
        for (ItemLike item : items) {
            disableRecipe(output, IPUtil.mcLoc(RegUtil.path(item)));
        }
    }

    protected ICondition[] toConds(ItemLike result, String[] modIds, Object... objects) {
        List<ICondition> conditions = new ArrayList<>();

        if (modIds != null) {
            for (String modId : modIds) {
                if (modId != null) {
                    if (modId.startsWith("no:")){
                        conditions.add(new NotCondition(new ModLoadedCondition(modId.replace("no:", ""))));
                    } else {
                        conditions.add(new ModLoadedCondition(modId));
                    }
                }
            }
        }

        for (Object object : objects) {
            switch (object) {
                case ItemLike item -> {
                    if (!RegUtil.isVanilla(RegUtil.key(item))) {
                        conditions.add(new NotDisabledCondition(RegUtil.key(item.asItem())));
                    }
                }
                case TagKey<?> tagKey -> {
                    TagKey<Item> tag = (TagKey<Item>) tagKey;
                    if (!RegUtil.isVanilla(tag.location())) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                }
                case Ingredient ingredient -> {
                    for (Ingredient.Value value : ingredient.getValues()) {
                        switch (value) {
                            case Ingredient.ItemValue(ItemStack item) -> {
                                ItemLike itemm = item.getItem();
                                if (!RegUtil.isVanilla(RegUtil.key(itemm))) {
                                    conditions.add(new NotDisabledCondition(RegUtil.key(itemm.asItem())));
                                }
                            }
                            case Ingredient.TagValue(TagKey<Item> tag) -> {
                                if (!RegUtil.isVanilla(tag.location())) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                            }
                            default -> {}
                        }
                    }
                }
                default -> {}
            }
        }

        conditions.add(new NotDisabledCondition(RegUtil.key(result.asItem())));

        return conditions.toArray(new ICondition[0]);
    }

    @SuppressWarnings("unchecked")
    protected Ingredient toIngredient(Object object) {
        switch (object) {
            case Ingredient ingredient -> {
                return ingredient;
            }
            case ItemLike item -> {
                return Ingredient.of(item);
            }
            case ItemStack stack -> {
                if (!stack.getComponentsPatch().isEmpty()) {
                    return DataComponentIngredient.of(false, stack);
                }
                return Ingredient.of(stack);
            }
            case TagKey<?> tag -> {
                return Ingredient.of((TagKey<Item>) tag);
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }

    protected Object[] handleFDObjects(Object[] fDObjects) {
        List<Object> objects = new ArrayList<>();

        for (Object obj : fDObjects) {
            if (objects.size() >= 9) break;

            if (obj instanceof TagKey<?> tag) {
                if (tag.equals(IPItemTags.DOUGH) && objects.size() <= 7) {
                    objects.add(Items.WHEAT);
                    objects.add(Items.WHEAT);
                    continue;
                }
            }

            if (obj instanceof ItemLike itemLike) {
                Item item = itemLike.asItem();
                if (RegUtil.key(item).getNamespace().equals(IPCompat.FD)) {
                    if (item == CompatUtil.fDItem("pie_crust") && objects.size() <= 7) {
                        objects.add(Tags.Items.DRINKS_MILK);
                        objects.add(Items.WHEAT);
                        continue;
                    }
                }
            }
            if(!objects.contains(obj)) objects.add(obj);
        }

        return objects.toArray();
    }

    protected Object[] getObjects(String[] pattern, Map<Character, Object> map, boolean only) {
        List<Object> list = new ArrayList<>();
        Set<Object> set = new LinkedHashSet<>();

        for (String row : pattern) {
            for (char c : row.toCharArray()) {
                if (c == ' ') continue;
                list.add(map.get(c));
                set.add(map.get(c));
            }
        }
        return only ? set.toArray() : list.toArray();
    }

    protected boolean isNewCond(ItemLike item) {
       return !RegUtil.isVanilla(RegUtil.key(item));
    }

    protected boolean isNewCond(TagKey<Item> key) {
        return !RegUtil.isVanilla(key.location());
    }

}
