package com.gumillea.inlandport.core.data.providers.server;

import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.block.StorageBlock;
import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.common.block.family.stone.StoneBaseBlock;
import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.IPCompat;
import com.gumillea.inlandport.core.util.tags.IPItemTags;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.*;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class IPRecipeProvider extends RecipeProvider {

    private final Set<ResourceLocation> LOCATIONS = new HashSet<>();
    private final String modId;

    private String no(String modId){
        return "no:" + modId;
    }

    public IPRecipeProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
        this.modId = modId;
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
            if (hangingSign != null && strippedLog != null) {
                hangingSignRecipe(output, hangingSign, strippedLog, modIds);
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
    public void pieRecipe(RecipeOutput output, ItemLike result, String[] pattern, Map<Character, Object> map, String... modIds) {
        shapelessRecipe(output, RecipeCategory.FOOD, result, 1, handleFDObjects(getObjects(pattern, map)), ArrayUtils.addAll(new String[] {no(IPCompat.FD)}, modIds));
        shapedRecipe(output, RecipeCategory.FOOD, result, 1, pattern, map, ArrayUtils.addAll(new String[] {IPCompat.FD}, modIds));
        if (result instanceof EdibleBlock edibleBlock) {
            Item slice = edibleBlock.getSlice().getItem();
            cuttingRecipe(output, slice, 4,1F, edibleBlock, IPItemTags.KNIVES, modIds, null);
            shapelessRecipe(output, RecipeCategory.FOOD, result, 1, new Object[]{slice, slice, slice, slice},modIds);
        }
    }

    //FD - Cooking
    public void simpleFDCooking(RecipeOutput output, CookingPotRecipeBookTab tab, ItemLike result, int count, @Nullable ItemLike container, Object[] objects, String... modIds) {
        cookingRecipe(output, tab, result, count, 200, 1, container, objects, modIds);
    }

    public void bowlMealCooking(RecipeOutput output, ItemLike result, int count, Object[] objects, String... modIds) {
        cookingRecipe(output, CookingPotRecipeBookTab.MEALS, result, count, 200, 1, Items.BOWL, objects, modIds);
    }

    //FD - Cutting
    public void logCutting(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        cuttingRecipe(output, result, 1,1F, object, ItemTags.AXES, modIds, b -> b.addResult(CompatUtil.fDItem("tree_bark")));
    }

    public void planksCutting(RecipeOutput output, ItemLike result, Object object, String... modIds) {
        simpleCuttingWithChance(output, result, 0.75F, object, ItemTags.AXES, modIds);
    }

    public void simpleCutting(RecipeOutput output, ItemLike result, Object object, Object tool, String[] modIds) {
        cuttingRecipe(output, result, 1,1F, object, tool, modIds, null);
    }

    public void simpleCuttingWithChance(RecipeOutput output, ItemLike result, float chance, Object object, Object tool, String[] modIds) {
        cuttingRecipe(output, result, 1, chance, object, tool, modIds, null);
    }

    //Smelting
    public void oreSmeltingRecipe(RecipeOutput output, ItemLike result, float exp, float seconds, Object object, String[] modIds) {
        smeltingRecipe(output, RecipeCategory.MISC, result, exp, seconds, object, modIds);
        blastingRecipe(output, RecipeCategory.MISC, result, exp, seconds / 2, object, modIds);
    }

    public void oreSmeltingRecipe(RecipeOutput output, ItemLike result, Object object, String[] modIds) {
        oreSmeltingRecipe(output, result, 0.7F, 10, object, modIds);
    }

    public void foodCookingRecipe(RecipeOutput output, ItemLike result, float exp, float seconds, Object object, String[] modIds) {
        smeltingRecipe(output, RecipeCategory.FOOD, result, exp, seconds, object, modIds);
        smokingRecipe(output, RecipeCategory.FOOD, result, exp, seconds / 2, object, modIds);
        campfireCookingRecipe(output, RecipeCategory.FOOD, result, exp, seconds * 3, object, modIds);
    }

    public void foodCookingRecipe(RecipeOutput output, ItemLike result, Object object, String[] modIds) {
        foodCookingRecipe(output, result, 0.35F, 10, object, modIds);
    }

    //Shaped
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

    //
    public void cuttingRecipe(RecipeOutput output, ItemLike result, int count, float chance, Object object, Object tool, @Nullable String[] modIds, @Nullable Consumer<CuttingBoardRecipeBuilder> extraResults) {
        regRecipe(output, CuttingBoardRecipeBuilder.cuttingRecipe(toIngredient(object), toIngredient(tool), result, count, chance), result, b -> {
            if (extraResults != null) extraResults.accept(b);
        }, modIds, IPCompat.FD + "/cutting", object, tool);
    }

    public void cookingRecipe(RecipeOutput output, CookingPotRecipeBookTab tab, ItemLike result, int count, int cookingTime, float exp, @Nullable ItemLike container, Object[] objects, @Nullable String[] modIds) {
        regRecipe(output, CookingPotRecipeBuilder.cookingPotRecipe(result, count, cookingTime, exp, container), result, b -> {
            for (Object object : objects) {
                b.setRecipeBookTab(tab);
                b.addIngredient(toIngredient(object));
                if (object instanceof ItemLike itemLike) b.unlockedByAnyIngredient(itemLike);
            }
        }, modIds, IPCompat.FD + "/cooking", objects);
    }

    public void stonecuttingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object object, @Nullable String[] modIds) {
        regRecipe(output, SingleItemRecipeBuilder.stonecutting(toIngredient(object), category, result, count), result, b -> {}, modIds, "stonecutting", object);
    }

    public void blastingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String[] modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.blasting(toIngredient(object), category, result, exp, (int) seconds * 20), result, b -> {}, modIds, "cooking/blasting", object);
    }

    public void campfireCookingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String[] modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.campfireCooking(toIngredient(object), category, result, exp, (int) seconds * 20), result, b -> {}, modIds, "cooking/campfire_cooking", object);
    }

    public void smeltingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String[] modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.smelting(toIngredient(object), category, result, exp, (int) seconds * 20), result, b -> {}, modIds, "cooking/smelting", object);
    }

    public void smokingRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, float exp, float seconds, Object object, @Nullable String[] modIds) {
        regRecipe(output, SimpleCookingRecipeBuilder.smoking(toIngredient(object), category, result, exp, (int) seconds * 20), result, b -> {}, modIds, "cooking/smoking", object);
    }

    public void shapelessRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, Object[] objects, String[] modIds) {
        regRecipe(output, ShapelessRecipeBuilder.shapeless(category, result, count), result, b -> {
            for (Object object : objects) {
                b.requires(toIngredient(object));
            }
        }, modIds, "shapeless", objects);
    }

    @SuppressWarnings("unchecked")
    public void shapedRecipe(RecipeOutput output, RecipeCategory category, ItemLike result, int count, String[] pattern, Map<Character, Object> map, @Nullable String[] modIds) {
        regRecipe(output, ShapedRecipeBuilder.shaped(category, result, count), result, b -> {
            for (String row : pattern) b.pattern(row);
            map.forEach((character, object) -> {
                if (object instanceof ItemLike item) b.define(character, item);
                if (object instanceof TagKey<?> tag) b.define(character, (TagKey<Item>) tag);
            });
        }, modIds, "shaped", map.values().toArray());
    }

    @SuppressWarnings("unchecked")
    public <B extends RecipeBuilder> void regRecipe(RecipeOutput output, B builder, ItemLike result, Consumer<B> consumer, String[] modIds, String type, Object... objects) {
        Set<ItemLike> itemUnlocks = new LinkedHashSet<>();
        Set<TagKey<Item>> tagUnlocks = new LinkedHashSet<>();
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
                    itemUnlocks.add(item);
                    if (!RegUtil.isVanilla(RegUtil.key(item))) conditions.add(new ItemExistsCondition(RegUtil.key(item.asItem())));
                }
                case TagKey<?> tagKey -> {
                    TagKey<Item> tag = (TagKey<Item>) tagKey;
                    tagUnlocks.add(tag);
                    if (!RegUtil.isVanilla(tag.location())) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                }
                case Ingredient ingredient -> {
                    for (Ingredient.Value value : ingredient.getValues()) {
                        switch (value) {
                            case Ingredient.ItemValue(ItemStack item) -> {
                                ItemLike itemm = item.getItem();
                                itemUnlocks.add(itemm);
                                if (!RegUtil.isVanilla(RegUtil.key(itemm))) conditions.add(new ItemExistsCondition(RegUtil.key(itemm.asItem())));
                            }
                            case Ingredient.TagValue(TagKey<Item> tag) -> {
                                tagUnlocks.add(tag);
                                if (!RegUtil.isVanilla(tag.location())) conditions.add(new NotCondition(new TagEmptyCondition(tag.location())));
                            }
                            default -> {}
                        }
                    }
                }
                default -> {}
            }
        }

        consumer.accept(builder);

        for (ItemLike item : itemUnlocks) {
            String path = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
            builder.unlockedBy("has_" + path, RecipeProvider.has(item));
        }

        for (TagKey<Item> tag : tagUnlocks) {
            builder.unlockedBy("has_" + tag.location().getPath(), RecipeProvider.has(tag));
        }

        Set<ItemLike> exclusions = Set.of(Items.GLASS_BOTTLE, Items.BOWL, Items.BUCKET, Items.STICK);

        String path = type + "/" + RegUtil.path(result.asItem());
        ResourceLocation location = IPUtil.loc(modId, path);

        if (LOCATIONS.contains(location)) {
            String newPath = path + "_from_" + RegUtil.path(itemUnlocks.stream().filter(item -> !exclusions.contains(item.asItem())).findFirst().orElse(result).asItem());
            location = IPUtil.loc(modId, newPath);
        }

        LOCATIONS.add(location);
        builder.save(output.withConditions(conditions.toArray(new ICondition[0])), location);
    }

    @SuppressWarnings("unchecked")
    private Ingredient toIngredient(Object object) {
        switch (object) {
            case Ingredient ingredient -> {
                return ingredient;
            }
            case ItemLike item -> {
                return Ingredient.of(item);
            }
            case TagKey<?> tag -> {
                return Ingredient.of((TagKey<Item>) tag);
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }

    private Object[] handleFDObjects(Object[] fDObjects) {
        List<Object> objects = new ArrayList<>();

        for (Object obj : fDObjects) {
            if (objects.size() >= 9) break;

            if (obj instanceof ItemLike itemLike) {
                Item item = itemLike.asItem();
                if (RegUtil.key(item).getNamespace().equals(IPCompat.FD)) {
                    if (item == CompatUtil.fDItem("pie_crust") && objects.size() <= 7) {
                        objects.add(Tags.Items.DRINKS_MILK);
                        objects.add(Items.WHEAT);
                        continue;
                    }
                    if (item == CompatUtil.fDItem("dough") && objects.size() <= 7) {
                        objects.add(Items.WHEAT);
                        objects.add(Items.WHEAT);
                        continue;
                    }
                }
            }
            if(!objects.contains(obj)) objects.add(obj);
        }

        return objects.toArray();
    }

    private Object[] getObjects(String[] pattern, Map<Character, Object> map) {
        List<Object> objects = new ArrayList<>();

        for (String row : pattern) {
            for (char symbol : row.toCharArray()) {
                if (symbol == ' ') continue;
                objects.add(map.get(symbol));
            }
        }
        return objects.toArray();
    }

}
