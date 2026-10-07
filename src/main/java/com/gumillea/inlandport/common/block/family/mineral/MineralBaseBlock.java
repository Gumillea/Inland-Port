package com.gumillea.inlandport.common.block.family.mineral;

import com.gumillea.inlandport.common.block.family.SimpleBaseBlock;
import com.gumillea.inlandport.common.block.family.Variant;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MineralBaseBlock extends OreBlock {

    public MineralBaseBlock(IntProvider exp, Properties properties, @Nullable Supplier<Item> mineral, Type type) {
        super(exp, properties, mineral, type);
    }

}
