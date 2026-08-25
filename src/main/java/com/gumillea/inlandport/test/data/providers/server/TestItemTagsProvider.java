package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPBlockTagsProvider;
import com.gumillea.inlandport.core.data.providers.server.IPItemTagsProvider;
import com.gumillea.inlandport.core.util.tags.IPItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TestItemTagsProvider extends IPItemTagsProvider {

    public TestItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> lookUp) {
        super(output, InlandPort.MODID, provider, lookUp);
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
    }

}
