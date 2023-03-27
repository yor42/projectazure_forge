package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.BasicChemicalReactionRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class JEIRecipeCategoryBasicChemicalReaction implements IRecipeCategory<BasicChemicalReactionRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_basic_recipe_processing");
    public static final RecipeType<BasicChemicalReactionRecipe> RECIPE_TYPE = new RecipeType<>(UID, BasicChemicalReactionRecipe.class);
    private final IDrawable Tank_Overlay;
    public JEIRecipeCategoryBasicChemicalReaction(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegisterBlocks.METAL_PRESS.get().asItem()));

        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_chemical_reactor.png");
        this.background = guiHelper.createDrawable(TEXTURE, 36,3, 100, 80);
        this.Tank_Overlay = guiHelper.createDrawable(TEXTURE, 176, 31, 16, 34);

    }

    @Override
    public RecipeType<BasicChemicalReactionRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    //method above replaces following 2

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
    public void setRecipe(IRecipeLayoutBuilder builder, BasicChemicalReactionRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7,31).addIngredients(VanillaTypes.ITEM_STACK, recipe.getIngredientStack());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 53,22).setFluidRenderer(15000, false,14,33).addIngredient(ForgeTypes.FLUID_STACK, recipe.getOutput()).setOverlay(Tank_Overlay,0,0);
    }
}
