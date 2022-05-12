package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class guiBasicRefinery extends ContainerScreen<ContainerBasicRefinery> {
    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_refinery.png");
    public guiBasicRefinery(ContainerBasicRefinery screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTanks(matrixStack, partialTicks, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {
    }

    protected void renderTanks(MatrixStack matrixStack, float partialTicks, int x, int y){
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        FluidStack stack = this.menu.crudeoilstack;
        float height = 32*((float)this.menu.getCrudeOilTankAmount()/this.menu.getCrudeOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+10,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.gasolinestack;
        height = 32*((float)this.menu.getGasolineTankAmount()/this.menu.getGasolineTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+69,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.dieselstack;
        height = 32*((float)this.menu.getDieselTankAmount()/this.menu.getDieselTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+92,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();
        stack = this.menu.fueloilstack;
        height = 32*((float)this.menu.getFuelOilTankAmount()/this.menu.getFuelOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+115,this.topPos+27+(32-height), 12, height);
        buffer.endBatch();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos + 9, this.topPos + 26, 176, 31, 14, 34);
        this.blit(matrixStack, this.leftPos + 68, this.topPos + 26, 176, 31, 14, 34);
        this.blit(matrixStack, this.leftPos + 91, this.topPos + 26, 176, 31, 14, 34);
        this.blit(matrixStack, this.leftPos + 114, this.topPos + 26, 176, 31, 14, 34);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isBurning()) {
            int burnScale = (int) this.menu.getBurnLeftScaled();
            this.blit(matrixStack, this.leftPos + 39, this.topPos + 46 + 12 - burnScale, 176, 12 - burnScale, 14, burnScale + 1);
        }
        if(this.menu.isActive()){
            this.blit(matrixStack, this.leftPos + 34, this.topPos + 27, 176, 14, 24, 17);
        }
    }
}
