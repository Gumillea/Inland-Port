package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPDataMapProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class TestDataMapProvider extends IPDataMapProvider {

    public TestDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, InlandPort.MODID, provider);
    }

}
