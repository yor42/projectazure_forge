package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yor42.projectazure.gameobject.containers.machine.ContainerCrystalGrowthChamber;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class GuiCrystalGrowthChamber extends AbstractContainerScreen<ContainerCrystalGrowthChamber> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/crystal_growth_chamber.png");

    public GuiCrystalGrowthChamber(ContainerCrystalGrowthChamber screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTanks(PoseStack, partialTicks, mouseX, mouseY);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    protected void renderTanks(PoseStack PoseStack, float partialTicks, int x, int y){
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        FluidStack stack = this.menu.getWaterTank();
        float height = 34*((float)this.menu.getWaterTankAmount()/this.menu.getWaterTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+12,this.topPos+26+(34-height), 16, height);
        buffer.endBatch();
        stack = this.menu.getSolutionTank();
        height = 34*((float)this.menu.getSolutionTankAmount()/this.menu.getSolutionTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+58,this.topPos+26+(34-height), 16, height);
        buffer.endBatch();

        RenderSystem.setShaderTexture(0, TEXTURE);

        this.blit(PoseStack, this.leftPos + 57, this.topPos + 25, 176, 49, 18, 36);
        this.blit(PoseStack, this.leftPos + 11, this.topPos + 25, 176, 85, 18, 36);
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int x, int y) {
        PoseStack.pushPose();
        float renserscale = 0.9F;
        int width = this.font.width(this.title.getString());
        this.titleLabelY = 6;
        this.titleLabelX = 180-width;
        PoseStack.scale(renserscale, renserscale, renserscale);
        this.font.draw(PoseStack, this.title, (float)this.titleLabelX/renserscale, (float)this.titleLabelY/renserscale, 4210752);
        PoseStack.popPose();
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int b = this.menu.getprogressScaled(24);
        this.blit(PoseStack, this.leftPos + 80, this.topPos + 35, 176, 14, b, 17);
    }
}
