package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerCrystalGrowthChamber;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class GuiCrystalGrowthChamber extends ContainerScreen<ContainerCrystalGrowthChamber> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/crystal_growth_chamber.png");

    public GuiCrystalGrowthChamber(ContainerCrystalGrowthChamber screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTanks(matrixStack, partialTicks, mouseX, mouseY);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderTanks(MatrixStack matrixStack, float partialTicks, int x, int y){
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        FluidStack stack = this.container.getWaterTank();
        float height = 34*((float)this.container.getWaterTankAmount()/this.container.getWaterTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+12,this.guiTop+26+(34-height), 16, height);
        buffer.finish();
        stack = this.container.getSolutionTank();
        height = 34*((float)this.container.getSolutionTankAmount()/this.container.getSolutionTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.guiLeft+58,this.guiTop+26+(34-height), 16, height);
        buffer.finish();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        this.blit(matrixStack, this.guiLeft + 57, this.guiTop + 25, 176, 49, 18, 36);
        this.blit(matrixStack, this.guiLeft + 11, this.guiTop + 25, 176, 85, 18, 36);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        int width = this.font.getStringWidth(this.title.getString());
        this.titleY = 6;
        this.titleX = 166-width;
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int b = this.container.getprogressScaled(24);
        this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 35, 176, 14, b, 17);
    }
}
