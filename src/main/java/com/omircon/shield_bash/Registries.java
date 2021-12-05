package com.omircon.shield_bash;

import com.omircon.shield_bash.enchantments.GuardEnchantment;
import com.omircon.shield_bash.enchantments.SmeltingTouchEnchantment;
import com.omircon.shield_bash.enchantments.SpikesEnchantment;
import com.omircon.shield_bash.glm.AutoSmeltLootModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Items;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.omircon.shield_bash.ShieldBash.MODID;

public class Registries {
    public static final EnchantmentType SHIELDS = EnchantmentType.create("shields", (item)->(item == Items.SHIELD));

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

    public static final RegistryObject<GuardEnchantment> GUARD = ENCHANTS.register("guard", () -> new GuardEnchantment());

    //public static final RegistryObject<SmeltingTouchEnchantment> SMELTING_TOUCH = ENCHANTS.register("smelting_touch", () -> new SmeltingTouchEnchantment());

    public static final RegistryObject<SpikesEnchantment> SPIKES = ENCHANTS.register("spikes", () -> new SpikesEnchantment());

    public static final RegistryObject<AutoSmeltLootModifier.Serializer> MODIFIER = GLM.register("smelting_touch_loot", AutoSmeltLootModifier.Serializer::new);
}
