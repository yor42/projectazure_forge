package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.containers.machine.ContainerMetalPress;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class guiMetalPress extends AbstractContainerScreen<ContainerMetalPress> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/metal_press.png");

    private final ContainerMetalPress container;

    public guiMetalPress(ContainerMetalPress container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.container = container;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        if(isHovering(157, 6,10,72,mouseX,mouseY)){
            this.renderTooltip(matrixStack, new TextComponent(this.container.getField().get(2)+"/"+this.container.getField().get(3)), mouseX,mouseY);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int b = this.container.getprogressScaled(41);
        this.blit(matrixStack,this.leftPos+65,this.topPos+30,186,0,b+1,26);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(matrixStack, this.leftPos+157, this.topPos+6, 176, 0, 10, 72-p);

    }

}
