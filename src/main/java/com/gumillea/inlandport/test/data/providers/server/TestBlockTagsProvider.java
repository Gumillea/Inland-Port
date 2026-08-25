package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPBlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TestBlockTagsProvider extends IPBlockTagsProvider {

    public TestBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
        super(output, provider, InlandPort.MODID, helper);
    }
}
