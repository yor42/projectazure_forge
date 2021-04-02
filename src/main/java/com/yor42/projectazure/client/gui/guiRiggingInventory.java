package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.defined;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class guiRiggingInventory extends ContainerScreen<RiggingContainer> implements IHasContainer<RiggingContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/rigging_inventory.png");

    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;
    private ItemStack RiggingStack;

    public guiRiggingInventory(RiggingContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.RiggingStack = Main.PROXY.getSharedStack();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2+14;
        this.playerInventoryTitleX = 9;
        this.playerInventoryTitleY = 100;
        this.titleX = 11;
        this.titleY=9;
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
        this.renderSlotBackgrounds(matrixStack);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        //this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 14085119);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 14085119);
    }
    private void renderSlotBackgrounds(MatrixStack matrixStack){
        if(this.RiggingStack.getItem() instanceof ItemRiggingBase) {
            if(((ItemRiggingBase) this.RiggingStack.getItem()).getMainGunSlotCount()>0) {
                this.blit(matrixStack, this.x+6, this.y+26, 176, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.RiggingStack.getItem()).getMainGunSlotCount(); i++){
                    this.blit(matrixStack, this.x+6, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.RiggingStack.getItem()).getSubGunSlotCount()>0) {
                this.blit(matrixStack, this.x+30, this.y+26, 194, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.RiggingStack.getItem()).getSubGunSlotCount(); i++){
                    this.blit(matrixStack, this.x+30+18*i, this.y+33, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.RiggingStack.getItem()).getAASlotCount()>0) {
                this.blit(matrixStack, this.x+150, this.y+26, 176, 52, 18, 7);
                for(int i=0; i<((ItemRiggingBase) this.RiggingStack.getItem()).getAASlotCount(); i++){
                    this.blit(matrixStack, this.x+150, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.RiggingStack.getItem()).getTorpedoSlotCount()>0) {
                this.blit(matrixStack, this.x+29, this.y+69, 194, 52, 18, 7);
                for(int i=0; i<((ItemRiggingBase) this.RiggingStack.getItem()).getTorpedoSlotCount(); i++){
                    this.blit(matrixStack, this.x+29+18*i, this.y+76, 176, 0, 18, 18);
                }
            }
        }

    }
}
