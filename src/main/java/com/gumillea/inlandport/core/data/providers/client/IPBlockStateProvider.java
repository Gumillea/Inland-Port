package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.block.EdibleBlock;
import com.gumillea.inlandport.common.block.family.wooden.LogBlock;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class IPBlockStateProvider extends BlockStateProvider {

    private final String modId;

    public IPBlockStateProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.modId = modId;
    }

    @Override
    protected void registerStatesAndModels() {
        overrideModels();
        AutoDataGeneHelper.autoGeneBlockModels(this, modId);
    }

    public void overrideModels() {
    }

    public boolean hasExistingModel(String path) {
        ResourceLocation location = IPUtil.loc(modId, "block/" + path);
        return this.models().existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".json", "models");
    }

    public boolean hasExistingState(String path) {
        ResourceLocation location = IPUtil.loc(modId, path);
        return this.models().existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".json", "blockstates");
    }

    public boolean hasExistingTexture(String path) {
        ResourceLocation location = IPUtil.loc(modId, "block/" + path);
        return this.models().existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".png", "textures");
    }

    @Override
    public void simpleBlock(@NotNull Block block) {
        this.simpleBlock(block, cubeAll(block));
        this.simpleBlockItem(block);
    }

    public void rotatedPillarBlock(RotatedPillarBlock block) {
        super.axisBlock(block, this.blockTexture(block), IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_top"));
        this.simpleBlockItem(block);
    }

    public void cubeColumnBlock(Block block) {
        ResourceLocation texture = this.blockTexture(block);
        this.simpleBlock(block, models().cubeColumn(RegUtil.path(block), texture, IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_top")));
        this.simpleBlockItem(block);
    }

    public void cubeBottomTop(Block block) {
        this.simpleBlock(block, models().cubeBottomTop(RegUtil.path(block), IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_side"), IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_bottom"), IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_top")));
        this.simpleBlockItem(block);
    }

    public void chiseledBlock(Block block) {
        if (this.hasExistingTexture(RegUtil.path(block) + "_top")) {
            cubeColumnBlock(block);
        } else {
            simpleBlock(block);
        }
    }

    public void logOrWoodBlock(LogBlock block) {
        String blockId = RegUtil.path(block);
        if (blockId.endsWith("_log") || blockId.endsWith("_stem")){
            this.logBlock(block);
        } else {
            Block baseBlock = CompatUtil.block(modId, blockId.replace("_wood", "_log").replace("_hyphae", "_stem"));
            this.axisBlock(block, this.blockTexture(baseBlock), this.blockTexture(baseBlock));
        }
        this.simpleBlockItem(block);
    }

    public void slabBlock(SlabBlock block, Block baseBlock) {
        this.slabBlock(block, RegUtil.key(baseBlock), this.blockTexture(baseBlock));
        this.simpleBlockItem(block);
    }

    public void slabBlock(Block block, Block baseBlock) {
        if (block instanceof SlabBlock slab) slabBlock(slab, baseBlock);
    }

    public void stairsBlock(StairBlock block, Block baseBlock) {
        this.stairsBlock(block, this.blockTexture(baseBlock));
        this.simpleBlockItem(block);
    }

    public void stairsBlock(Block block, Block baseBlock) {
        if (block instanceof StairBlock stairs) stairsBlock(stairs, baseBlock);
    }

    public void wallBlock(WallBlock block, Block baseBlock) {
        ResourceLocation texture = this.blockTexture(baseBlock);
        this.wallBlock(block, texture);
        this.simpleBlockItem(block, this.models().wallInventory(RegUtil.key(block) + "_inventory", texture));
    }

    public void wallBlock(Block block, Block baseBlock) {
        if (block instanceof WallBlock wall) wallBlock(wall, baseBlock);
    }

    public void fenceBlock(FenceBlock block, Block baseBlock) {
        ResourceLocation texture = this.blockTexture(baseBlock);
        this.fenceBlock(block, texture);
        this.simpleBlockItem(block, this.models().fenceInventory(RegUtil.key(block) + "_inventory", texture));
    }

    public void fenceBlock(Block block, Block baseBlock) {
        if (block instanceof FenceBlock fence) fenceBlock(fence, baseBlock);
    }

    public void fenceGateBlock(FenceGateBlock block, Block baseBlock) {
        this.fenceGateBlock(block, this.blockTexture(baseBlock));
        this.simpleBlockItem(block);
    }

    public void fenceGateBlock(Block block, Block baseBlock) {
        if (block instanceof FenceGateBlock fenceGate) fenceGateBlock(fenceGate, baseBlock);
    }

    public void pressurePlateBlock(PressurePlateBlock block, Block baseBlock) {
        this.pressurePlateBlock(block, this.blockTexture(baseBlock));
        this.simpleBlockItem(block);
    }

    public void pressurePlateBlock(Block block, Block baseBlock) {
        if (block instanceof PressurePlateBlock pressurePlate) pressurePlateBlock(pressurePlate, baseBlock);
    }

    public void buttonBlock(ButtonBlock block, Block baseBlock) {
        ResourceLocation texture = this.blockTexture(baseBlock);
        this.buttonBlock(block, texture);
        this.simpleBlockItem(block, this.models().buttonInventory(RegUtil.key(block) + "_inventory", texture));
    }

    public void buttonBlock(Block block, Block baseBlock) {
        if (block instanceof ButtonBlock button) buttonBlock(button, baseBlock);
    }

    public void doorBlock(DoorBlock block) {
        this.doorBlock(block, IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_bottom"), IPUtil.loc(modId, "block/" + RegUtil.path(block) + "_top"));
        this.basicItem(block.asItem());
    }

    public void doorBlock(Block block) {
        if (block instanceof DoorBlock door) doorBlock(door);
    }

    public void trapDoorBlock(TrapDoorBlock block) {
        ResourceLocation texture = this.blockTexture(block);
        this.trapdoorBlock(block, texture, true);
        this.simpleBlockItem(block, this.models().trapdoorOrientableBottom(RegUtil.key(block) + "_bottom", texture));
    }

    public void trapDoorBlock(Block block) {
        if (block instanceof TrapDoorBlock door) trapDoorBlock(door);
    }

    public void signBlock(Block sign, Block block) {
        if (sign instanceof StandingSignBlock || sign instanceof CeilingHangingSignBlock) {
            ResourceLocation texture = this.blockTexture(block);
            ModelFile signModel = models().getBuilder(RegUtil.path(sign)).texture("particle", texture);

            getVariantBuilder(sign).forAllStates(state -> ConfiguredModel.builder().modelFile(signModel).build());
            this.basicItem(sign.asItem());
        }
    }

    public void wallSignBlock(Block sign, Block block) {
        if (sign instanceof WallSignBlock || sign instanceof WallHangingSignBlock) {
            ResourceLocation texture = this.blockTexture(block);
            ModelFile signModel = models().getBuilder(RegUtil.path(sign)).texture("particle", texture);
            getVariantBuilder(sign).forAllStates(state -> ConfiguredModel.builder().modelFile(signModel).build());
        }
    }

    public void honeyBlock(Block block) {
        String path = RegUtil.path(block);
        String texture = "block/" + RegUtil.path(block);

        ModelFile honeyBlock = models().withExistingParent(path, mcLoc("block/honey_block"))
                .texture("particle", texture)
                .texture("down", texture)
                .texture("up", texture)
                .texture("side", texture);

        simpleBlock(block, honeyBlock);
        simpleBlockItem(block);
    }

    public void edibleBlock(EdibleBlock block) {
        String texture = "block/" + RegUtil.path(block);

        getVariantBuilder(block).forAllStates(state -> {
            int bites = state.getValue(EdibleBlock.BITES);
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            String suffix = bites > 0 && bites < block.getMaxBites() ? "_slice" + bites : "";
            ResourceLocation parent = block.getType() == EdibleBlock.Type.CAKE ? mcLoc("block/cake") : modLoc("block/pie" + suffix);

            BlockModelBuilder model = models().withExistingParent("block/" + InlandPort.MODID + suffix, parent)
                    .texture("particle", IPUtil.loc(modId,texture + "_inner"))
                    .texture("top", IPUtil.loc(modId, texture + "_top"))
                    .texture("bottom", IPUtil.loc(modId,texture + "_bottom"))
                    .texture("side", IPUtil.loc(modId,texture + "_side"));

            if (bites > 0) {
                model.texture("inner", IPUtil.loc(modId, texture + "_inner"));
            }

            return ConfiguredModel.builder().modelFile(model).rotationY(((int) facing.toYRot() + 180) % 360).build();
        });
    }

    public void simpleBlockItem(@NotNull Block block) {
        this.simpleBlockItem(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
    }

    public ItemModelBuilder basicItem(Item item) {
        return this.basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder basicItem(ResourceLocation item) {
        return itemModels().getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

}
