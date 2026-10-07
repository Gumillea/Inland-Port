package com.gumillea.inlandport.common.block.family.mineral;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DropExperienceBlock;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OreBlock extends DropExperienceBlock {

    @Nullable
    private final Supplier<Item> mineral;
    private final Type type;

    public OreBlock(IntProvider exp, Properties properties, @Nullable Supplier<Item> mineral, Type type) {
        super(exp, properties);
        this.mineral = mineral;
        this.type = type;
    }

    public Item getMineral() {
        return mineral == null ? this.asBlock().asItem() : mineral.get();
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        METAL,
        GEMS
    }

}
