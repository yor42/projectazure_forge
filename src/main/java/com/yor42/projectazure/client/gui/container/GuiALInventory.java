package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerALInventory;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.packets.ChangeContainerPacket;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GuiALInventory extends AbstractGUIScreen<ContainerALInventory>{

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/ship_inventory.png");

    private final enums.ALAffection affectionLevel;
    private double affection=0D;
    private double morale;
    private final Inventory inventory;

    public GuiALInventory(ContainerALInventory container, Inventory playerinventory, Component titleIn) {
        super(container, playerinventory, titleIn);

        this.affection = this.host.getAffection();
        this.morale = this.host.getMorale();

        this.affectionLevel = this.affectionValuetoLevel();
        this.inventory = playerinventory;
        this.imageWidth = 176;
        this.imageHeight = 193;
        this.inventoryLabelX = 9;
        this.inventoryLabelY = 100;
        this.titleLabelX = 11;
        this.titleLabelY=9;
    }
    

    private enums.ALAffection affectionValuetoLevel(){
        if(this.affection>=100.0D){
            if(this.host.isOathed())
                return enums.ALAffection.OATH;
            else
                return enums.ALAffection.LOVE;
        }
        else if(this.affection>80){
            return enums.ALAffection.CRUSH;
        }
        else if(this.affection>60){
            return enums.ALAffection.FRIENDLY;
        }
        else if(this.affection>30){
            return enums.ALAffection.STRANGER;
        }
        else{
            return enums.ALAffection.DISAPPOINTED;
        }
    }

    public int getY() {
        return this.topPos;
    }

    public int getX() {
        return this.leftPos;
    }

    public int getBackgroundWidth() {
        return this.imageWidth;
    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack matrixstacck, int mouseX, int mousey, float partialticks) {
        this.renderBackground(matrixstacck);
        super.render(matrixstacck, mouseX, mousey, partialticks);
        this.renderEntity(110, 105,mouseX, mousey);
        this.renderTooltip(matrixstacck, mouseX, mousey);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(matrixStack, this.leftPos+this.imageWidth, this.topPos, 176, 104, 43, 90);
        matrixStack.popPose();
    }

    protected void renderLabels(PoseStack matrixStack, int mousex, int mousey) {
        matrixStack.pushPose();
        float rendersize = 0.8F;
        matrixStack.scale(rendersize, rendersize, rendersize);
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX/rendersize, (float)this.titleLabelY/rendersize, 14085119);
        this.font.draw(matrixStack, this.host.getDisplayName(), (float)76/rendersize, (float)25/rendersize, 14085119);
        this.font.draw(matrixStack, new TranslatableComponent("gui.ammostorage.title"), (this.imageWidth+5)/rendersize, 6/rendersize, 14085119);
        matrixStack.popPose();
        MutableComponent leveltext = new TextComponent("Lv.").append(Integer.toString(this.host.getEntityLevel()));
        this.font.draw(matrixStack, leveltext, (float)168-this.font.width(leveltext), (float)81, 14085119);
        this.renderAffection(matrixStack, mousex, mousey);
        this.renderMorale(matrixStack, mousex, mousey);
        //this.renderButton(matrixStack);
    }


    @Override
    protected void addButtons() {
        int FreeRoamX = this.host.isFreeRoaming()? 185:176;
        int itemPickupX = this.host.shouldPickupItem()? 176:185;
        ImageButton FreeroamButton = new ImageButton(this.leftPos+159,this.topPos+52,9,9,FreeRoamX,25,9,TEXTURE,256,256, action->switchBehavior(), FREEROAM_TOOLTIP, TextComponent.EMPTY);
        ImageButton ItemPickupButton = new ImageButton(this.leftPos+159,this.topPos+62,9,9,itemPickupX,43,9,TEXTURE,256,256,action->switchItemBehavior(), ITEM_TOOLTIP, TextComponent.EMPTY);
        ImageButton RiggingButton = new ImageButton(this.leftPos+98,this.topPos+6,12,12,14,193,13,TEXTURE,action->switchScreen());

        this.addRenderableWidget(ItemPickupButton);
        this.addRenderableWidget(FreeroamButton);
        this.addRenderableWidget(RiggingButton);
    }

    private void switchScreen() {

        if(!(this.host.getRigging().getItem() instanceof ItemRiggingBase)){
            return;
        }
        Main.NETWORK.sendToServer(new ChangeContainerPacket(this.host.getRigging(), this.host.getUUID()));
        this.onClose();
    }

    private void renderAffection(PoseStack matrixStack, int mousex, int mousey) {
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0,TEXTURE);
        int textureY = 1;
        int textureX = 176;

        int color=16777215;
        switch (this.affectionLevel){
            case DISAPPOINTED:{
                color = 7829367;
                break;
            }
            case STRANGER:{
                color = 10021373;
                textureX = 188;
                break;
            }
            case FRIENDLY:{
                color = 10021373;
                textureX = 200;
                break;
            }
            case CRUSH:{
                color = 8702971;
                textureX = 212;
                break;
            }
            case LOVE:{
                color = 15964118;
                textureX = 224;
                break;
            }
            case OATH:{
                color = 16702964;
                textureX = 236;
                break;
            }
        }
        this.blit(matrixStack, 139, 35, textureX, textureY, 12, 12);
        if (isHovering(139, 35, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslatableComponent("gui.current_affection_level").append(": ").append(new TranslatableComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_affection_value").append(": ").append(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(matrixStack, tooltips, mousex-this.leftPos, mousey-this.topPos, this.font);
        }
        matrixStack.popPose();
    }

    private void renderMorale(PoseStack matrixStack, int mousex, int mousey) {
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0,TEXTURE);
        int textureY = 13;
        int textureX = 176;

        enums.Morale morale = this.host.moraleValuetoLevel();

        int color=16777215;
        switch (morale){
            case EXHAUSTED:{
                color = 7829367;
                break;
            }
            case TIRED:{
                color = 16481134;
                textureX = 188;
                break;
            }
            case NEUTRAL:{
                color = 16695668;
                textureX = 200;
                break;
            }
            case HAPPY:{
                color = 9824105;
                textureX = 212;
                break;
            }
            case REALLY_HAPPY:{
                color = 11925139;
                textureX = 224;
                break;
            }
        }
        this.blit(matrixStack, 125, 35, textureX, textureY, 12, 12);
        if (isHovering(125, 35, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslatableComponent("gui.current_morale_level").append(": ").append(new TranslatableComponent(morale.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_morale_value").append(": ").append(String.format("%.2f",this.morale)+"/150").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(matrixStack, tooltips, mousex-this.leftPos, mousey-this.topPos, this.font);
        }
        matrixStack.popPose();
    }
}
