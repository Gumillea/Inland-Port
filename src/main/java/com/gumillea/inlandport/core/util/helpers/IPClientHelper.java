package com.gumillea.inlandport.core.util.helpers;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class IPClientHelper {
    public static void setRenderTypes(RenderType type, Block... blocks) {
        for (Block block : blocks) {
            ItemBlockRenderTypes.setRenderLayer(block, type);
        }
    }

    public static void setCutout(Block... blocks) {
        setRenderTypes(RenderType.cutout(), blocks);
    }

    public static void setTranslucent(Block... blocks) {
        setRenderTypes(RenderType.translucent(), blocks);
    }
}
