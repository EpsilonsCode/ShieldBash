package com.omircon.shield_bash.datagen;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import com.omircon.shield_bash.glm.AutoSmeltLootModifier;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class LootProvider extends GlobalLootModifierProvider {
    public LootProvider(DataGenerator gen) {
        super(gen, ShieldBash.MODID);
    }

    @Override
    protected void start()
    {
        /*
        add("smelting_touch_loot",
                Registries.MODIFIER.get(),
                new AutoSmeltLootModifier(
                new LootItemCondition[]{
                        MatchTool.toolMatches(
                                ItemPredicate.Builder.item().hasEnchantment(
                                        new EnchantmentPredicate(Registries.SMELTING_TOUCH.get(), MinMaxBounds.Ints.Ints.atLeast(1)))
                        ).build()
                }
        ));

         */
    }




}
