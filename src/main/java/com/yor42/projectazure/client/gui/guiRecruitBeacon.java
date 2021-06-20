package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerMetalPress;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class guiRecruitBeacon extends ContainerScreen<ContainerRecruitBeacon> implements IHasContainer<ContainerRecruitBeacon> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/recruit_beacon.png");

    private final ContainerRecruitBeacon container;


    public guiRecruitBeacon(ContainerRecruitBeacon screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        if(isPointInRegion(157, 6,10,72,mouseX,mouseY)){
            this.renderTooltip(matrixStack, new StringTextComponent(this.container.getField().get(2)+"/"+this.container.getField().get(3)), mouseX,mouseY);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(matrixStack, this.guiLeft+157, this.guiTop+6, 176, 0, 10, 72-p);
    }
}
