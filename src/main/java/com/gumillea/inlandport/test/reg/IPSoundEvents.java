package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.util.helpers.reg.ItemHelper;
import com.gumillea.inlandport.core.util.helpers.reg.SoundEventHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IPSoundEvents {
    public static final SoundEventHelper HELPER = new SoundEventHelper(InlandPort.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ATTACK_MISS = HELPER.regPlayerEvent("attack.miss");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEBUFF_IMMUNE = HELPER.regPlayerEvent("debuff.immune");

    public static final DeferredHolder<SoundEvent, SoundEvent> FLEKKEFJORD = HELPER.regRecordEvent("flekkefjord");
}
