package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryAlloying;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryCrystalizing;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryPressing;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class Jei implements IModPlugin {

    private IRecipeCategory<PressingRecipe> pressingCategory;
    private IRecipeCategory<AlloyingRecipe> alloyingCategory;
    private IRecipeCategory<CrystalizingRecipe> crystalizingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceUtils.ModResourceLocation("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(guiBAInventory.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(guiBAInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rect2i(guiLeft + xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiALInventory.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(GuiALInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rect2i(guiLeft + xSize, guiTop, 43, 90));
                return rects;
            }
        });


        registration.addGuiContainerHandler(GuiGFLInventory.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(GuiGFLInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getGuiLeft();
                int guiTop = containerScreen.getGuiTop();
                int xSize = containerScreen.getXSize();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiAKNInventory.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(GuiAKNInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getGuiLeft();
                int guiTop = containerScreen.getGuiTop();
                int xSize = containerScreen.getXSize();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiCLSInventory.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(GuiCLSInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getGuiLeft();
                int guiTop = containerScreen.getGuiTop();
                int xSize = containerScreen.getXSize();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.PRESSING, getPressingRecipes(this.pressingCategory));
        registration.addRecipes(RecipeTypes.ALLOYING, getAlloyingRecipes(this.alloyingCategory));
        registration.addRecipes(RecipeTypes.CRYSTALIZING, getCrystalizingRecipes(this.crystalizingCategory));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Main.METAL_PRESS.get().asItem()), RecipeTypes.PRESSING);
        registration.addRecipeCatalyst(new ItemStack(Main.ALLOY_FURNACE.get().asItem()), RecipeTypes.ALLOYING);
        registration.addRecipeCatalyst(new ItemStack(Main.CRYSTAL_GROWTH_CHAMBER.get().asItem()), RecipeTypes.CRYSTALIZING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        this.pressingCategory = new JEIRecipeCategoryPressing(registration.getJeiHelpers().getGuiHelper());
        this.alloyingCategory = new JEIRecipeCategoryAlloying(registration.getJeiHelpers().getGuiHelper());
        this.crystalizingCategory = new JEIRecipeCategoryCrystalizing(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(pressingCategory);
        registration.addRecipeCategories(this.alloyingCategory);
        registration.addRecipeCategories(this.crystalizingCategory);
    }

    public List<PressingRecipe> getPressingRecipes(IRecipeCategory<PressingRecipe> Category) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        RecipeManager recipeManager = world.getRecipeManager();
        CategoryRecipeValidator<PressingRecipe> validator = new CategoryRecipeValidator<>(Category, 2);
        return getValidHandledRecipes(recipeManager, PressingRecipe.TYPE.Instance, validator);
    }

    public List<AlloyingRecipe> getAlloyingRecipes(IRecipeCategory<AlloyingRecipe> Category) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        RecipeManager recipeManager = world.getRecipeManager();
        CategoryRecipeValidator<AlloyingRecipe> validator = new CategoryRecipeValidator<>(Category, 2);
        return getValidHandledRecipes(recipeManager, AlloyingRecipe.TYPE.Instance, validator);
    }

    public List<CrystalizingRecipe> getCrystalizingRecipes(IRecipeCategory<CrystalizingRecipe> Category) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        RecipeManager recipeManager = world.getRecipeManager();
        CategoryRecipeValidator<CrystalizingRecipe> validator = new CategoryRecipeValidator<>(Category, 2);
        return getValidHandledRecipes(recipeManager, CrystalizingRecipe.TYPE.Instance, validator);
    }


    private static <C extends Container, T extends Recipe<C>> List<T> getValidHandledRecipes(
            RecipeManager recipeManager,
            RecipeType<T> recipeType,
            CategoryRecipeValidator<T> validator
    ) {
        return recipeManager.getAllRecipesFor(recipeType)
                .stream()
                .filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r))
                .collect(Collectors.toList());
    }
}
