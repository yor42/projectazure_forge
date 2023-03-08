package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.entity.ContainerSWInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiSWInventory extends AbstractGUIScreen<ContainerSWInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/closers_inventory.png");

    public GuiSWInventory(ContainerSWInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 225;
        this.imageHeight = 240;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 3, 3, this.imageWidth, this.imageHeight);
        matrixStack.popPose();
    }
}
