package com.gumillea.inlandport;

import com.gumillea.inlandport.core.util.helpers.reg.BlockHelper;
import com.gumillea.inlandport.core.util.helpers.reg.RegHelper;
import com.gumillea.inlandport.test.data.modifiers.TestLootTableModifier;
import com.gumillea.inlandport.test.data.providers.server.*;
import com.gumillea.inlandport.test.data.providers.client.TestBlockStateProvider;
import com.gumillea.inlandport.test.data.providers.client.TestItemModelProvider;
import com.gumillea.inlandport.test.data.providers.TestRegistrySets;
import com.gumillea.inlandport.test.reg.*;
import com.gumillea.inlandport.core.util.modifiers.IPAttributeModifier;
import com.gumillea.inlandport.test.data.providers.client.TestLanguageProvider;
import com.gumillea.inlandport.test.data.providers.client.TestSoundDefinitionsProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(InlandPort.MODID)
public class InlandPort {
    public static final String MODID = "inland_port";
    public static final Logger LOGGER = LogUtils.getLogger();

    public InlandPort(IEventBus bus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.STARTUP, InlandPortConfig.STARTUP_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, InlandPortConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, InlandPortConfig.CLIENT_SPEC);

        IPAttributes.HELPER.register(bus);
        IPSoundEvents.HELPER.register(bus);
        IPItems.HELPER.register(bus);
        IPConditions.CONDITION_CODECS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::gatherData);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        IPAttributeModifier.addAttributeModifiers();
        BlockHelper.regBlockEntities();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        BlockHelper.regSheets();
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean includeServer = event.includeServer();
        TestRegistrySets sets = new TestRegistrySets(output, provider);
        generator.addProvider(includeServer, sets);
        provider = sets.getRegistryProvider();

        TestBlockTagsProvider blockTagsProvider = new TestBlockTagsProvider(output, provider, helper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new TestItemTagsProvider(output, provider, blockTagsProvider.contentsGetter()));
        generator.addProvider(includeServer, new TestDamageTypeTagsProvider(output, provider, helper));

        generator.addProvider(event.includeServer(), new TestLootTableModifier(output, provider));

        boolean client = event.includeClient();
        generator.addProvider(client, new TestLanguageProvider(output));
        generator.addProvider(client, new TestItemModelProvider(output, helper));
        generator.addProvider(client, new TestBlockStateProvider(output, helper));
        generator.addProvider(client, new TestSoundDefinitionsProvider(output, helper));
    }
}
