package com.gumillea.inlandport.core.data.providers.server;

import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.common.block.family.stone.StoneBaseBlock;
import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class IPBlockTagsProvider extends BlockTagsProvider {
    private final String modId;

    public IPBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper helper) {
        super(output, provider, modId, helper);
        this.modId = modId;
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        AutoDataGeneHelper.autoGeneBlockTags(this, modId);
        addManualTags();
    }

    public void addManualTags() {}

    public IntrinsicTagAppender<Block> tag(TagKey<Block> tagKey) {
        return super.tag(tagKey);
    }

    public void addIfExist(TagKey<Block> tagKey, Block... blocks) {
        for (Block block : blocks) {
            if (block != null) {
                this.tag(tagKey).add(block);
            }
        }
    }

    public void addStoneFamily(StoneBaseBlock family) {
        this.addIfExist(Tags.Blocks.STONES, family);
        for (Block block : Variant.getAllBlocks(family, StoneBaseBlock.REG_MAP)) {
            this.addIfExist(BlockTags.MINEABLE_WITH_PICKAXE, block);
        }
    }

    public void addWoodenFamily(WoodenBaseBlock family) {
        Block log = family.getLog();
        Block strippedLog = family.getStrippedLog();
        Block wood = family.getWood();
        Block strippedWood = family.getStrippedWood();
        TagKey<Block> logs = family.getLogsBlockTag();

        this.addIfExist(logs, log, strippedLog, wood, strippedWood);
        this.tag(BlockTags.LOGS).addTag(logs);
        this.addIfExist(Tags.Blocks.STRIPPED_LOGS, strippedLog);
        this.addIfExist(Tags.Blocks.STRIPPED_WOODS, strippedWood);

        this.addIfExist(BlockTags.PLANKS, family);

        this.addIfExist(BlockTags.WOODEN_STAIRS, family.getStairs());
        this.addIfExist(BlockTags.WOODEN_SLABS, family.getSlab());
        this.addIfExist(BlockTags.WOODEN_FENCES, family.getFence());
        this.addIfExist(Tags.Blocks.FENCE_GATES_WOODEN, family.getFenceGate());
        this.addIfExist(BlockTags.WOODEN_BUTTONS, family.getButton());
        this.addIfExist(BlockTags.WOODEN_PRESSURE_PLATES, family.getPressurePlate());
        this.addIfExist(BlockTags.WOODEN_TRAPDOORS, family.getTrapdoor());
        this.addIfExist(BlockTags.WOODEN_DOORS, family.getDoor());

        this.addIfExist(BlockTags.STANDING_SIGNS, family.getStandingSign());
        this.addIfExist(BlockTags.WALL_SIGNS, family.getWallSign());
        this.addIfExist(BlockTags.CEILING_HANGING_SIGNS, family.getCeilingHangingSign());
        this.addIfExist(BlockTags.WALL_HANGING_SIGNS, family.getHangingWallSign());
    }
}
