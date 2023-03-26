package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yor42.projectazure.gameobject.containers.machine.ContainerBasicChemicalReactor;
import com.yor42.projectazure.libs.utils.RenderingUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class GuiBasicChemicalReactor extends AbstractContainerScreen<ContainerBasicChemicalReactor> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/basic_chemical_reactor.png");

    public GuiBasicChemicalReactor(ContainerBasicChemicalReactor p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
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
        FluidStack stack = this.menu.getOutputtank();
        float height = 32*((float)this.menu.getOutputTankAmount()/this.menu.getOutputTankCapacity());
        RenderingUtils.drawRepeatedFluidSpriteGui(buffer, matrixStack, stack, this.leftPos+27,this.topPos+118+(32-height), 12, height);
        buffer.endBatch();

        
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.leftPos + 117, this.topPos + 26, 176, 31, 14, 34);
    }

    @Override
    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int p = this.menu.getStoredPowerScaled(71);
        this.blit(p_230450_1_, this.leftPos+156, this.topPos+7, 176, 65, 10, 72-p);
    }
}
