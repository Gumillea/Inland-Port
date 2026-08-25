package com.gumillea.inlandport.test.reg;

import com.gumillea.inlandport.core.util.helpers.reg.SoundEventHelper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.JukeboxSong;

public class IPJukeboxSongs {

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        SoundEventHelper.regSong(context, IPItems.MUSIC_DISC_FLEKKEFJORD, IPSoundEvents.FLEKKEFJORD, 124, 0);
    }
}
