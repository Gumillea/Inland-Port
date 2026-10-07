package com.gumillea.inlandport.core.util.tags;

import com.gumillea.inlandport.core.util.IPCompat;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.gumillea.inlandport.core.util.utils.CompatUtil.itemTag;

public class IPItemTags {
    public static final TagKey<Item> KNIVES = itemTag("tools/knife");
    public static final TagKey<Item> MOSS = itemTag("moss");

    public static final TagKey<Item> BEETROOT = cropTag("beetroot");
    public static final TagKey<Item> CARROT = cropTag("carrot");
    public static final TagKey<Item> POTATO = cropTag("potato");
    public static final TagKey<Item> SUGARCANE = cropTag("sugar_cane");
    public static final TagKey<Item> GRAIN = cropTag("grain");

    public static final TagKey<Item> APPLE = foodTag("apple");
    public static final TagKey<Item> SWEET_BERRY = foodTag("sweet_berry");
    public static final TagKey<Item> GLOW_BERRY = foodTag("glow_berry");
    public static final TagKey<Item> NUT = foodTag("nut");
    public static final TagKey<Item> DOUGH = foodTag("dough");
    public static final TagKey<Item> CAKE = foodTag("cake");
    public static final TagKey<Item> PIE_SLICE = foodTag("pie/slice");
    public static final TagKey<Item> CAKE_SLICE = foodTag("cake/slice");
    public static final TagKey<Item> LEAFY_GREEN = foodTag("leafy_green");

    public static final TagKey<Item> COFFEE = drinkTag("coffee");
    public static final TagKey<Item> TEA = drinkTag("tea");
    public static final TagKey<Item> SYRUP = drinkTag("syrup");
    public static final TagKey<Item> JAM = drinkTag("jam");
    public static final TagKey<Item> MILK_BOTTLE = drinkTag("milk/bottle");

    public static final TagKey<Item> SWEETS = itemTag(IPCompat.FD, "sweets");
    public static final TagKey<Item> SNACKS = itemTag(IPCompat.FD, "snacks");
    public static final TagKey<Item> MEALS = itemTag(IPCompat.FD, "meals");

    public static final TagKey<Item> CURIOS_HEAD = curiosTag("head");

    public static TagKey<Item> curiosTag(String name) {
        return itemTag("curios", name);
    }

    public static TagKey<Item> cropTag(String name) {
        return itemTag(null, "crops/" + name);
    }

    public static TagKey<Item> foodTag(String name) {
        return itemTag(null, "foods/" + name);
    }

    public static TagKey<Item> drinkTag(String name) {
        return itemTag(null, "drinks/" + name);
    }

}


