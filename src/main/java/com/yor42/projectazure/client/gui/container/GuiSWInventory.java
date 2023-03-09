package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.entity.ContainerSWInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiSWInventory extends AbstractGUIScreen<ContainerSWInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/sw_inventory.png");

    public GuiSWInventory(ContainerSWInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 225;
        this.imageHeight = 240;

        this.titleLabelX = 115;
        this.titleLabelY = 11;

        this.inventoryLabelX = 33;
        this.inventoryLabelY = 148;
    }

    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
        this.font.draw(pMatrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xffffff);
        this.font.draw(pMatrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        matrixStack.popPose();
    }
}
