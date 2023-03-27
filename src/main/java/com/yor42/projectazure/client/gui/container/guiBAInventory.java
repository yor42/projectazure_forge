package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class guiBAInventory extends AbstractGUIScreen<ContainerBAInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/bluearchive_inventory.png");

    private final enums.ALAffection affectionLevel;
    private final double affection, morale;
    

    public guiBAInventory(ContainerBAInventory screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.affection = this.host.getAffection();
        this.affectionLevel = this.affectionValuetoLevel();
        this.morale = this.host.getMorale();
        this.inventoryLabelX = 9;
        this.inventoryLabelY = 100;
        this.imageHeight = 193;
        this.imageWidth = 176;
        this.titleLabelX = 115;
        this.titleLabelY=17;
    }
    

    public int getY() {
        return this.getGuiTop();
    }

    public int getleftPos() {
        return this.leftPos;
    }

    public int getBackgroundWidth() {
        return this.imageWidth;
    }

    public int getBackgroundHeight() {
        return this.imageHeight;
    }

    private enums.ALAffection affectionValuetoLevel(){
        if(this.host.getAffection()>=100.0D){
            if(this.host.isOathed())
                return enums.ALAffection.OATH;
            else
                return enums.ALAffection.LOVE;
        }
        else if(this.host.getAffection()>80 && this.host.getAffection()<100){
            return enums.ALAffection.CRUSH;
        }
        else if(this.host.getAffection()>60 && this.host.getAffection()<=80){
            return enums.ALAffection.FRIENDLY;
        }
        else if(this.host.getAffection()>30 && this.host.getAffection()<=60){
            return enums.ALAffection.STRANGER;
        }
        else{
            return enums.ALAffection.DISAPPOINTED;
        }
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

    private void renderAffection(PoseStack matrixStack, int mousex, int mousey) {
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0,TEXTURE);
        int textureY = 1;
        int textureX = 176;

        int x = 63;
        int y = 74;

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
        this.blit(matrixStack, x, y, textureX, textureY, 12, 12);
        if (isHovering(x, y, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslatableComponent("gui.current_affection_level").append(": ").append(new TranslatableComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_affection_value").append(": ").append(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(matrixStack, tooltips,  mousex-this.leftPos, mousey-this.topPos, this.font);
        }
        matrixStack.popPose();
    }

    private enums.Morale moraleValuetoLevel(){
        if(this.host.getMorale()>=120.0D){
            return enums.Morale.REALLY_HAPPY;
        }
        else if(this.morale>=70 && this.morale<120){
            return enums.Morale.HAPPY;
        }
        else if(this.morale>=30 && this.morale<70){
            return enums.Morale.NEUTRAL;
        }
        else if(this.morale>=10 && this.morale<30){
            return enums.Morale.TIRED;
        }
        else{
            return enums.Morale.EXHAUSTED;
        }
    }

    private void renderMorale(PoseStack matrixStack, int mousex, int mousey) {
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0,TEXTURE);
        int textureY = 13;
        int textureX = 176;

        int x = 72;
        int y = 16;

        enums.Morale morale = this.moraleValuetoLevel();

        int color=16777215;
        switch (morale){
            case EXHAUSTED:{
                color = 7829367;
                textureX = 176;
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
        this.blit(matrixStack, x, y, textureX, textureY, 12, 12);
        if (isHovering(x, y, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslatableComponent("gui.current_morale_level").append(": ").append(new TranslatableComponent(moraleValuetoLevel().getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_morale_value").append(": ").append(String.format("%.2f",this.morale)+"/150").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(matrixStack, tooltips, mousex-this.leftPos, mousey-this.topPos, this.font);
        }
        matrixStack.popPose();
    }

    protected void addButtons() {

        int FreeRoamingX = this.host.isFreeRoaming()? 185:176;
        int ItemPickupX = this.host.shouldPickupItem()? 194:203;

        Button FreeRoamingButton = new EntityStatusButton(this.host, this.leftPos +10,this.topPos+63,9,9,FreeRoamingX,25,9,0,TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
        Button ItemPickupButton = new EntityStatusButton(this.host, this.leftPos +10,this.topPos+53,9,9,ItemPickupX,25,9,0,TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM, ITEM_TOOLTIP);

        this.addRenderableWidget(FreeRoamingButton);
        this.addRenderableWidget(ItemPickupButton);
    }

    protected void renderLabels(PoseStack matrixStack, int mousex, int mousey) {

        matrixStack.pushPose();

        int InventoryTextCenter = this.font.width(new TranslatableComponent("gui.companioninventory").getString())/2;
        MutableComponent leveltext = new TextComponent("Lv.").append(Integer.toString(this.host.getEntityLevel()));
        this.font.draw(matrixStack, new TranslatableComponent("gui.ammostorage.title"), imageWidth+6, 5, 16777215);
        this.font.draw(matrixStack, this.playerInventoryTitle, 9, 101, 0x38393b);
        //this.playerInventory.getDisplayName()
        matrixStack.pushPose();
        float drawscale = 0.8F;
        matrixStack.scale(drawscale,drawscale,drawscale);
        this.font.draw(matrixStack, new TranslatableComponent("gui.companioninventory"), (140-(InventoryTextCenter*drawscale)) /drawscale, (float)this.titleLabelY/drawscale, 16777215);

        this.font.draw(matrixStack, this.host.getDisplayName(), (float)16/drawscale, (float)78/drawscale, 16777215);
        matrixStack.popPose();

        TextComponent ExpText = new TextComponent((int)this.host.getExp()+"/"+(int)this.host.getMaxExp());
        float ExptextwidthHalf = this.font.width(ExpText.getString())/2.0F;

        matrixStack.pushPose();
        drawscale = 0.5F;
        matrixStack.scale(drawscale,drawscale,drawscale);
        this.font.draw(matrixStack, ExpText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xfdfdfd))), (50.5F-(ExptextwidthHalf*drawscale))/drawscale, 86F/drawscale, 16777215);
        this.font.draw(matrixStack, leveltext.setStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromRgb(0xdbe4e9))), 12/drawscale, 86.5F/drawscale, 16777215);
        matrixStack.popPose();


        this.renderAffection(matrixStack, mousex, mousey);
        this.renderMorale(matrixStack, mousex, mousey);
        //this.renderButton(matrixStack);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {

        matrixStack.pushPose();
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        for(int i = 0; i<this.host.getSkillItemCount(); i++) {
            this.blit(matrixStack, this.leftPos + 67, this.topPos + 34+i*18, 1, 194, 17, 17);
        }
        this.blit(matrixStack, this.leftPos +imageWidth, this.topPos, 176, 104, 43, 90);
        this.blit(matrixStack, this.leftPos +26, this.topPos+87, 176, 44, this.getEXPgauge(49), 2);
        this.renderEntity(48, 70,x, y);
        matrixStack.popPose();
    }
}
