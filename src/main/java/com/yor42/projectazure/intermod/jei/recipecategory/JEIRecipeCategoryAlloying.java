package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import mekanism.api.recipes.CombinerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import static mezz.jei.api.constants.VanillaTypes.ITEM;
import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class JEIRecipeCategoryAlloying extends BaseRecipeCategory<AlloyingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_alloying");

    public JEIRecipeCategoryAlloying(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(ITEM, new ItemStack(registerBlocks.ALLOY_FURNACE.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/alloy_furnace.png");
        this.background = guiHelper.createDrawable(TEXTURE, 4,4, 168, 75);

    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends AlloyingRecipe> getRecipeClass() {
        return AlloyingRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("recipe.alloying");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 42, 12)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(INPUT, 60, 12)
                .addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(OUTPUT, 110, 29)
                .addItemStack(recipe.getResultItem());
    }
}
