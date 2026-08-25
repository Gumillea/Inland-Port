package com.gumillea.inlandport.test.data.modifiers;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.modifiers.IPLootTableModifier;
import com.gumillea.inlandport.test.reg.IPItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.concurrent.CompletableFuture;

public class TestLootTableModifier extends IPLootTableModifier {

    public TestLootTableModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future, InlandPort.MODID);
    }

    @Override
    protected void start() {
        this.inject(IPItems.MUSIC_DISC_FLEKKEFJORD.value(), BuiltInLootTables.ABANDONED_MINESHAFT, 0.15F);
    }
}
