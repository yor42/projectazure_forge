package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.ReopenEntityInventoryPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class guiRiggingInventory extends AbstractContainerScreen<RiggingContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/rigging_inventory.png");
    
    private final ItemStack riggingStack;

    public guiRiggingInventory(RiggingContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.riggingStack = screenContainer.riggingStack;
        this.inventoryLabelX = 9;
        this.inventoryLabelY = 100;
        this.titleLabelX = 11;
        this.titleLabelY=9;
        this.imageWidth = 176;
        this.imageHeight = 193;
    }


    @Override
    protected void init() {
        super.init();
        if (this.menu.previousEntityUUID != null) {
            ImageButton RiggingButton = new ImageButton(this.leftPos + 81, this.topPos + 6, 12, 12, 22, 193, 13, TEXTURE, action -> switchScreen());
            this.addRenderableWidget(RiggingButton);
        }
    }

    private void switchScreen() {
        this.onClose();
        Main.NETWORK.sendToServer(new ReopenEntityInventoryPacket(this.menu.previousEntityUUID));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderSlotBackgrounds(matrixStack);
    }

    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        //this.font.draw(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 14085119);
        this.font.draw(matrixStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 14085119);
    }
    private void renderSlotBackgrounds(PoseStack matrixStack){
        if(this.riggingStack.getItem() instanceof ItemRiggingBase) {
            if(((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount()>0) {
                this.blit(matrixStack, this.leftPos+6, this.topPos+26, 176, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount(); i++){
                    this.blit(matrixStack, this.leftPos+6, this.topPos+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount()>0) {
                this.blit(matrixStack, this.leftPos+30, this.topPos+26, 194, 59, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount(); i++){
                    this.blit(matrixStack, this.leftPos+30+18*i, this.topPos+33, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()>0) {
                this.blit(matrixStack, this.leftPos+150, this.topPos+26, 176, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(); i++){
                    this.blit(matrixStack, this.leftPos+150, this.topPos+33+18*i, 176, 0, 18, 18);
                }
            }

            if(((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount()>0) {
                this.blit(matrixStack, this.leftPos+29, this.topPos+69, 194, 52, 18, 7);
                for(int i = 0; i<((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount(); i++){
                    this.blit(matrixStack, this.leftPos+29+18*i, this.topPos+76, 176, 0, 18, 18);
                }
            }
        }

    }
}
