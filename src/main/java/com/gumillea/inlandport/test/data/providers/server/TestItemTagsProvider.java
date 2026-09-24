package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPItemTagsProvider;
import com.gumillea.inlandport.core.util.IPCompat;
import com.gumillea.inlandport.core.util.tags.IPItemTags;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class TestItemTagsProvider extends IPItemTagsProvider {

    public TestItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> lookUp) {
        super(output, provider, InlandPort.MODID, lookUp);
    }

    @Override
    public void addManualTags() {
        addIfExist(IPItemTags.APPLE, Items.APPLE);
        addIfExist(IPItemTags.SWEET_BERRY, Items.SWEET_BERRIES);
        addIfExist(IPItemTags.GLOW_BERRY, Items.GLOW_BERRIES);

        addIfExist(IPItemTags.CARROT, Items.CARROT);
        addIfExist(IPItemTags.BEETROOT, Items.BEETROOT);
        addIfExist(IPItemTags.POTATO, Items.POTATO);
        addIfExist(IPItemTags.SUGARCANE, Items.SUGAR_CANE);

        addIfExist(IPItemTags.MOSS, Items.MOSS_BLOCK, Items.MOSS_CARPET);

        addIfExist(IPItemTags.MILK_BOTTLE, CompatUtil.fDItem("milk_bottle"));
        addIfExist(Tags.Items.DRINKS_MILK, IPItemTags.MILK_BOTTLE);
    }

}
