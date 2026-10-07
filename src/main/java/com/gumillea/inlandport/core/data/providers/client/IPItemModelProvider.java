package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class IPItemModelProvider extends ItemModelProvider {

    protected final String modId;

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
            basicItem(item);
    }

    public void addHandheldItem(Item... items) {
        for (Item item : items)
            handheldItem(item);
    }

    public void addSpawnEggItem(Item... items) {
        for (Item item : items)
            spawnEggItem(item);
    }

    public void addSeparateTransformsModelItem(Item item, ItemDisplayContext context) {
            ResourceLocation baseName = RegUtil.key(item);
            String path = baseName.getPath();
            getBuilder(path + "_gui").parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", modLoc("item/" + path + "_gui"));

            getBuilder(path).parent(getExistingFile(mcLoc("item/generated"))).customLoader(SeparateTransformsModelBuilder::begin).base(nested().parent(getExistingFile(modLoc("item/" + path + "_gui")))).perspective(context, nested().parent(getExistingFile(modLoc("item/" + path + "_3d"))));
    }

    public void addSeparateModelHandheldItem(Item... items) {
        for (Item item : items) {
            ResourceLocation baseName = RegUtil.key(item);
            String path = baseName.getPath();
            getBuilder(path + "_gui").parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", modLoc("item/" + path + "_gui"));

            ResourceLocation model = modLoc("item/" + path + "_3d");

            getBuilder(path).parent(getExistingFile(mcLoc("item/generated"))).customLoader(SeparateTransformsModelBuilder::begin).base(nested().parent(getExistingFile(modLoc("item/" + path + "_gui"))))
                    .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, nested().parent(getExistingFile(model)))
                    .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, nested().parent(getExistingFile(model)))
                    .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, nested().parent(getExistingFile(model)))
                    .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, nested().parent(getExistingFile(model)));
        }
    }

    public void addSimpleHat(Item... items) {
        for (Item item : items) {
            ResourceLocation baseName = RegUtil.key(item);
            String path = baseName.getPath();
            getBuilder(path + "_gui").parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", modLoc("item/" + path));
            if (!hasExistingJson(path + "_3d")) getBuilder(path + "_3d").parent(getExistingFile(IPUtil.loc(InlandPort.MODID,"item/hat"))).texture("hat", modLoc("item/armor/" + path));

            getBuilder(path).parent(getExistingFile(mcLoc("item/generated"))).customLoader(SeparateTransformsModelBuilder::begin).base(nested().parent(getExistingFile(modLoc("item/" + path + "_gui")))).perspective(ItemDisplayContext.HEAD, nested().parent(getExistingFile(modLoc("item/" + path + "_3d"))));

        }
    }

    public void addDurabilityItem(Item... items) {
        for (Item item : items) {
            ResourceLocation baseName = RegUtil.key(item);
            String path = baseName.getPath();
            int maxDamage = item.getMaxDamage(RegUtil.stack(item));

            for (int i = 1; i < maxDamage; i++) {
                String subPath = path + i;
                withExistingParent(subPath, "item/generated").texture("layer0", modLoc("item/" + subPath));
            }

            ItemModelBuilder builder = withExistingParent(path, "item/generated").texture("layer0", modLoc("item/" + path + "0"));

            for (int i = 1; i < maxDamage; i++) {
                builder.override().predicate(IPUtil.mcLoc("damage"), (float) i / maxDamage).model(getExistingFile(modLoc("item/" + path + i))).end();
            }
        }
    }

}
