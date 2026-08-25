package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPRecipeProvider;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.test.reg.IPBlocks;
import com.gumillea.inlandport.test.reg.IPItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TestRecipeProvider extends IPRecipeProvider {

    public TestRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, InlandPort.MODID, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput output, HolderLookup.Provider provider) {
        stoneFamily(output, IPBlocks.STROMATOLITE.value());
        woodFamily(output, IPBlocks.GUMILLEA.value());

        storageBlockRecipe(output, IPBlocks.ANSAULT_CRATE.value());
        pieRecipe(output, IPBlocks.ANSAULT_PIE.get(), new String[]{"#E#", "#S#", " C "}, Map.of('#', IPItems.ANSAULT.get(), 'E', Tags.Items.EGGS, 'S', Items.SUGAR, 'C', CompatUtil.fDItem("pie_crust")));
        cookingRecipe(output, CookingPotRecipeBookTab.MISC, IPBlocks.SLUG_HUSK.get(),2,200, 1, Items.ICE, new Object[]{IPItems.ANSAULT.get(), Blocks.ACACIA_LOG}, null);
    }


}
