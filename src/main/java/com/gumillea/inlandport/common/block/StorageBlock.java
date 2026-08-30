package com.gumillea.inlandport.common.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.function.Supplier;

public class StorageBlock extends RotatedPillarBlock {
    private final Supplier<Item> item;

    public StorageBlock(Properties properties, Supplier<Item> item) {
        super(properties);
        this.item = item;
    }

    public Item getItem() {
        return item.get();
    }

}
