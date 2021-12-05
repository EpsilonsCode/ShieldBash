package com.omircon.shield_bash.glm;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
    public AutoSmeltLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        List<ItemStack> removeList = new ArrayList<>();
        List<ItemStack> addList = new ArrayList<>();
        int lvl = 0;//EnchantmentHelper.getItemEnchantmentLevel(Registries.SMELTING_TOUCH.get(), context.getParamOrNull(LootParameters.TOOL).copy());
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if(entity != null && lvl>=1 && generatedLoot != null)
        {
            List<SmeltingRecipe> list = entity.getCommandSenderWorld().getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
            for(ItemStack stack : generatedLoot)
            {
                ItemStack item = stack;
                for(SmeltingRecipe recipe : list)
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
        public AutoSmeltLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new AutoSmeltLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(AutoSmeltLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
