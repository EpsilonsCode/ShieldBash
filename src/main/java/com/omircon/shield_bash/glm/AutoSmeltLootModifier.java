package com.omircon.shield_bash.glm;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.ShieldBash;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AutoSmeltLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public AutoSmeltLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        List<ItemStack> removeList = new ArrayList<>();
        List<ItemStack> addList = new ArrayList<>();
        int lvl = 0;//EnchantmentHelper.getItemEnchantmentLevel(Registries.SMELTING_TOUCH.get(), context.getParamOrNull(LootParameters.TOOL).copy());
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if(entity != null && lvl>=1 && generatedLoot != null)
        {
            List<FurnaceRecipe> list = entity.getCommandSenderWorld().getRecipeManager().getAllRecipesFor(IRecipeType.SMELTING);
            for(ItemStack stack : generatedLoot)
            {
                ItemStack item = stack;
                for(FurnaceRecipe recipe : list)
                {
                    Ingredient ingredient = recipe.getIngredients().get(0);
                        if(ingredient.test(stack))
                        {
                            item = recipe.getResultItem();
                            break;
                        }
                }
                for(ResourceLocation tag : stack.getItem().getTags())
                {
                    if(tag.getPath().endsWith("ores") || tag.getPath().endsWith("ore"))
                    {
                        removeList.add(stack); //remove
                        addList.add(item); //add
                        break;
                    }
                }
            }
            for(ItemStack stack : removeList)
            {
                generatedLoot.remove(stack);
            }

            for(ItemStack stack : addList)
            {
                generatedLoot.add(stack);
            }
        }

        return generatedLoot;
    }
    public static class Serializer extends GlobalLootModifierSerializer<AutoSmeltLootModifier>
    {

        @Override
        public AutoSmeltLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new AutoSmeltLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(AutoSmeltLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
