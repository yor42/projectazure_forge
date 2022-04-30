package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
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

public class GuiALInventory extends AbstractContainerScreen<ContainerKansenInventory> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/ship_inventory.png");

    private final EntityKansenBase host;
    private final enums.ALAffection affectionLevel;
    private double affection=0D;
    private double morale;
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;
    private final Inventory inventory;

    private int x, y;

    public GuiALInventory(ContainerKansenInventory container, Inventory playerinventory, Component titleIn) {
        super(container, playerinventory, titleIn);
        this.host = container.entity;
        if(container.entity != null) {
            this.affection = this.host.getAffection();
            this.morale = this.host.getMorale();
        }
        this.affectionLevel = this.affectionValuetoLevel();
        this.inventory = playerinventory;
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

    private enums.Morale moraleValuetoLevel(){
        if(this.morale>=120.0D){
            return enums.Morale.REALLY_HAPPY;
        }
        else if(this.morale>=70){
            return enums.Morale.HAPPY;
        }
        else if(this.morale>=30){
            return enums.Morale.NEUTRAL;
        }
        else if(this.morale>10){
            return enums.Morale.TIRED;
        }
        else{
            return enums.Morale.EXHAUSTED;
        }
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
        PoseStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(PoseStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.blit(PoseStack, this.x+backgroundWidth, this.y, 176, 104, 43, 90);
        PoseStack.popPose();
    }

    protected void renderLabels(PoseStack PoseStack, int mousex, int mousey) {
        PoseStack.pushPose();
        float rendersize = 0.8F;
        PoseStack.scale(rendersize, rendersize, rendersize);
        this.font.draw(PoseStack, this.title, (float)this.titleLabelX/rendersize, (float)this.titleLabelY/rendersize, 14085119);
        this.font.draw(PoseStack, this.host.getDisplayName(), (float)76/rendersize, (float)25/rendersize, 14085119);
        this.font.draw(PoseStack, new TranslatableComponent("gui.ammostorage.title"), (this.backgroundWidth+5)/rendersize, 6/rendersize, 14085119);
        PoseStack.popPose();
        MutableComponent leveltext = new TextComponent("Lv.").append(Integer.toString(this.host.getCompanionLevel()));
        this.font.draw(PoseStack, leveltext, (float)168-this.font.width(leveltext), (float)81, 14085119);
        this.renderAffection(PoseStack, mousex, mousey);
        this.renderMorale(PoseStack, mousex, mousey);
        this.renderEntity(mousex, mousey);
        this.drawButtons(PoseStack, mousex, mousey);
        //this.renderButton(PoseStack);
    }

    private void drawButtons(PoseStack stack, int MouseX, int MouseY) {

        int FreeRoamX = this.host.isFreeRoaming()? 185:176;
        int itemPickupX = this.host.shouldPickupItem()? 176:185;
        ImageButton FreeroamButton = new ImageButton(this.x+159,this.y+52,9,9,FreeRoamX,25,9,TEXTURE, action->switchBehavior());
        ImageButton ItemPickupButton = new ImageButton(this.x+159,this.y+62,9,9,itemPickupX,43,9,TEXTURE,action->switchItemBehavior());

        if(this.isHovering(159,52,9,9, MouseX, MouseY)){
            List<MutableComponent> tooltips = new ArrayList<>();

            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslatableComponent("gui.tooltip.freeroaming.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.freeroaming.off").withStyle(ChatFormatting.BLUE));
            }

            if(this.host.getHOMEPOS().isPresent()) {
                BlockPos Home = this.host.getHOMEPOS().get();
                tooltips.add(new TranslatableComponent("gui.tooltip.homepos").append(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()));
            }else{
                tooltips.add(new TranslatableComponent("gui.tooltip.homemode.nohome").withStyle(ChatFormatting.GRAY));
            }
            this.renderComponentTooltip(stack, tooltips, MouseX-this.x, MouseY-this.y, this.font);
        }
        else if(this.isHovering(159,62,9,9, MouseX, MouseY)){
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.off").withStyle(ChatFormatting.BLUE));
            }
            this.renderComponentTooltip(stack, tooltips, MouseX-this.x, MouseY-this.y, this.font);
        }
        this.addWidget(ItemPickupButton);
        this.addWidget(FreeroamButton);
    }

    private void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }
    private void switchBehavior() {
        this.host.SwitchFreeRoamingStatus();
    }

    private void renderButton(PoseStack PoseStack) {
        for (int i = 0; i < 5; i++){
            if(i==0)
                continue;
            int finalI = i;
            new ImageButton(81+(12*i)+8,6,12,12,1,193,0,TEXTURE, action -> moveTab(finalI));
        }
    }

    private void moveTab(int finalI) {
        inventory.player.closeContainer();
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory((110 - (entityWidth / 2)), 105, 30, mousex * -1 + leftPos + (53 - entityWidth / 2), mousey * -1 + this.topPos + 70, (LivingEntity) entity);
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
        this.blit(PoseStack, 139, 35, textureX, textureY, 12, 12);
        if (isHovering(139, 35, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslatableComponent("gui.current_affection_level").append(": ").append(new TranslatableComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_affection_value").append(": ").append(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(PoseStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        PoseStack.popPose();
    }

    private void renderMorale(PoseStack PoseStack, int mousex, int mousey) {
        PoseStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        int textureY = 13;
        int textureX = 176;

        enums.Morale morale = this.moraleValuetoLevel();

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
        this.blit(PoseStack, 125, 35, textureX, textureY, 12, 12);
        if (isHovering(125, 35, 12,12,mousex,mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslatableComponent("gui.current_morale_level").append(": ").append(new TranslatableComponent(morale.getName())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            tooltips.add(new TranslatableComponent("gui.current_morale_value").append(": ").append(String.format("%.2f",this.morale)+"/150").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
            this.renderComponentTooltip(PoseStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        PoseStack.popPose();
    }
}
