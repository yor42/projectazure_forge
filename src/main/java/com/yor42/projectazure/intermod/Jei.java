package com.yor42.projectazure.intermod;

import com.yor42.projectazure.client.gui.guiBAInventory;
import com.yor42.projectazure.client.gui.guiShipInventory;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rectangle2d;
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

        registration.addGuiContainerHandler(guiShipInventory.class, new IGuiContainerHandler<guiShipInventory>() {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(guiShipInventory containerScreen) {
                List<Rectangle2d> rects = new ArrayList<>();
                int guiLeft = containerScreen.getX();
                int guiTop = containerScreen.getY();
                int xSize = containerScreen.getBackgroundWidth();
                rects.add(new Rectangle2d(guiLeft+xSize, guiTop, 43, 90));
                return rects;
            }
        });
    }
}
