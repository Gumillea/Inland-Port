package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.helpers.reg.CreativeTabHelper;
import com.gumillea.inlandport.core.util.helpers.reg.ItemHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IPCreativeTabs {
    public static final CreativeTabHelper HELPER = new CreativeTabHelper(InlandPort.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = HELPER.regAutoItemTab("item", IPItems.ANSAULT);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCk_TAB = HELPER.regAutoBlockTab("block", IPBlocks.GUMILLEA);

}
