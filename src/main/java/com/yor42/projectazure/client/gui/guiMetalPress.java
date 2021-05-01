package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.ContainerMetalPress;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class guiMetalPress extends ContainerScreen<ContainerMetalPress> implements IHasContainer<ContainerMetalPress> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/metal_press.png");

    private ContainerMetalPress container;

    public guiMetalPress(ContainerMetalPress container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.container = container;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        if(isPointInRegion(157, 6,10,72,mouseX,mouseY)){
            this.renderTooltip(matrixStack, new StringTextComponent(Integer.toString(this.container.getprogressScaled(100))), mouseX,mouseY);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int b = this.container.getprogressScaled(38);
        this.blit(matrixStack,this.guiLeft+65,this.guiTop+30,186,0,b+1,26);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(matrixStack, this.guiLeft+157, this.guiTop+6, 176, 0, 10, 72-p);

    }

}
