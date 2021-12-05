package com.omircon.shield_bash.enchantments;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class SpikesEnchantment extends Enchantment {
    public SpikesEnchantment() {
        super(Rarity.VERY_RARE, Registries.SHIELDS, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
