package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerCrystalGrowthChamber;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

public class GuiCrystalGrowthChamber extends AbstractContainerScreen<ContainerCrystalGrowthChamber> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/crystal_growth_chamber.png");

    public GuiCrystalGrowthChamber(ContainerCrystalGrowthChamber screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTanks(matrixStack, partialTicks, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderTanks(PoseStack matrixStack, float partialTicks, int x, int y){
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        FluidStack stack = this.menu.getWaterTank();
        float height = 34*((float)this.menu.getWaterTankAmount()/this.menu.getWaterTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+12,this.topPos+26+(34-height), 16, height);
        buffer.endBatch();
        stack = this.menu.getSolutionTank();
        height = 34*((float)this.menu.getSolutionTankAmount()/this.menu.getSolutionTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+58,this.topPos+26+(34-height), 16, height);
        buffer.endBatch();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        this.blit(matrixStack, this.leftPos + 57, this.topPos + 25, 176, 49, 18, 36);
        this.blit(matrixStack, this.leftPos + 11, this.topPos + 25, 176, 85, 18, 36);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        float renserscale = 0.9F;
        int width = this.font.width(this.title.getString());
        this.titleLabelY = 6;
        this.titleLabelX = 180-width;
        matrixStack.scale(renserscale, renserscale, renserscale);
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX/renserscale, (float)this.titleLabelY/renserscale, 4210752);
        matrixStack.popPose();
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int b = this.menu.getprogressScaled(24);
        this.blit(matrixStack, this.leftPos + 80, this.topPos + 35, 176, 14, b, 17);
    }
}
