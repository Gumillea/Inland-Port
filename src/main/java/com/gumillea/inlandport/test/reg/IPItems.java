package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.IPCompat;
import com.gumillea.inlandport.core.util.helpers.reg.ItemHelper;
import com.gumillea.inlandport.core.util.tags.IPItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IPItems {
    public static final ItemHelper HELPER = new ItemHelper(InlandPort.MODID);

    public static final DeferredHolder<Item, Item> MUSIC_DISC_FLEKKEFJORD = HELPER.regRecord("music_disc_flekkefjord", new Item.Properties());
    public static final DeferredHolder<Item, Item> ANSAULT = HELPER.regFood("ansault", 3, 0.8F, "I see her everywhere. That sad blue lady.", new Object[]{ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY}, IPCompat.PLACE_HOLDER, IPCompat.FR, IPItemTags.FRUITS_BITTER);
    public static final DeferredHolder<Item, Item> ANSAULT_PIE_SLICE = HELPER.regFood("ansault_pie_slice", 4, 0.8F, null, null, ANSAULT);

}