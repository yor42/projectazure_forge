package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class guiRiggingInventory extends ContainerScreen<RiggingContainer> implements IHasContainer<RiggingContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/rigging_inventory.png");

    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;
    private ItemStack riggingStack;

    public guiRiggingInventory(RiggingContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.riggingStack = screenContainer.riggingStack;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2+14;
        this.inventoryLabelX = 9;
        this.inventoryLabelY = 100;
        this.titleLabelX = 11;
        this.titleLabelY=9;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.renderSlotBackgrounds(matrixStack);
    }

    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        //this.font.draw(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 14085119);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 14085119);
    }
    private void renderSlotBackgrounds(MatrixStack matrixStack){
        if(this.riggingStack.getItem() instanceof ItemRiggingBase) {
            if(((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount()>0) {
                this.blit(matrixStack, this.x+6, this.y+26, 176, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount(); i++){
                    this.blit(matrixStack, this.x+6, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount()>0) {
                this.blit(matrixStack, this.x+30, this.y+26, 194, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount(); i++){
                    this.blit(matrixStack, this.x+30+18*i, this.y+33, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()>0) {
                this.blit(matrixStack, this.x+150, this.y+26, 176, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(); i++){
                    this.blit(matrixStack, this.x+150, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount()>0) {
                this.blit(matrixStack, this.x+29, this.y+69, 194, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount(); i++){
                    this.blit(matrixStack, this.x+29+18*i, this.y+76, 176, 0, 18, 18);
                }
            }
        }

    }
}
