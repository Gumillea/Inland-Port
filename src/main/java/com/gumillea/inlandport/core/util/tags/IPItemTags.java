package com.gumillea.inlandport.core.util.tags;

import com.gumillea.inlandport.core.util.IPCompat;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.gumillea.inlandport.core.util.utils.CompatUtil.itemTag;

public class IPItemTags {
    public static final TagKey<Item> KNIVES = itemTag("tools/knife");

    public static final TagKey<Item> BEETROOT = itemTag("foods/beetroot");
    public static final TagKey<Item> CARROT = itemTag("foods/carrot");
    public static final TagKey<Item> POTATO = itemTag("foods/potato");
    public static final TagKey<Item> SUGARCANE = itemTag("foods/sugarcane");
    public static final TagKey<Item> APPLE = itemTag("foods/apple");
    public static final TagKey<Item> SWEET_BERRY = itemTag("foods/sweet_berry");
    public static final TagKey<Item> GLOW_BERRY = itemTag("foods/glow_berry");
    public static final TagKey<Item> GRAIN = itemTag("foods/grain");
    public static final TagKey<Item> NUT = itemTag("foods/nut");

    public static final TagKey<Item> SWEETS = itemTag(IPCompat.FD, "sweets");
}


