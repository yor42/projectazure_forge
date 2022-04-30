package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class guiBAInventory extends AbstractContainerScreen<ContainerBAInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/bluearchive_inventory.png");

    private final AbstractEntityCompanion host;
    private final enums.ALAffection affectionLevel;
    private final double affection, morale;
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;

    public guiBAInventory(ContainerBAInventory screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = screenContainer.companion;
        this.affection = this.host.getAffection();
        this.affectionLevel = this.affectionValuetoLevel();
        this.morale = this.host.getMorale();
    }

    @Override
    public void init() {
        super.init();

        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2+14;
        this.inventoryLabelX = 9;
        this.inventoryLabelY = 100;
        this.titleLabelX = 115;
        this.titleLabelY=17;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public int getBackgroundWidth() {
        return this.backgroundWidth;
    }

    public int getBackgroundHeight() {
        return this.backgroundHeight;
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
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory((48 - (entityWidth / 2)), 70, 30, mousex * -1 + leftPos + (53 - ((float )entityWidth) / 2), mousey * -1 + this.topPos + 70, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    private void renderAffection(PoseStack PoseStack, int mousex, int mousey) {
        PoseStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
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
        this.blit(PoseStack, x, y, textureX, textureY, 12, 12);
        if (isHovering(x, y, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslatableComponent("gui.current_affection_level").append(": ").append(new TranslatableComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_affection_value").append(": ").append(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(PoseStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        PoseStack.popPose();
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

    private void renderMorale(PoseStack PoseStack, int mousex, int mousey) {
        PoseStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
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
        this.blit(PoseStack, x, y, textureX, textureY, 12, 12);
        if (isHovering(x, y, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslatableComponent("gui.current_morale_level").append(": ").append(new TranslatableComponent(moraleValuetoLevel().getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_morale_value").append(": ").append(String.format("%.2f",this.morale)+"/150").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(PoseStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        PoseStack.popPose();
    }

    private void drawButtons(PoseStack stack, int mousex, int mousey) {

        int FreeRoamingX = this.host.isFreeRoaming()? 185:176;
        int ItemPickupX = this.host.shouldPickupItem()? 194:203;

        ImageButton FreeRoamingButton = new ImageButton(this.x+10,this.y+63,9,9,FreeRoamingX,25,9,TEXTURE, action-> switchFreeRoamingBehavior());
        ImageButton ItemPickupButton = new ImageButton(this.x+10,this.y+53,9,9,ItemPickupX,25,9,TEXTURE, action-> switchItemBehavior());
        if(this.isHovering(10,63,9,9, mousex, mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslatableComponent("gui.tooltip.homemode.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.homemode.off").withStyle(ChatFormatting.BLUE));
            }

            if(this.host.getHOMEPOS().isPresent()) {
                Component shift = new TextComponent("[SHIFT]").withStyle(ChatFormatting.YELLOW);
                BlockPos Home = this.host.getHOMEPOS().get();
                tooltips.add(new TranslatableComponent("gui.tooltip_homepos").append(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()).withStyle(ChatFormatting.BLUE));
                tooltips.add(new TranslatableComponent("gui.tooltip.shifttoclearhome", shift).withStyle(ChatFormatting.GRAY));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.homemode.nohome").withStyle(ChatFormatting.GRAY));
            }
            this.renderComponentTooltip(stack, tooltips, mousex - this.x, mousey - this.y, this.font);
        }
        else if(this.isHovering(10,53,9,9, mousex, mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.off").withStyle(ChatFormatting.BLUE));
            }
            this.renderComponentTooltip(stack, tooltips, mousex - this.x, mousey - this.y, this.font);
        }
        this.addWidget(FreeRoamingButton);
        this.addWidget(ItemPickupButton);
    }

    private void switchFreeRoamingBehavior() {
        if (Screen.hasShiftDown()) {
            this.host.clearHomePos();
        }
        else {
            this.host.SwitchFreeRoamingStatus();
        }
    }

    private void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }

    protected void renderLabels(PoseStack PoseStack, int mousex, int mousey) {

        PoseStack.pushPose();

        int InventoryTextCenter = this.font.width(new TranslatableComponent("gui.companioninventory").getString())/2;
        MutableComponent leveltext = new TextComponent("Lv.").append(Integer.toString(this.host.getCompanionLevel()));
        this.font.draw(PoseStack, new TranslatableComponent("gui.ammostorage.title"), backgroundWidth+6, 5, 16777215);
        //this.font.draw(PoseStack, this.inventory.getDisplayName(), 9, 101, 0x38393b);
        //this.playerInventory.getDisplayName()
        PoseStack.pushPose();
        float drawscale = 0.8F;
        PoseStack.scale(drawscale,drawscale,drawscale);
        this.font.draw(PoseStack, new TranslatableComponent("gui.companioninventory"), (float)(140-(InventoryTextCenter*drawscale))/drawscale, (float)this.titleLabelY/drawscale, 16777215);

        this.font.draw(PoseStack, this.host.getDisplayName(), (float)16/drawscale, (float)78/drawscale, 16777215);
        PoseStack.popPose();

        TextComponent ExpText = new TextComponent((int)this.host.getExp()+"/"+(int)this.host.getMaxExp());
        float ExptextwidthHalf = this.font.width(ExpText.getString())/2.0F;

        PoseStack.pushPose();
        drawscale = 0.5F;
        PoseStack.scale(drawscale,drawscale,drawscale);
        this.font.draw(PoseStack, ExpText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xfdfdfd))), (50.5F-(ExptextwidthHalf*drawscale))/drawscale, 86F/drawscale, 16777215);
        this.font.draw(PoseStack, leveltext.setStyle(Style.EMPTY.withItalic(true).withColor(TextColor.fromRgb(0xdbe4e9))), 12/drawscale, 86.5F/drawscale, 16777215);
        PoseStack.popPose();


        this.renderAffection(PoseStack, mousex, mousey);
        this.renderMorale(PoseStack, mousex, mousey);
        this.drawButtons(PoseStack, mousex, mousey);
        this.renderEntity(mousex, mousey);
        //this.renderButton(PoseStack);
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {

        int expVal = (int)(49*this.host.getExp()/this.host.getMaxExp());
        PoseStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(PoseStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        for(int i = 0; i<this.host.getSkillItemCount(); i++) {
            this.blit(PoseStack, this.x + 67, this.y + 34+i*18, 1, 194, 17, 17);
        }
        this.blit(PoseStack, this.x+backgroundWidth, this.y, 176, 104, 43, 90);
        this.blit(PoseStack, this.x+26, this.y+87, 176, 44, expVal, 2);
        PoseStack.popPose();
    }
}
