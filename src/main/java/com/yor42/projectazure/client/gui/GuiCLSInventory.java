package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerCLSInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiCLSInventory extends ContainerScreen<ContainerCLSInventory> implements IHasContainer<ContainerCLSInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/closers_inventory.png");
    private final AbstractEntityCompanion host;

    public GuiCLSInventory(ContainerCLSInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = Main.PROXY.getSharedMob();
        this.imageWidth = 214;
        this.imageHeight = 202;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialticks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialticks);
        this.renderValues(matrixStack, mouseX, mouseY);
        this.drawButtons(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    private void drawButtons(MatrixStack matrixStack, int mouseX, int mouseY) {
        int homeModeX = this.host.isFreeRoaming()? 0:14;
        int ItemPickupX = this.host.shouldPickupItem()? 0:14;

        Button homebutton = new ImageButton(this.leftPos+178, this.topPos+141, 14,14, homeModeX, 210, 14,TEXTURE, action->switchBehavior());
        Button itembutton = new ImageButton(this.leftPos+193, this.topPos+141, 14,14, ItemPickupX, 210, 14,TEXTURE, action->switchItemBehavior());
        this.addButton(homebutton);
        this.addButton(itembutton);
    }

    private void renderValues(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.blit(matrixStack, 27,35, 18, 244, ((int)(40*(this.host.getHealth()/this.host.getMaxHealth()))), 6);
        this.blit(matrixStack, 33,195, 58, 246, ((int)(72*(this.host.getExp()/this.host.getMaxExp()))), 5);
        this.blit(matrixStack, 125,195, 58, 251, ((int)(72*((150-this.host.getMorale())/150))), 5);

        this.blit(matrixStack, 180,160, 181, 219, 9, 9);
        this.blit(matrixStack, 180,178, 56, 210, 9, 9);
    }

    private void switchBehavior() {
        if(Screen.hasShiftDown()){
            this.host.clearHomePos();
        }
        else {
            this.host.SwitchFreeRoamingStatus();
        }
    }

    private void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        matrixStack.pushPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderEntity(mouseX, mouseY);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(matrixStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 178, 102, 39, 87);
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos-21, this.topPos+57+(19*l), 41, 0, 238, 18);
        }
        matrixStack.popPose();
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientProxy.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory(this.leftPos + (48 - (entityWidth / 2)), this.topPos + 75, 30, mousex * -1 + leftPos + (53 - (float)entityWidth / 2), mousey * -1 + this.topPos + 70, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }
}
