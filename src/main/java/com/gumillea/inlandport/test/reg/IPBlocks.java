package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.common.block.StickyBlock;
import com.gumillea.inlandport.common.block.StorageBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import com.gumillea.inlandport.core.util.helpers.reg.BlockHelper;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.gumillea.inlandport.core.util.utils.VariantUtil.*;

public class IPBlocks {
    public static final BlockHelper HELPER = new BlockHelper(InlandPort.MODID);

    public static final DeferredHolder<Block, Block> ANSAULT_PIE = HELPER.regPie("ansault_pie", BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), new Item.Properties().food(Foods.APPLE), IPItems.ANSAULT_PIE_SLICE);
    public static final DeferredHolder<Block, Block> ANSAULT_CRATE = HELPER.reg("ansault_crate", () -> new StorageBlock(Properties.GUMILLEA, IPItems.ANSAULT));

    public static final DeferredHolder<Block, Block> SLUG_HUSK = HELPER.reg("slug_husk", () -> new StickyBlock(Properties.GUMILLEA, true));

    public static final DeferredHolder<Block, Block> GUMILLEA = HELPER.regWoodFamily("gumillea", Properties.GUMILLEA);

    public static final DeferredHolder<Block, Block> STROMATOLITE = HELPER.regStoneFamily("stromatolite", BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).sound(SoundType.DEEPSLATE), Variant.except(Prefixes.MOSSY, Prefixes.CRACKED, Suffixes.TILES));

    static class Properties {
        public static final BlockBehaviour.Properties GUMILLEA = BlockHelper.woodProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.BAMBOO_WOOD), MapColor.SNOW, MapColor.COLOR_BLUE, MapColor.COLOR_CYAN);
    }
}
