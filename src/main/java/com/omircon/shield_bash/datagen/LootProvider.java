package com.omircon.shield_bash.datagen;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import com.omircon.shield_bash.glm.AutoSmeltLootModifier;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class LootProvider extends GlobalLootModifierProvider {
    public LootProvider(DataGenerator gen) {
        super(gen, ShieldBash.MODID);
    }

    @Override
    protected void start()
    {/*
        add("smelting_touch_loot",
                Registries.MODIFIER.get(),
                new AutoSmeltLootModifier(
                new ILootCondition[]{
                        MatchTool.toolMatches(
                                ItemPredicate.Builder.item().hasEnchantment(
                                        new EnchantmentPredicate(Registries.SMELTING_TOUCH.get(), MinMaxBounds.IntBound.IntBound.atLeast(1)))
                        ).build()
                }
        ));*/
    }


}
