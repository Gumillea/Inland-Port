package com.gumillea.inlandport.core.data.providers.server;

import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class IPDataMapProvider extends DataMapProvider {
    private final String modId;

    public IPDataMapProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
        this.modId = modId;
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        gatherManualMaps();

        AutoDataGeneHelper.autoGeneDataMap(this, modId);
    }

    public void gatherManualMaps() {}

    public void addFuel(Object time, Item... items) {
        for (Item item : items) {
            if (item != null) this.builder(NeoForgeDataMaps.FURNACE_FUELS).add(item.builtInRegistryHolder(), new FurnaceFuel(IPUtil.toTicks(time)), false);
        }
    }

    public void addFuel(Object time, Block... blocks) {
        for (Block block : blocks) {
            if (block != null) addFuel(time, block.asItem());
        }
    }

    public void replaceFuel(Object time, Item... items) {
        for (Item item : items) {
            if (item != null)
                this.builder(NeoForgeDataMaps.FURNACE_FUELS).add(item.builtInRegistryHolder(), new FurnaceFuel(IPUtil.toTicks(time)), true);
        }
    }

    public void replaceFuel(Object time, Block... blocks) {
        for (Block block : blocks) {
            if (block != null) replaceFuel(time, block.asItem());
        }
    }

    public void setCompostable(float chance, Item... items) {
        for (Item item : items) {
            if (item != null) this.builder(NeoForgeDataMaps.COMPOSTABLES).add(item.builtInRegistryHolder(), new Compostable(chance), false);
        }
    }

    public void setCompostable(float chance, Block... blocks) {
        for (Block block : blocks) {
            if (block != null) setCompostable(chance, block.asItem());
        }
    }

}
