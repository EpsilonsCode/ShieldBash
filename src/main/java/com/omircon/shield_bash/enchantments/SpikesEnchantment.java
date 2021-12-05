package com.omircon.shield_bash.enchantments;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class SpikesEnchantment extends Enchantment {
    public SpikesEnchantment() {
        super(Rarity.VERY_RARE, Registries.SHIELDS, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
