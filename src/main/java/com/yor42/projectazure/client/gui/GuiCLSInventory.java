package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerCLSInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiCLSInventory extends AbstractContainerScreen<ContainerCLSInventory>{

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/closers_inventory.png");
    private final AbstractEntityCompanion host;

    public GuiCLSInventory(ContainerCLSInventory p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = p_i51105_1_.companion;
        this.imageWidth = 214;
        this.imageHeight = 202;
        this.inventoryLabelX = 11;
        this.inventoryLabelY = 100;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialticks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialticks);
        this.renderValues(PoseStack, mouseX, mouseY);
        this.drawButtons(PoseStack, mouseX, mouseY);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    private void drawButtons(PoseStack PoseStack, int mouseX, int mouseY) {
        int homeModeX = this.host.isFreeRoaming()? 0:14;
        int ItemPickupX = this.host.shouldPickupItem()? 28:42;

        Button homebutton = new ImageButton(this.leftPos+178, this.topPos+141, 14,14, homeModeX, 210, 14,TEXTURE, action->switchBehavior());
        Button itembutton = new ImageButton(this.leftPos+193, this.topPos+141, 14,14, ItemPickupX, 210, 14,TEXTURE, action->switchItemBehavior());
        this.addWidget(homebutton);
        this.addWidget(itembutton);
    }

    @Override
    protected void renderLabels(PoseStack stack, int p_230451_2_, int p_230451_3_) {
        stack.pushPose();
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xe3eef4);
        //this.font.draw(stack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xe3eef4);
        this.font.draw(stack, new TranslatableComponent("gui.ammostorage.title"), this.imageWidth+6, 10, 16777215);
        stack.popPose();
        stack.pushPose();
        stack.scale(0.75F, 0.75F, 0.75F);
        this.font.draw(stack, this.host.getDisplayName(), (float)24/0.75F, (float)26/0.75F, 0xffcd00);
        this.font.drawShadow(stack, this.host.getFoodStats().getFoodLevel()+"/20", (float)188.5/0.75F, (float)180/0.75F, 0xffdd00);
        this.font.drawShadow(stack, this.host.getAffection()+"%", (float)188.5/0.75F, (float)163/0.75F, 0xffdd00);
        stack.popPose();
        stack.pushPose();
        stack.scale(0.5F, 0.5F, 0.5F);
        this.font.draw(stack, "Lv."+this.host.getCompanionLevel(), (float)17/0.5F, (float)19/0.5F, 0xffcd00);
        stack.popPose();
    }

    private void renderValues(PoseStack PoseStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        int health = ((int)(40*(this.host.getHealth()/this.host.getMaxHealth())));
        this.blit(PoseStack, this.leftPos+27,this.topPos+35, 18, 244, health, 6);
        this.blit(PoseStack, this.leftPos+33,this.topPos+195, 58, 251, ((int)(72*(this.host.getExp()/this.host.getMaxExp()))), 5);
        this.blit(PoseStack, this.leftPos+125,this.topPos+195, 58, 246, ((int)(72*((150-this.host.getMorale())/150))), 5);

        this.blit(PoseStack, this.leftPos+179,this.topPos+161, 56, 219, 9, 9);
        this.blit(PoseStack, this.leftPos+179,this.topPos+179, 56, 210, 9, 9);
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
    protected void renderBg(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY) {
        PoseStack.pushPose();
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(PoseStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 214, 0, 42, 87);

        //this.blit(PoseStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 178, 102, 39, 87);
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            //this.blit(PoseStack, this.leftPos-21, this.topPos+57+(19*l), 41, 0, 238, 18);
        }
        this.renderEntity(mouseX, mouseY);
        PoseStack.popPose();
    }
    @OnlyIn(Dist.CLIENT)
    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory(this.leftPos + (100 - (entityWidth / 2)), this.topPos + 80, 30, mousex * -1 + leftPos + (100 - (float)entityWidth / 2), mousey * -1 + this.topPos + 80, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }
}
