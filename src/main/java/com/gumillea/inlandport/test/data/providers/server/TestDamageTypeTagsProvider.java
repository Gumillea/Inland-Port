package com.gumillea.inlandport.test.data.providers.server;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.server.IPBlockTagsProvider;
import com.gumillea.inlandport.core.util.tags.IPDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TestDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public TestDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
        super(output, provider, InlandPort.MODID, helper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(IPDamageTypeTags.BYPASSES_DODGE).add(DamageTypes.STARVE, DamageTypes.FALL, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.DROWN, DamageTypes.FREEZE).addTag(DamageTypeTags.IS_FIRE);
    }

}
