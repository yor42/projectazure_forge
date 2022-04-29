package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.*;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryAlloying;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryCrystalizing;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryPressing;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
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
            public List<Rect2i> getGuiExtraAreas(guiBAInventory containerScreen) {
                List<Rect2i> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rect2i(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiALInventory.class, new IGuiContainerHandler<GuiALInventory>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(GuiALInventory containerScreen) {
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
            public List<Rect2i> getGuiExtraAreas(GuiGFLInventory containerScreen) {
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
            public List<Rect2i> getGuiExtraAreas(GuiAKNInventory containerScreen) {
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
        registration.addRecipes(Collections.singleton(RecipeTypes.PRESSING), JEIRecipeCategoryPressing.UID);
        registration.addRecipes(Collections.singleton(RecipeTypes.ALLOYING), JEIRecipeCategoryAlloying.UID);
        registration.addRecipes(Collections.singleton(RecipeTypes.CRYSTALIZING), JEIRecipeCategoryCrystalizing.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Main.METAL_PRESS.get().asItem()), JEIRecipeCategoryPressing.UID);
        registration.addRecipeCatalyst(new ItemStack(Main.ALLOY_FURNACE.get().asItem()), JEIRecipeCategoryAlloying.UID);
        registration.addRecipeCatalyst(new ItemStack(Main.CRYSTAL_GROWTH_CHAMBER.get().asItem()), JEIRecipeCategoryCrystalizing.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIRecipeCategoryPressing(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryAlloying(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryCrystalizing(registration.getJeiHelpers().getGuiHelper()));
    }
}
