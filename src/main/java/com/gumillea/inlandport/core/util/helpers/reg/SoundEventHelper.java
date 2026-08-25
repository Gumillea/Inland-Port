package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.item.RecordItem;
import com.gumillea.inlandport.core.util.utils.IPUtil;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundEventHelper {
    private final DeferredRegister<SoundEvent> sEReg;
    private final String modId;

    public SoundEventHelper(String modId) {
        this.modId = modId;
        this.sEReg = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, modId);
    }

    public void register(IEventBus bus) {
        sEReg.register(bus);
    }

    public DeferredHolder<SoundEvent, SoundEvent> regEvent(String name) {
        return sEReg.register(name, () -> SoundEvent.createVariableRangeEvent(IPUtil.loc(modId, name)));
    }

    public DeferredHolder<SoundEvent, SoundEvent> regEntityEvent(String name) {
        return regEvent("entity." + name);
    }

    public DeferredHolder<SoundEvent, SoundEvent> regPlayerEvent(String name) {
        return regEntityEvent("player." + name);
    }

    public DeferredHolder<SoundEvent, SoundEvent> regRecordEvent(String name) {
        return regEvent("record." + name);
    }

    /**JukeboxSong*/
    public static ResourceKey<JukeboxSong> createSongKey(String modId , String name){
        return ResourceKey.create(Registries.JUKEBOX_SONG, IPUtil.loc(modId, name));
    }

    public static void regSong(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> song, Holder<SoundEvent> event, int length, int intensity) {
        context.register(song, new JukeboxSong(event, Component.translatable(Util.makeDescriptionId("jukebox_song", song.location())), (float)length, intensity));
    }

    public static void regSong(BootstrapContext<JukeboxSong> context, Holder<Item> item, Holder<SoundEvent> event, int length, int intensity) {
        if (item.value() instanceof RecordItem recordItem) regSong(context, recordItem.getSongKey(), event, length, intensity);
    }
}
