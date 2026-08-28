package com.gumillea.inlandport.core.data.providers;

import com.gumillea.inlandport.core.data.providers.client.IPBlockStateProvider;
import com.gumillea.inlandport.core.data.providers.client.IPItemModelProvider;
import com.gumillea.inlandport.core.data.providers.client.IPLanguageProvider;
import com.gumillea.inlandport.core.data.providers.client.IPSoundDefinitionsProvider;
import com.gumillea.inlandport.core.data.providers.server.IPBlockTagsProvider;
import com.gumillea.inlandport.core.data.providers.server.IPDataMapProvider;
import com.gumillea.inlandport.core.data.providers.server.IPItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class AutoDataGenerator {

    public static void add(GatherDataEvent event, String modId, EnumSet<Providers> providers) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();
        boolean client = event.includeClient();

        if (providers.isEmpty()) return;

        providers.forEach(p -> {
            switch (p) {
                case BLOCK_MODEL -> generator.addProvider(client, new IPBlockStateProvider(output, modId, helper) {});
                case ITEM_MODEL -> generator.addProvider(client, new IPItemModelProvider(output, modId, helper) {});
                case LANGUAGE -> generator.addProvider(client, new IPLanguageProvider(output, modId) {});
                case SOUND_DEFINITION -> generator.addProvider(client, new IPSoundDefinitionsProvider(output, modId, helper) {});

                case BLOCK_AND_ITEM_TAG -> {
                    IPBlockTagsProvider blockTagsProvider = new IPBlockTagsProvider(output, provider, modId, helper) {};
                    generator.addProvider(server, blockTagsProvider);
                    generator.addProvider(server, new IPItemTagsProvider(output, provider, modId, blockTagsProvider.contentsGetter()) {});
                }
                case DATA_MAP -> generator.addProvider(server, new IPDataMapProvider(output, provider, modId) {});
                default -> {}
            }
        });

    }

    public enum Providers {
        BLOCK_MODEL,
        ITEM_MODEL,
        LANGUAGE,
        SOUND_DEFINITION,
        BLOCK_AND_ITEM_TAG,
        DATA_MAP;

        public static EnumSet<Providers> all() {
            return EnumSet.allOf(Providers.class);
        }

        public static EnumSet<Providers> only(Providers... providers) {
            return EnumSet.of(providers[0], providers);
        }

        public static EnumSet<Providers> except(Providers... excludes) {
            EnumSet<Providers> set = EnumSet.allOf(Providers.class);
            for (Providers exclude : excludes) {
                set.remove(exclude);
            }
            return set;
        }
    }
}
