package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.HashSet;
import java.util.Set;

public abstract class IPSoundDefinitionsProvider extends SoundDefinitionsProvider {

    private final String modId;
    private final Set<String> keys = new HashSet<>();
    private boolean isAutoGenerating = false;

    public IPSoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.modId = modId;
    }

    @Override
    public void registerSounds() {
        overrideSounds();

        this.isAutoGenerating = true;
        AutoDataGeneHelper.autoGeneSoundJson(this, modId);
        this.isAutoGenerating = false;
        this.keys.clear();
    }

    public void overrideSounds() {};

    @Override
    public void add(SoundEvent event, SoundDefinition definition) {
        String soundKey = RegUtil.path(event);

        if (this.isAutoGenerating) {
            if (this.keys.contains(soundKey)) {
                return;
            }
        } else {
            this.keys.add(soundKey);
        }

        super.add(event, definition);
    }

    public void addSound(SoundEvent event) {
        this.add(event, SoundDefinition.definition().with(sound(RegUtil.path(event).replace(".", "/"))));
    }
}