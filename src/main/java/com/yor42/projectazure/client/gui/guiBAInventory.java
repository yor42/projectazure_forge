package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.ContainerBAInventory;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityGangwon;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class guiBAInventory extends ContainerScreen<ContainerBAInventory> implements IHasContainer<ContainerBAInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/bluearchive_inventory.png");

    private final EntityGunUserBase host;
    private final enums.Affection affectionLevel;
    private final double affection, morale;
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;

    private int x, y;

    public guiBAInventory(ContainerBAInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = (EntityGunUserBase) Main.PROXY.getSharedMob();
        this.affection = this.host.getAffection();
        this.affectionLevel = this.affectionValuetoLevel();
        this.morale = host.getMorale();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2+14;
        this.playerInventoryTitleX = 9;
        this.playerInventoryTitleY = 100;
        this.titleX = 115;
        this.titleY=17;
    }

    private enums.Affection affectionValuetoLevel(){
        if(this.affection>=100.0D){
            if(this.host.isOathed())
                return enums.Affection.OATH;
            else
                return enums.Affection.LOVE;
        }
        else if(this.affection>80 && this.affection<100){
            return enums.Affection.CRUSH;
        }
        else if(this.affection>60 && this.affection<=80){
            return enums.Affection.FRIENDLY;
        }
        else if(this.affection>30 && this.affection<=60){
            return enums.Affection.STRANGER;
        }
        else{
            return enums.Affection.DISAPPOINTED;
        }
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

    private void renderEntity(int mousex, int mousey){
        AbstractEntityCompanion entity = this.host;

        if (entity != null) {
            int entityWidth = (int) entity.getWidth();
            try {
                InventoryScreen.drawEntityOnScreen(57+(entityWidth/2), (int) (15+this.host.getHeight()), 25, (float)(this.x + 51) - mousex, (float)(this.y + 75 - 50) - mousey, entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    private void renderAffection(MatrixStack matrixStack, int mousex, int mousey) {
        matrixStack.push();
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int textureY = 1;
        int textureX = 176;

        int x = 63;
        int y = 78;

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
        if (isPointInRegion(x, y, 12,12,mousex,mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslationTextComponent("gui.current_affection_level").appendString(": ").append(new TranslationTextComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            tooltips.add(new TranslationTextComponent("gui.current_affection_value").appendString(": ").appendString(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            this.renderWrappedToolTip(matrixStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        matrixStack.pop();
    }

    private enums.Morale moraleValuetoLevel(){
        if(this.morale>=120.0D){
            return enums.Morale.REALLY_HAPPY;
        }
        else if(this.affection>=70 && this.affection<120){
            return enums.Morale.HAPPY;
        }
        else if(this.affection>=30 && this.affection<70){
            return enums.Morale.NEUTRAL;
        }
        else if(this.affection>10 && this.affection<=30){
            return enums.Morale.SAD;
        }
        else{
            return enums.Morale.EXHAUSTED;
        }
    }

    private void renderMorale(MatrixStack matrixStack, int mousex, int mousey) {
        matrixStack.push();
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int textureY = 13;
        int textureX = 176;

        int x = 72;
        int y = 16;

        enums.Morale morale = this.moraleValuetoLevel();

        int color=16777215;
        switch (morale){
            case EXHAUSTED:{
                color = 7829367;
                break;
            }
            case SAD:{
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
        if (isPointInRegion(x, y, 12,12,mousex,mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslationTextComponent("gui.current_morale_level").appendString(": ").append(new TranslationTextComponent(morale.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            tooltips.add(new TranslationTextComponent("gui.current_morale_value").appendString(": ").appendString(String.format("%.2f",this.affection)+"/150").setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            this.renderWrappedToolTip(matrixStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        matrixStack.pop();
    }

    private void drawButtons(MatrixStack stack, int mousex, int mousey) {

        int x = this.host.isFreeRoaming()? 185:176;

        ImageButton button = new ImageButton(this.x+10,this.y+63,9,9,x,25,9,TEXTURE, action->switchBehavior());

        if(this.isPointInRegion(10,63,9,9, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslationTextComponent("gui.tooltip_homepos").appendString(": "+this.host.getHomePosition().getX()+" / "+this.host.getHomePosition().getY()+" / "+this.host.getHomePosition().getZ()));
            this.renderWrappedToolTip(stack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        this.addButton(button);
    }

    private void switchBehavior() {
        this.host.SwitchFreeRoamingStatus();
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mousex, int mousey) {
        matrixStack.push();
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 16777215);
        this.font.func_243248_b(matrixStack, this.host.getDisplayName(), (float)17, (float)75, 16777215);
        IFormattableTextComponent leveltext = new StringTextComponent("Lv.").appendString(Integer.toString(this.host.getLevel()));
        this.font.func_243248_b(matrixStack, leveltext, 12, (float)84, 16777215);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.ammostorage.title"), backgroundWidth+5, 5, 16777215);
        this.renderAffection(matrixStack, mousex, mousey);
        this.renderMorale(matrixStack, mousex, mousey);
        this.drawButtons(matrixStack, mousex, mousey);
        this.renderEntity(mousex, mousey);
        matrixStack.pop();
        //this.renderButton(matrixStack);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.blit(matrixStack, this.x+backgroundWidth, this.y, 176, 104, 43, 90);
        matrixStack.pop();
    }
}
