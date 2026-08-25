package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class TestLootTableProvider extends IPLootTableProvider {

    public TestLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, InlandPort.MODID, future);
    }

}
