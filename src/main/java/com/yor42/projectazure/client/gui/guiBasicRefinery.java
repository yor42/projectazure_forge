package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicRefinery;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
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
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTanks(matrixStack, partialTicks, mouseX, mouseY);
    }

    protected void renderTanks(MatrixStack matrixStack, float partialTicks, int x, int y){
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        FluidStack stack = this.container.crudeoilstack;
        float height = 32*((float)this.container.getCrudeOilTankAmount()/this.container.getCrudeOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+10,this.guiTop+26+(32-height), 12, height);

        stack = this.container.gasolinestack;
        height = 32*((float)this.container.getGasolineTankAmount()/this.container.getGasolineTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+69,this.guiTop+26+(32-height), 12, height);

        stack = this.container.dieselstack;
        height = 32*((float)this.container.getDieselTankAmount()/this.container.getDieselTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+92,this.guiTop+26+(32-height), 12, height);

        stack = this.container.fueloilstack;
        height = 32*((float)this.container.getFuelOilTankAmount()/this.container.getFuelOilTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+115,this.guiTop+26+(32-height), 12, height);
        buffer.finish();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

    }
}
