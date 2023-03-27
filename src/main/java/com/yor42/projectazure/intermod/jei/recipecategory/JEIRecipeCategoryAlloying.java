package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.AlloyingRecipe;
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

public class JEIRecipeCategoryAlloying implements IRecipeCategory<AlloyingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_alloying");
    public static final RecipeType<AlloyingRecipe> RECIPE_TYPE = new RecipeType<>(UID, AlloyingRecipe.class);

    public JEIRecipeCategoryAlloying(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegisterBlocks.ALLOY_FURNACE.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/alloy_furnace.png");
        this.background = guiHelper.createDrawable(TEXTURE, 4,4, 168, 75);

    }


    @Override
    public RecipeType<AlloyingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    //method above replaces following 2
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
        builder.addSlot(RecipeIngredientRole.INPUT, 42,12).addIngredients(VanillaTypes.ITEM_STACK, recipe.getIngredientStack().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 60,12).addIngredients(VanillaTypes.ITEM_STACK, recipe.getIngredientStack().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110,29).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }
}
