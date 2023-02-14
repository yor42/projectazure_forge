package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.AlloyingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class JEIRecipeCategoryAlloying implements IRecipeCategory<AlloyingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_alloying");

    public JEIRecipeCategoryAlloying(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegisterBlocks.ALLOY_FURNACE.get().asItem()));

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
    public String getTitle() {
        return "";
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("recipe.alloying");
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
    public void setIngredients(AlloyingRecipe alloyingRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, alloyingRecipe.getIngredientStack());
        iIngredients.setOutput(VanillaTypes.ITEM, alloyingRecipe.getResultItem());
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
