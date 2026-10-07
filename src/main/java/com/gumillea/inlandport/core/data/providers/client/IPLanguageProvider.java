package com.gumillea.inlandport.core.data.providers.client;

import com.gumillea.inlandport.common.effect.SimpleMobEffect;
import com.gumillea.inlandport.common.item.TooltipItem;
import com.gumillea.inlandport.core.util.helpers.AutoDataGeneHelper;
import com.gumillea.inlandport.core.util.utils.RegUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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

    protected final String modId;
    protected final Set<String> keys = new HashSet<>();
    protected boolean isAutoGenerating = false;

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

    public void addManualTranslations() {}

    public void addAdvancement(String path, String title, String description) {
        add("advancements." + modId + path + ".title", title);
        add("advancements." + modId + path + ".description", description);
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
            add(block, format(Objects.requireNonNull(RegUtil.key(block))));
    }

    public void addBlock(Block block, String name) {
        add(block, name);
    }

    public void addBaskets(Block... blocks) {
        for (Block block : blocks)
            add(block, "Basket of " + format(Objects.requireNonNull(RegUtil.key(block))).replace(" Basket", ""));
    }

    public void addSacks(Block... blocks) {
        for (Block block : blocks)
            add(block, "Sack of " + format(Objects.requireNonNull(RegUtil.key(block))).replace(" Sack", ""));
    }

    public void addFluidTypes(FluidType... fluidTypes) {
        for (FluidType fluidType : fluidTypes) {
            ResourceLocation location = RegUtil.key(fluidType);
            String key = "fluid_type." + location.getNamespace() + "." + location.getPath();
            add(key, format(location).replace(" Fluid", ""));
        }
    }

    public void addFluidType(FluidType fluidType, String name) {
        ResourceLocation location = RegUtil.key(fluidType);
        String key = "fluid_type." + location.getNamespace() + "." + location.getPath();
        add(key, name);
    }

    public void addPotions(Potion... potions) {
        for (Potion potion : potions) {
            ResourceLocation modId = RegUtil.key(potion);
            String key = "item.minecraft.potion.effect." + modId.getNamespace() + "." + modId.getPath();
            add(key, "Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key2 = "item.minecraft.splash_potion.effect." + modId.getNamespace() + "." + modId.getPath();
            add(key2,  "Splash Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key3 = "item.minecraft.lingering_potion.effect." + modId.getNamespace() + "." + modId.getPath();
            add(key3,  "Lingering Potion of " + format(modId).replace("Long ", "").replace("Strong ", ""));
            String key4 = "item.minecraft.tipped_arrow.effect." + modId.getNamespace() + "." + modId.getPath();
            add(key4,  "Arrow of " + format(modId).replace("Long ", "").replace("Strong ", ""));
        }
    }

    public void addEffects(MobEffect... effects) {
        for (MobEffect effect : effects) {
            add(effect, format(RegUtil.key(effect)));
            if (effect instanceof SimpleMobEffect simpleEffect && simpleEffect.hasDescription()) {
                addEffectDescription(simpleEffect, simpleEffect.getDescription());
            }
        }
    }

    public void addEffectDescription(MobEffect effect, String description) {
            String path = effect.getDescriptionId();
            add(path + ".description", description);
    }

    public void addEffectWithDescription(MobEffect effect, String description) {
        addEffects(effect);
        addEffectDescription(effect, description);
    }

    public void addItem(Item... items) {
        for (Item item : items){
            add(item, format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace(" With ", " with ").replace(" De ", " de ").replace(" And ", " and "));
            if (item instanceof TooltipItem tooltipItem && tooltipItem.hasTooltip()) {
                String tooltip = tooltipItem.getTooltip();
                if (!tooltip.startsWith("tooltip.")) addTooltip(item, tooltip);
            }
        }
    }

    public void skipItem(Item... items) {
        for (Item item : items){
            keys.add(item.getDescriptionId());
        }
    }

    public void addTooltip(String name, String description) {
        add("tooltip." + modId + "." + name, description);
    }

    public void addTooltip(Item item, @Nullable String suffix, String description) {
        suffix = suffix != null ? suffix : "";
        add("tooltip." + modId + "." + RegUtil.path(item) + suffix, description);
    }

    public void addTooltip(Item item, String description) {
        addTooltip(item, null, description);
    }

    public void addItem(Item item, String name) {
        add(item, name);
    }

    public void addSlice(Item... items) {
        for (Item item : items)
            add(item, "Slice of " + format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace(" Slice", ""));
    }

    public void addChestBoats(Item... items) {
        for (Item item : items)
            add(item, format(Objects.requireNonNull(RegUtil.key(item))).replace(" Chest", "") + " with Chest");
    }

    public void addTradeItem(Item... items) {
        for (Item item : items)
            add(item, format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))).replace("ers ", "er's "));
    }

    public void addJeiItemDescription(String description, Item... items) {
        for (Item item : items) {
            ResourceLocation location = RegUtil.key(item);
            String key = "jei." + location.getNamespace() + "." + location.getPath() + ".desc";
            add(key, description);
        }
    }

    public void addMessage(String path, String message) {
        String key = "message."+ modId + "." + path;
        add(key, message);
    }

    public void addJukeboxSong(Holder<Item> item, String title) {
        String key = "jukebox_song."+ modId + "." + RegUtil.path(item.value()).replace("music_disc_", "");
        add(key, title);
    }

    public void addItemTag(TagKey<Item> tagKey, String name) {
        add("tag.item." + tagKey.location().toLanguageKey().replace("/", "."), name);
    }

    public void addBlockTag(TagKey<Block> tagKey, String name) {
        add("tag.block." + tagKey.location().toLanguageKey().replace("/", "."), name);
    }

    public static String format(ResourceLocation location) {
        return WordUtils.capitalizeFully(location.getPath().replace("_", " ").replace(".", " "));
    }

    @Override
    public void add(@NotNull String key, @NotNull String value) {
        if (isAutoGenerating) {
            if (keys.contains(key)) {
                return;
            }
        } else {
            keys.add(key);
        }
        super.add(key, value);
    }

}
