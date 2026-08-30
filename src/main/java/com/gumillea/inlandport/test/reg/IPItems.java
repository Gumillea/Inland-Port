package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.helpers.reg.ItemHelper;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IPItems {
    public static final ItemHelper HELPER = new ItemHelper(InlandPort.MODID);

    public static final DeferredHolder<Item, Item> MUSIC_DISC_FLEKKEFJORD = HELPER.regRecord("music_disc_flekkefjord", new Item.Properties());
}