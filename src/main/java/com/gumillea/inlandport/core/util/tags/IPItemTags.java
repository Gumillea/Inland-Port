package com.gumillea.inlandport.core.util.tags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.gumillea.inlandport.core.util.utils.CompatUtil.itemTag;

public class IPItemTags {
    public static final TagKey<Item> KNIVES = itemTag("tools/knife");

    public static final TagKey<Item> CROPS = itemTag("crops");
    public static final TagKey<Item> BEETROOT = itemTag("crops/beetroot");
    public static final TagKey<Item> CARROT = itemTag("crops/carrot");
    public static final TagKey<Item> POTATO = itemTag("crops/potato");
    public static final TagKey<Item> SUGARCANE = itemTag("crops/sugarcane");

    public static final TagKey<Item> FRUITS = itemTag("fruits");
    public static final TagKey<Item> FRUITS_SOUR = itemTag("fruits/sour");
    public static final TagKey<Item> FRUITS_SWEET = itemTag("fruits/sweet");
    public static final TagKey<Item> FRUITS_BITTER = itemTag("fruits/bitter");
    public static final TagKey<Item> FRUITS_SPICY = itemTag("fruits/spicy");

    public static final TagKey<Item> APPLE = itemTag("fruits/apple");
    public static final TagKey<Item> SWEET_BERRY = itemTag("fruits/sweet_berry");
    public static final TagKey<Item> GLOW_BERRY = itemTag("fruits/glow_berry");

    public static final TagKey<Item> GRAIN = itemTag("grain");
    public static final TagKey<Item> NUTS = itemTag("nuts");

    public static final TagKey<Item> MILK = itemTag("milk");
}


