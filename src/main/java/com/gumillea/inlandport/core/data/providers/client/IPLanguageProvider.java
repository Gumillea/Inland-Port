package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.common.effect.SimpleMobEffect;
import com.gumillea.inlandport.common.item.TooltipItem;
import com.gumillea.inlandport.core.data.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.fluids.FluidType;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class IPLanguageProvider extends LanguageProvider {

    private final String modId;
    private final Set<String> keys = new HashSet<>();
    private boolean isAutoGenerating = false;

    public IPLanguageProvider(PackOutput output, String modId) {
        super(output, modId, "en_us");
        this.modId = modId;
    }

    @Override
    public void addTranslations() {
        addManualTranslations();

        this.isAutoGenerating = true;
        AutoDataGeneHelper.autoGeneLangJson(this, modId);
        this.isAutoGenerating = false;
        this.keys.clear();
    }

    public abstract void addManualTranslations();

    public void addAdvancement(String path, String title, String description) {
        this.add("advancements." + modId + path + ".title", title);
        this.add("advancements." + modId + path + ".description", description);
    }

    public void addCreativeTabs(CreativeModeTab... tabs) {
        for (CreativeModeTab tab : tabs) {
            String path = RegUtil.path(tab);
            add("item_group." + path, WordUtils.capitalizeFully(path.replace(modId + "_", modId + ": ").replace("_", " ").replace(".", " ").replace(" and ", " & ")));
        }
    }

    public void addAttributes(Attribute... attributes) {
        for (Attribute attribute : attributes) {
            add(RegUtil.path(attribute), format(RegUtil.key(attribute)).replace("Generic ", ""));
        }
    }

    public void addBlocks(Block... blocks) {
        for (Block block : blocks)
            this.add(block, format(Objects.requireNonNull(RegUtil.key(block))));
    }

    public void addBlock(Block block, String name) {
        this.add(block, name);
    }

    public void addBaskets(Block... blocks) {
        for (Block block : blocks)
            this.add(block, "Basket of " + format(Objects.requireNonNull(RegUtil.key(block))).replace(" Basket", ""));
    }

    public void addSacks(Block... blocks) {
        for (Block block : blocks)
            this.add(block, "Sack of " + format(Objects.requireNonNull(RegUtil.key(block))).replace(" Sack", ""));
    }

    public void addFluidTypes(FluidType... fluidTypes) {
        for (FluidType fluidType : fluidTypes) {
            ResourceLocation location = RegUtil.key(fluidType);
            String key = "fluid_type." + location.getNamespace() + "." + location.getPath();
            this.add(key, format(location));
        }
    }

    public void addFluidType(FluidType fluidType, String name) {
        ResourceLocation location = RegUtil.key(fluidType);
        String key = "fluid_type." + location.getNamespace() + "." + location.getPath();
        this.add(key, name);
    }

    public void addPotions(Potion... potions) {
        for (Potion potion : potions) {
            ResourceLocation modId = RegUtil.key(potion);
            String key = "item.minecraft.potion.effect." + modId.getNamespace() + "." + modId.getPath();
            this.add(key, "Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key2 = "item.minecraft.splash_potion.effect." + modId.getNamespace() + "." + modId.getPath();
            this.add(key2,  "Splash Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key3 = "item.minecraft.lingering_potion.effect." + modId.getNamespace() + "." + modId.getPath();
            this.add(key3,  "Lingering Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key4 = "item.minecraft.tipped_arrow.effect." + modId.getNamespace() + "." + modId.getPath();
            this.add(key4,  "Arrow of " + format(modId).replace("Long ", "").replace("Strong ", ""));
        }
    }

    public void addEffects(MobEffect... effects) {
        for (MobEffect effect : effects) {
            this.add(effect, format(RegUtil.key(effect)));
            if (effect instanceof SimpleMobEffect simpleEffect && simpleEffect.hasDescription()) {
                this.addEffectDescription(simpleEffect, simpleEffect.getDescription());
            }
        }
    }

    public void addEffectDescription(MobEffect effect, String description) {
            String path = effect.getDescriptionId();
            this.add(path + ".description", description);
    }

    public void addEffectWithDescription(MobEffect effect, String description) {
        this.addEffects(effect);
        this.addEffectDescription(effect, description);
    }

    public void addItem(Item... items) {
        for (Item item : items){
            this.add(item, format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace(" With ", " with ").replace(" De ", " de ").replace(" And ", " and "));
            if (item instanceof TooltipItem tooltipItem && tooltipItem.hasTooltip()) {
                this.addTooltip(item, tooltipItem.getTooltip());
            }
        }
    }

    public void addTooltip(String name, String description) {
        this.add("tooltip." + modId + "." + name, description);
    }

    public void addTooltip(Item item, @Nullable String suffix, String description) {
        suffix = suffix != null ? suffix : "";
        this.add("tooltip." + modId + "." + RegUtil.path(item) + suffix, description);
    }

    public void addTooltip(Item item, String description) {
        this.addTooltip(item, null, description);
    }

    public void addItem(Item item, String name) {
        this.add(item, name);
    }

    public void addSlice(Item... items) {
        for (Item item : items)
            this.add(item, "Slice of " + format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace(" Slice", ""));
    }

    public void addChestBoats(Item... items) {
        for (Item item : items)
            this.add(item, format(Objects.requireNonNull(RegUtil.key(item))).replace(" Chest", "") + " with Chest");
    }

    public void addTradeItem(Item... items) {
        for (Item item : items)
            this.add(item, format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace("ers ", "er's "));
    }

    public void addJeiItemDescription(String description, Item... items) {
        for (Item item : items) {
            ResourceLocation location = RegUtil.key(item);
            String key = "jei." + location.getNamespace() + "." + location.getPath() + ".desc";
            this.add(key, description);
        }
    }

    public void addMessage(String path, String message) {
        String key = "message."+ modId + "." + path;
        this.add(key, message);
    }

    public void addJukeboxSong(Holder<Item> item, String title) {
        String key = "jukebox_song."+ modId + "." + RegUtil.path(item.value()).replace("music_disc_", "");
        this.add(key, title);
    }

    public static String format(ResourceLocation location) {
        return WordUtils.capitalizeFully(location.getPath().replace("_", " ").replace(".", " "));
    }

    @Override
    public void add(@NotNull String key, @NotNull String value) {
        if (this.isAutoGenerating) {
            if (this.keys.contains(key)) {
                return;
            }
        } else {
            this.keys.add(key);
        }
        super.add(key, value);
    }

}
