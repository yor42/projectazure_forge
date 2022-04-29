package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.yor42.projectazure.intermod.jei.RecipeTypes.PRESSING;
import static mezz.jei.api.constants.VanillaTypes.ITEM_STACK;
import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class JEIRecipeCategoryPressing implements IRecipeCategory<PressingRecipe> {

    private IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_pressing");

    public JEIRecipeCategoryPressing(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(ITEM_STACK, new ItemStack(Main.CRYSTAL_GROWTH_CHAMBER.get().asItem()));

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
    public RecipeType<PressingRecipe> getRecipeType() {
        return PRESSING;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("Pressing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 36, 30)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(INPUT, 70, 30)
                .addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(OUTPUT, 111, 30)
                .addItemStack(recipe.getResultItem());
    }
}
