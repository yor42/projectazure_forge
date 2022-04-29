package com.yor42.projectazure.intermod.jei.recipecategory;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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

import static com.yor42.projectazure.intermod.jei.RecipeTypes.CRYSTALIZING;
import static mezz.jei.api.constants.VanillaTypes.ITEM_STACK;
import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class JEIRecipeCategoryCrystalizing implements IRecipeCategory<CrystalizingRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable Tank_Overlay;
    public static final ResourceLocation UID = ResourceUtils.ModResourceLocation("jei_crystalizing");

    public JEIRecipeCategoryCrystalizing(IGuiHelper helper){
        this.icon = helper.createDrawableIngredient(ITEM_STACK, new ItemStack(Main.CRYSTAL_GROWTH_CHAMBER.get().asItem()));
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
    public RecipeType<CrystalizingRecipe> getRecipeType() {
        return CRYSTALIZING;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent( "recipe.crystalizing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrystalizingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 53, 2)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(OUTPUT, 111, 31)
                .addItemStack(recipe.getResultItem());

        builder.addSlot(INPUT, 53, 22).setFluidRenderer(5000, true,  18, 36).setOverlay(Tank_Overlay, 0, 0).addIngredients(ForgeTypes.FLUID_STACK, recipe.getFluid());
    }
}
