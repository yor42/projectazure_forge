package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.client.gui.GuiALInventory;
import com.yor42.projectazure.client.gui.guiBAInventory;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryAlloying;
import com.yor42.projectazure.intermod.jei.recipecategory.JEIRecipeCategoryPressing;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class Jei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceUtils.ModResourceLocation("_jei");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(guiBAInventory.class, new IGuiContainerHandler<guiBAInventory>() {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(guiBAInventory containerScreen) {
                List<Rectangle2d> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rectangle2d(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });

        registration.addGuiContainerHandler(GuiALInventory.class, new IGuiContainerHandler<GuiALInventory>() {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(GuiALInventory containerScreen) {
                List<Rectangle2d> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rectangle2d(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
        registration.addRecipes(recipeManager.getRecipesForType(registerRecipes.Types.PRESSING), JEIRecipeCategoryPressing.UID);
        registration.addRecipes(recipeManager.getRecipesForType(registerRecipes.Types.ALLOYING), JEIRecipeCategoryAlloying.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(registerBlocks.METAL_PRESS.get().asItem()), JEIRecipeCategoryPressing.UID);
        registration.addRecipeCatalyst(new ItemStack(registerBlocks.ALLOY_FURNACE.get().asItem()), JEIRecipeCategoryAlloying.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIRecipeCategoryPressing(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JEIRecipeCategoryAlloying(registration.getJeiHelpers().getGuiHelper()));
    }
}
