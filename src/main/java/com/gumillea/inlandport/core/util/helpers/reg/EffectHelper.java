package com.gumillea.inlandport.core.util.helpers.reg;

import com.gumillea.inlandport.common.effect.SimpleMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;

public class EffectHelper {

    private final DeferredRegister<MobEffect> effectReg;
    private final DeferredRegister<Potion> potionReg;
    private final String modId;

    public EffectHelper(String modId) {
        this.modId = modId;
        this.effectReg = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, modId);
        this.potionReg = DeferredRegister.create(BuiltInRegistries.POTION, modId);
    }

    public void register(IEventBus bus) {
        effectReg.register(bus);
        potionReg.register(bus);
    }

    public DeferredHolder<MobEffect, MobEffect> reg(String name, Supplier<MobEffect> supplier) {
        return effectReg.register(name, supplier);
    }

    public DeferredHolder<MobEffect, MobEffect> reg(String name, MobEffectCategory category, int color, String description) {
        return reg(name, () -> new SimpleMobEffect(category, color, description));
    }

    public DeferredHolder<MobEffect, MobEffect> reg(String name, MobEffectCategory category, int color) {
        return reg(name, category, color, null);
    }

    public DeferredHolder<MobEffect, MobEffect> regBeneficial(String name, int color, String description) {
        return reg(name, MobEffectCategory.BENEFICIAL, color, description);
    }

    public DeferredHolder<MobEffect, MobEffect> regBeneficial(String name, int color) {
        return regBeneficial(name, color, null);
    }

    public DeferredHolder<MobEffect, MobEffect> regNeutral(String name, int color, String description) {
        return reg(name, MobEffectCategory.NEUTRAL, color, description);
    }

    public DeferredHolder<MobEffect, MobEffect> regNeutral(String name, int color) {
        return regNeutral(name, color, null);
    }

    public DeferredHolder<MobEffect, MobEffect> regHarmful(String name, int color, String description) {
        return reg(name, MobEffectCategory.HARMFUL, color, description);
    }

    public DeferredHolder<MobEffect, MobEffect> regHarmful(String name, int color) {
        return regHarmful(name, color, null);
    }

    /**Potion*/
    public static float LONG() {
        return 8F / 3F;
    }

    public static float STRONG() {
        return 1F / 2F;
    }

    public DeferredHolder<Potion, Potion> regPotion(String name, MobEffectInstance... insts) {
        return potionReg.register(name, () -> new Potion(modId + "." + name, insts));
    }

    public DeferredHolder<Potion, Potion> regLongPotion(String name, MobEffectInstance... insts) {
        return regPotion("long_" + name, insts);
    }

    public DeferredHolder<Potion, Potion> regStrongPotion(String name, MobEffectInstance... insts) {
        return regPotion("strong_" + name, insts);
    }

    private static MobEffectInstance[] longInst(MobEffectInstance[] original, float durationMultiplier) {
        return Arrays.stream(original).map(inst -> new MobEffectInstance(inst.getEffect(), (int) (inst.getDuration() * durationMultiplier), inst.getAmplifier(), inst.isAmbient(), inst.isVisible(), inst.showIcon())).toArray(MobEffectInstance[]::new);
    }

    private static MobEffectInstance[] strongInst(MobEffectInstance[] original, int extraAmplifier, float durationMultiplier) {
        return Arrays.stream(original).map(inst -> new MobEffectInstance(inst.getEffect(), (int) (inst.getDuration() * durationMultiplier), inst.getAmplifier() + extraAmplifier, inst.isAmbient(), inst.isVisible(), inst.showIcon())).toArray(MobEffectInstance[]::new);
    }

    public DeferredHolder<Potion, Potion> regPotionsWithLong(String name, MobEffectInstance... insts) {
        regLongPotion(name, longInst(insts, LONG()));
        return regPotion(name, insts);
    }

    public DeferredHolder<Potion, Potion> regPotionsWithStrong(String name, MobEffectInstance... insts) {
        regStrongPotion(name, strongInst(insts, 1, STRONG()));
        return regPotion(name, insts);
    }

    public DeferredHolder<Potion, Potion> regPotionsWithLongAndStrong(String name, MobEffectInstance... insts) {
        regLongPotion(name, longInst(insts, LONG()));
        regStrongPotion(name, strongInst(insts, 1, STRONG()));
        return regPotion(name, insts);
    }

    public static void addStandardPotionRecipes(PotionBrewing.Builder builder, Holder<Potion> potion, Item ingredient, Holder<Potion> result) {
        Registry<Potion> registry = BuiltInRegistries.POTION;
        ResourceLocation key = registry.getKey(result.value());

        builder.addMix(potion, ingredient, result);

        ResourceLocation longKey = key.withPrefix("long_");
        Potion longPotion = registry.get(longKey);
        if (longPotion != null) builder.addMix(result, Items.REDSTONE, registry.wrapAsHolder(longPotion));

        ResourceLocation strongKey = key.withPrefix("strong_");
        Potion strongPotion = registry.get(strongKey);
        if (strongPotion != null) builder.addMix(result, Items.GLOWSTONE_DUST, registry.wrapAsHolder(strongPotion));
    }
}
