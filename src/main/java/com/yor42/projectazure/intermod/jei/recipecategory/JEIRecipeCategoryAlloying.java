package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.constants.VanillaTypes.ITEM;

public class JEIRecipeCategoryAlloying implements IRecipeCategory<AlloyingRecipe> {

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
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
    }

    @Override
    public void setIngredients(AlloyingRecipe alloyingRecipe, IIngredients iIngredients) {
        iIngredients.setInputs(ITEM, alloyingRecipe.getIngredientStack());
        iIngredients.setOutput(ITEM, alloyingRecipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, AlloyingRecipe alloyingRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
        itemStacks.init(0, true, 42, 12);
        itemStacks.init(1, true, 60, 12);
        itemStacks.init(3, false, 110, 29);

        itemStacks.set(iIngredients);
    }
}
