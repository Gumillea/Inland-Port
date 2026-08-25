package com.gumillea.inlandport.test.data.providers.client;

import com.gumillea.inlandport.InlandPort;
import com.gumillea.inlandport.core.data.providers.client.IPLanguageProvider;
import com.gumillea.inlandport.test.reg.IPItems;
import net.minecraft.data.PackOutput;

public class TestLanguageProvider extends IPLanguageProvider {

    public TestLanguageProvider(PackOutput output) {
        super(output, InlandPort.MODID);
    }

    @Override
    public void addManualTranslations() {
        this.addJukeboxSong(IPItems.MUSIC_DISC_FLEKKEFJORD, "Blear Moon - Flekkefjord");
        this.addTooltip("disabled", "DISABLED");
        this.addTooltip("items_disabled", "Items Disabled: ");
        this.addTooltip("mods_required", "Mods Required: ");
        this.addTooltip("tags_missing", "Tags Missing: ");
        this.addTooltip("placeable", "Placeable");
        this.addTooltip("placeable_while_sneaking", "Placeable while sneaking");
    }

}
