package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.libs.defined;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class guiShipInventory extends ContainerScreen<ContainerKansenInventory> implements IHasContainer<ContainerKansenInventory> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/ship_inventory.png");

    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;

    public guiShipInventory(ContainerKansenInventory container, PlayerInventory playerinventory, ITextComponent titleIn) {
        super(container, playerinventory, titleIn);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
