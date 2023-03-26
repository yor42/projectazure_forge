package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.BasicChemicalReactionRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryBasicChemicalReaction implements IRecipeCategory<BasicChemicalReactionRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_basic_recipe_processing");
    private final IDrawable Tank_Overlay;
    public JEIRecipeCategoryBasicChemicalReaction(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegisterBlocks.METAL_PRESS.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_chemical_reactor.png");
        this.background = guiHelper.createDrawable(TEXTURE, 36,3, 100, 80);
        this.Tank_Overlay = guiHelper.createDrawable(TEXTURE, 176, 31, 16, 34);

    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends BasicChemicalReactionRecipe> getRecipeClass() {
        return BasicChemicalReactionRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {return "";}

    @Nonnull
    @Override
    public Component getTitleAsTextComponent() {
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
    public void setIngredients(BasicChemicalReactionRecipe pressingRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(pressingRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.FLUID, pressingRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BasicChemicalReactionRecipe recipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
        itemStacks.init(0, true, 7, 31);
        itemStacks.set(iIngredients);
        IGuiFluidStackGroup fluidstack = iRecipeLayout.getFluidStacks();
        fluidstack.init(1,false, 81,23, 14, 33, 15000, false, this.Tank_Overlay);
        fluidstack.set(1, recipe.getOutput());
    }
}
