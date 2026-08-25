package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class IPItemModelProvider extends ItemModelProvider {

    private final String modId;

    public IPItemModelProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        overrideModels();
        AutoDataGeneHelper.autoGeneItemModels(this, modId);
    }

    public void overrideModels() {}

    public boolean hasExistingJson(String path) {
        ResourceLocation location = IPUtil.loc(modId, "item/" + path);
        return existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".json", "models");
    }

    public boolean hasExistingTexture(String path) {
        ResourceLocation location = IPUtil.loc(modId, "item/" + path);
        return existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".png", "textures");
    }

    public void addItem(Item... items) {
        for (Item item : items)
            this.basicItem(item);
    }

    public void addHandheldItem(Item... items) {
        for (Item item : items)
            this.handheldItem(item);
    }

    public void addSpawnEggItem(Item... items) {
        for (Item item : items)
            this.spawnEggItem(item);
    }

}
