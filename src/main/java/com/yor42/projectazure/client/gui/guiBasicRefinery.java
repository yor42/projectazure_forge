package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class guiBasicRefinery extends AbstractContainerScreen<ContainerBasicRefinery> {
    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_refinery.png");
    public guiBasicRefinery(ContainerBasicRefinery screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTanks(PoseStack, partialTicks, mouseX, mouseY);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int p_230451_2_, int p_230451_3_) {
    }

    protected void renderTanks(PoseStack PoseStack, float partialTicks, int x, int y){
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        FluidStack stack = this.menu.crudeoilstack;
        float height = 32*((float)this.menu.getCrudeOilTankAmount()/this.menu.getCrudeOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+10,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.gasolinestack;
        height = 32*((float)this.menu.getGasolineTankAmount()/this.menu.getGasolineTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+69,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.dieselstack;
        height = 32*((float)this.menu.getDieselTankAmount()/this.menu.getDieselTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+92,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.fueloilstack;
        height = 32*((float)this.menu.getFuelOilTankAmount()/this.menu.getFuelOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, PoseStack, stack, this.leftPos+115,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(PoseStack, this.leftPos + 9, this.topPos + 26, 176, 31, 14, 34);
        this.blit(PoseStack, this.leftPos + 68, this.topPos + 26, 176, 31, 14, 34);
        this.blit(PoseStack, this.leftPos + 91, this.topPos + 26, 176, 31, 14, 34);
        this.blit(PoseStack, this.leftPos + 114, this.topPos + 26, 176, 31, 14, 34);
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(PoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isBurning()) {
            int burnScale = (int) this.menu.getBurnLeftScaled();
            this.blit(PoseStack, this.leftPos + 39, this.topPos + 46 + 12 - burnScale, 176, 12 - burnScale, 14, burnScale + 1);
        }
        if(this.menu.isActive()){
            this.blit(PoseStack, this.leftPos + 34, this.topPos + 27, 176, 14, 24, 17);
        }
    }
}
