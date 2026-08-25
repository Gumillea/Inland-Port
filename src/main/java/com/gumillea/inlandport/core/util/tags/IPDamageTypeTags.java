package com.gumillea.inlandport.core.util.tags;

import com.gumillea.inlandport.InlandPort;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import static com.gumillea.inlandport.core.util.utils.CompatUtil.*;

public class IPDamageTypeTags {
    public static final TagKey<DamageType> BYPASSES_DODGE = damageTypeTag(InlandPort.MODID, "bypasses_dodge");
}


