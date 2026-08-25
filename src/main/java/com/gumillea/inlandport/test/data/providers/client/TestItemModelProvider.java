package com.gumillea.inlandport.test.data.providers.client;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.client.IPItemModelProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TestItemModelProvider extends IPItemModelProvider {

    public TestItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, InlandPort.MODID, helper);
    }

}
