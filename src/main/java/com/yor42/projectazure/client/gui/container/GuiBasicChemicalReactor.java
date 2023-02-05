package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicChemicalReactor;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class GuiBasicChemicalReactor extends ContainerScreen<ContainerBasicChemicalReactor> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_chemical_reactor.png");

    public GuiBasicChemicalReactor(ContainerBasicChemicalReactor p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTanks(matrixStack, partialTicks, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderTanks(MatrixStack matrixStack, float partialTicks, int x, int y){
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        FluidStack stack = this.menu.getOutputtank();
        float height = 32*((float)this.menu.getOutputTankAmount()/this.menu.getOutputTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+27,this.topPos+118+(32-height), 12, height);
        buffer.endBatch();

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos + 117, this.topPos + 26, 176, 31, 14, 34);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int p = this.menu.getStoredPowerScaled(71);
        this.blit(p_230450_1_, this.leftPos+156, this.topPos+7, 176, 65, 10, 72-p);
    }
}
