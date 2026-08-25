package com.gumillea.inlandport.common.block.family.wooden;

import com.gumillea.inlandport.core.util.utils.CompatUtil;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class LogBlock extends RotatedPillarBlock {

    private final String name;
    private final String modId;
    private final Supplier<? extends Block> strippedLog;

    public LogBlock(Properties properties, @Nullable Supplier<? extends Block> strippedLog, @Nullable String modId, @Nullable String name) {
        super(properties);
        this.strippedLog = strippedLog;
        this.name = name;
        this.modId = modId;
    }

    public LogBlock(Properties properties, @Nullable Supplier<? extends Block> strippedLog) {
        this(properties, strippedLog, null, null);
    }

    public LogBlock(Properties properties, @Nullable String name, @Nullable String modId) {
        this(properties, null, name, modId);
    }

    public LogBlock(Properties properties) {
        this(properties, null, null, null);
    }

    private Block getStrippedLog() {
        if (strippedLog != null) {
            return strippedLog.get();
        } else if (modId != null && name != null) {
            return CompatUtil.block(modId,"stripped_" + name);
        }
        return null;
    }

    public Boolean hasStrippedLog() {
        return getStrippedLog() != null;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility ability, boolean simulate) {
        if (!hasStrippedLog()) return super.getToolModifiedState(state, context, ability, simulate);
        return ItemAbilities.AXE_STRIP.equals(ability) ? getStrippedLog().defaultBlockState().setValue(AXIS, state.getValue(AXIS)) : null;
    }

}
