package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class guiAlloyFurnace extends ContainerScreen<ContainerAlloyFurnace> implements IHasContainer<ContainerAlloyFurnace> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/alloy_furnace.png");

    private final ContainerAlloyFurnace container;

    public guiAlloyFurnace(ContainerAlloyFurnace screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled(13);
            this.blit(matrixStack, this.guiLeft + 56, this.guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled(24);
        this.blit(matrixStack, this.guiLeft + 79, this.guiTop + 34, 176, 14, l + 1, 16);
    }
}
