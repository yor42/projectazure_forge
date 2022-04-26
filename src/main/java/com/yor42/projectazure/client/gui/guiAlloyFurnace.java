package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class guiAlloyFurnace extends AbstractContainerScreen<ContainerAlloyFurnace> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/alloy_furnace.png");

    private final ContainerAlloyFurnace container;

    public guiAlloyFurnace(ContainerAlloyFurnace screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled(13);
            this.blit(matrixStack, this.leftPos + 56, this.topPos + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled(24);
        this.blit(matrixStack, this.leftPos + 79, this.topPos + 34, 176, 14, l + 1, 16);
    }
}
