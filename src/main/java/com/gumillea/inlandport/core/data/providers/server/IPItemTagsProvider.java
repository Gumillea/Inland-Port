package com.gumillea.inlandport.core.data.providers.server;

import com.gumillea.inlandport.common.block.family.wooden.WoodenBaseBlock;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.CompatUtil;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public abstract class IPItemTagsProvider extends ItemTagsProvider {
    protected final String modId;

    public IPItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, CompletableFuture<TagLookup<Block>> lookUp) {
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
            copy(blockTag, itemTag);
        }
    }

    public void addIfExist(TagKey<Item> tagKey, Item... items) {
        for (Item item : items) {
            if (item != null) {
                ResourceLocation key = RegUtil.key(item);
                if (!key.getNamespace().equals(modId) && !key.getNamespace().equals("minecraft")) {
                    tag(tagKey).addOptional(key);
                } else {
                    tag(tagKey).add(item);
                }
            }
        }
    }

    @SafeVarargs
    public final void addIfExist(TagKey<Item> tagKey, TagKey<Item>... tagKeys) {
        for (TagKey<Item> newTagKey : tagKeys) {
            tag(tagKey).addTag(newTagKey);
        }
    }

    public void addWoodenFamily(WoodenBaseBlock family) {
        copy(BlockTags.PLANKS, ItemTags.PLANKS);

        Block log = family.getLog();
        copyIfExist(log, family.getLogsBlockTag(), family.getLogsItemTag());
        if (log != null) {
            if (log.defaultBlockState().ignitedByLava()) {
                copyIfExist(family.getLog(), BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
                copyIfExist(family.getStrippedLog(), BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
                copyIfExist(family.getWood(), BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
                copyIfExist(family.getStrippedLog(), BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
            }
            copyIfExist(log, BlockTags.LOGS, ItemTags.LOGS);
        }

        copyIfExist(family.getStrippedLog(), Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS);
        copyIfExist(family.getStrippedWood(), Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS);

        copyIfExist(family.getSlab(), BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copyIfExist(family.getStairs(), BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);

        copyIfExist(family.getButton(), BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        copyIfExist(family.getPressurePlate(), BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copyIfExist(family.getDoor(), BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copyIfExist(family.getTrapdoor(), BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copyIfExist(family.getFence(), BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copyIfExist(family.getFenceGate(), Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
        addIfExist(ItemTags.SIGNS, family.getSign());
        addIfExist(ItemTags.HANGING_SIGNS, family.getHangingSign());
        addIfExist(ItemTags.BOATS, family.getBoat());
        addIfExist(ItemTags.CHEST_BOATS, family.getChestBoat());
    }

}
