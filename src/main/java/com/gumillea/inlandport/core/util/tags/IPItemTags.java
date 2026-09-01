package com.gumillea.inlandport.core.util.tags;

import com.gumillea.inlandport.core.util.IPCompat;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.gumillea.inlandport.core.util.utils.CompatUtil.itemTag;

public class IPItemTags {
    public static final TagKey<Item> KNIVES = itemTag("tools/knife");

    public static final TagKey<Item> BEETROOT = itemTag("crops/beetroot");
    public static final TagKey<Item> CARROT = itemTag("crops/carrot");
    public static final TagKey<Item> POTATO = itemTag("crops/potato");
    public static final TagKey<Item> SUGARCANE = itemTag("crops/sugar_cane");
    public static final TagKey<Item> GRAIN = itemTag("crops/grain");

    public static final TagKey<Item> APPLE = itemTag("foods/apple");
    public static final TagKey<Item> SWEET_BERRY = itemTag("foods/sweet_berry");
    public static final TagKey<Item> GLOW_BERRY = itemTag("foods/glow_berry");

    public static final TagKey<Item> NUT = itemTag("foods/nut");

    public static final TagKey<Item> SWEETS = itemTag(IPCompat.FD, "sweets");
}


