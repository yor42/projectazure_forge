package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class BaseRecipeCategory<T extends Recipe<?>> implements IRecipeCategory<T> {
    protected IRecipeSlotBuilder initItem(IRecipeLayoutBuilder builder, RecipeIngredientRole role, int x, int y, List<ItemStack> stacks) {
        return builder.addSlot(role, x, y)
                .addItemStacks(stacks);
    }
}
