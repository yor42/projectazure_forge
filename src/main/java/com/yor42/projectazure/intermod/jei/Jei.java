package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.client.gui.container.*;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryAlloying;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryBasicChemicalReaction;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryCrystalizing;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryPressing;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.registerRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class Jei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceUtils.ModResourceLocation("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(guiBAInventory.class, new IGuiContainerHandler<guiBAInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull guiBAInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getleftPos();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiALInventory.class, new IGuiContainerHandler<GuiALInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull GuiALInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });


        registration.addGuiContainerHandler(GuiGFLInventory.class, new IGuiContainerHandler<GuiGFLInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull GuiGFLInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getGuiLeft();
                int guiTop = containerScreen.getGuiTop();
                int xSize = containerScreen.getXSize();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiAKNInventory.class, new IGuiContainerHandler<GuiAKNInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull GuiAKNInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getGuiLeft();
                int guiTop = containerScreen.getGuiTop();
                int xSize = containerScreen.getXSize();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiCLSInventory.class, new IGuiContainerHandler<GuiCLSInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull GuiCLSInventory containerScreen) {
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
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(recipeManager.getAllRecipesFor(registerRecipes.Types.PRESSING), JEIRecipeCategoryPressing.UID);
        registration.addRecipes(recipeManager.getAllRecipesFor(registerRecipes.Types.ALLOYING), JEIRecipeCategoryAlloying.UID);
        registration.addRecipes(recipeManager.getAllRecipesFor(registerRecipes.Types.CRYSTALIZING), JEIRecipeCategoryCrystalizing.UID);
        registration.addRecipes(recipeManager.getAllRecipesFor(registerRecipes.Types.BASIC_CHEMICAL_REACTION), JEIRecipeCategoryBasicChemicalReaction.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.METAL_PRESS.get().asItem()), JEIRecipeCategoryPressing.UID);
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.ALLOY_FURNACE.get().asItem()), JEIRecipeCategoryAlloying.UID);
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.CRYSTAL_GROWTH_CHAMBER.get().asItem()), JEIRecipeCategoryCrystalizing.UID);
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.BASIC_CHEMICAL_REACTOR.get().asItem()), JEIRecipeCategoryBasicChemicalReaction.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIRecipeCategoryPressing(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryAlloying(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryCrystalizing(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryBasicChemicalReaction(registration.getJeiHelpers().getGuiHelper()));
    }
}
