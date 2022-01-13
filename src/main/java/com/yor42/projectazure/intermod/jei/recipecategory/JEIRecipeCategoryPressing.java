package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
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

public class JEIRecipeCategoryPressing implements IRecipeCategory<PressingRecipe> {

    private IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_pressing");

    public JEIRecipeCategoryPressing(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(registerBlocks.METAL_PRESS.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/metal_press.png");
        this.background = guiHelper.createDrawable(TEXTURE, 4,4, 165, 75);

    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends PressingRecipe> getRecipeClass() {
        return PressingRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Pressing";
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("recipe.pressing");
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
    public void setIngredients(PressingRecipe pressingRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(pressingRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, pressingRecipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, PressingRecipe pressingRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        itemStacks.init(0, true, 36, 30);
        itemStacks.init(1, true, 70, 30);
        itemStacks.init(2, false, 111, 30);

        itemStacks.set(iIngredients);
    }
}
