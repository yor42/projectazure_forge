package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class guiRiggingInventory extends AbstractContainerScreen<RiggingContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/rigging_inventory.png");

    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;
    private ItemStack riggingStack;

    public guiRiggingInventory(RiggingContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.riggingStack = screenContainer.riggingStack;
    }

    @Override
    public void init() {
        super.init();
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
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(PoseStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.renderSlotBackgrounds(PoseStack);
    }

    protected void renderLabels(PoseStack PoseStack, int x, int y) {
        //this.font.draw(PoseStack, this.title, (float)this.titleX, (float)this.titleY, 14085119);
    }
    private void renderSlotBackgrounds(PoseStack PoseStack){
        if(this.riggingStack.getItem() instanceof ItemRiggingBase) {
            if(((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount()>0) {
                this.blit(PoseStack, this.x+6, this.y+26, 176, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount(); i++){
                    this.blit(PoseStack, this.x+6, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount()>0) {
                this.blit(PoseStack, this.x+30, this.y+26, 194, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount(); i++){
                    this.blit(PoseStack, this.x+30+18*i, this.y+33, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()>0) {
                this.blit(PoseStack, this.x+150, this.y+26, 176, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(); i++){
                    this.blit(PoseStack, this.x+150, this.y+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount()>0) {
                this.blit(PoseStack, this.x+29, this.y+69, 194, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount(); i++){
                    this.blit(PoseStack, this.x+29+18*i, this.y+76, 176, 0, 18, 18);
                }
            }
        }

    }
}
