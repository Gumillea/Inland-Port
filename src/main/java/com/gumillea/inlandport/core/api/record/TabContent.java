package com.gumillea.inlandport.core.api.record;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public record TabContent(ResourceKey<CreativeModeTab> tabKey, Runnable task) {}