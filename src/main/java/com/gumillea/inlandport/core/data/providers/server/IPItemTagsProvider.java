package com.gumillea.inlandport.core.data.providers.server;

import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public abstract class IPItemTagsProvider extends ItemTagsProvider {
    private final String modId;

    public IPItemTagsProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> lookUp) {
        super(output, provider, lookUp);
        this.modId = modId;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        AutoDataGeneHelper.autoGeneItemTags(this, modId);

        addManualTags();
    }

    public void addManualTags() {}

    public IntrinsicTagAppender<Item> tag(TagKey<Item> tagKey) {
        return super.tag(tagKey);
    }

    public void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
        super.copy(blockTag, itemTag);
    }

    public void copyIfExist(Block block, TagKey<Block> blockTag, TagKey<Item> itemTag) {
        if (block != null) {
            this.copy(blockTag, itemTag);
        }
    }

    public void addIfExist(TagKey<Item> tagKey, Item... items) {
        for (Item item : items) {
            if (item != null) {
                this.tag(tagKey).add(item);
            }
        }
    }

    @SafeVarargs
    public final void addIfExist(TagKey<Item> tagKey, TagKey<Item>... tagKeys) {
        for (TagKey<Item> newTagKey : tagKeys) {
            if (!CompatUtil.isItemTagEmpty(tagKey)) {
                this.tag(tagKey).addTag(newTagKey);
            }
        }
    }

    public void addWoodenFamily(WoodenBaseBlock family) {
        Block log = family.getLog();
        this.copy(BlockTags.PLANKS, ItemTags.PLANKS);

        this.copyIfExist(log, family.getLogsBlockTag(), family.getLogsItemTag());

        if (log.defaultBlockState().is(BlockTags.LOGS_THAT_BURN)) {
            this.copyIfExist(log, BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        } else {
            this.copyIfExist(log, BlockTags.LOGS, ItemTags.LOGS);
        }

        this.copyIfExist(family.getStrippedLog(), Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS);
        this.copyIfExist(family.getStrippedWood(), Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS);

        this.copyIfExist(family.getSlab(), BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        this.copyIfExist(family.getStairs(), BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);

        this.copyIfExist(family.getButton(), BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        this.copyIfExist(family.getPressurePlate(), BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        this.copyIfExist(family.getDoor(), BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        this.copyIfExist(family.getTrapdoor(), BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        this.copyIfExist(family.getFence(), BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.copyIfExist(family.getFenceGate(), Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
        this.addIfExist(ItemTags.SIGNS, family.getSign());
        this.addIfExist(ItemTags.HANGING_SIGNS, family.getHangingSign());
        this.addIfExist(ItemTags.BOATS, family.getBoat());
        this.addIfExist(ItemTags.CHEST_BOATS, family.getChestBoat());
    }

}
