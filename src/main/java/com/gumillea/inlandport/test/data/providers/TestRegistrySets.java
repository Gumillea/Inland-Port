package com.gumillea.inlandport.test.data.providers;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.test.reg.IPJukeboxSongs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TestRegistrySets extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.JUKEBOX_SONG, IPJukeboxSongs::bootstrap);

    public TestRegistrySets(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(InlandPort.MODID));
    }
}
