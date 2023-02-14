package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.gameobject.crafting.recipes.CrystalizingRecipe;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.yor42.projectazure.libs.Constants.CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY;

public class JEIRecipeCategoryCrystalizing implements IRecipeCategory<CrystalizingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable Tank_Overlay;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_crystalizing");

    public JEIRecipeCategoryCrystalizing(IGuiHelper helper){
        this.icon = helper.createDrawableIngredient(new ItemStack(RegisterBlocks.CRYSTAL_GROWTH_CHAMBER.get().asItem()));
        ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/crystal_growth_chamber.png");
        this.background = helper.createDrawable(TEXTURE, 4,3, 168, 80);
        this.Tank_Overlay = helper.createDrawable(TEXTURE, 176, 49, 18, 36);
        //this.Tank_Overlay = helper.createDrawable(TEXTURE, 57, 25, )
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CrystalizingRecipe> getRecipeClass() {
        return CrystalizingRecipe.class;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("recipe.crystalizing");
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
    public void setIngredients(CrystalizingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getIngredientStack());
        ingredients.setInputs(VanillaTypes.FLUID, recipe.getFluid());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrystalizingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 53, 2);
        itemStacks.init(4, false, 111, 31);
        IGuiFluidStackGroup fluid = recipeLayout.getFluidStacks();
        fluid.init(0, true, 53, 22, 18, 36, CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY, true, Tank_Overlay);
        fluid.set(0, recipe.getFluid());

        itemStacks.set(ingredients);
    }
}
