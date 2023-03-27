package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.PressingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryPressing implements IRecipeCategory<PressingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_pressing");
    public static RecipeType<PressingRecipe> RECIPE_TYPE = new RecipeType<>(UID, PressingRecipe.class);

    public JEIRecipeCategoryPressing(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegisterBlocks.METAL_PRESS.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/metal_press.png");
        this.background = guiHelper.createDrawable(TEXTURE, 4,4, 165, 75);

    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TranslatableComponent("recipe.pressing");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public RecipeType<PressingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }
    //method above replaces following 2
    @Override
    @SuppressWarnings("Removal")
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    @SuppressWarnings("Removal")
    public Class<? extends PressingRecipe> getRecipeClass() {
        return PressingRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 36,30).addIngredients(VanillaTypes.ITEM_STACK, recipe.getIngredientStack());
        builder.addSlot(RecipeIngredientRole.CATALYST, 70,30).addIngredients(VanillaTypes.ITEM_STACK, recipe.getMold());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 111,30).addItemStack(recipe.getResultItem());

    }
}
