package com.gumillea.inlandport.common.item;

import com.gumillea.inlandport.core.util.helpers.reg.SoundEventHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;

public class RecordItem extends Item {

    private final ResourceKey<JukeboxSong> songKey;

    public RecordItem(Properties properties, Rarity rarity, String modId, String song) {
        super(properties.stacksTo(1).rarity(rarity).jukeboxPlayable(SoundEventHelper.createSongKey(modId, song)));
        this.songKey = SoundEventHelper.createSongKey(modId, song);
    }

    public ResourceKey<JukeboxSong> getSongKey() {
        return songKey;
    }
}