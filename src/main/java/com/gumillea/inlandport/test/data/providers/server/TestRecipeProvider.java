package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPRecipeProvider;
import com.gumillea.inlandport.core.util.utils.EffectUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.concurrent.CompletableFuture;

public class TestRecipeProvider extends IPRecipeProvider {

    public TestRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, InlandPort.MODID, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput output, HolderLookup.Provider provider) {
        injectingRecipe(output, RegUtil.stack(Items.GLASS_BOTTLE), Fluids.WATER, 250, EffectUtil.potion(Potions.WATER));
        extractingRecipe(output, PotionContents.createItemStack(Items.POTION, Potions.WATER), Fluids.WATER, 250, RegUtil.stack(Items.GLASS_BOTTLE));
        fluidBucketRecipe(output, Fluids.WATER, Items.WATER_BUCKET);
        fluidBucketRecipe(output, Fluids.LAVA, Items.LAVA_BUCKET);
        fluidBucketRecipe(output, NeoForgeMod.MILK.get(), Items.MILK_BUCKET);
    }

}
