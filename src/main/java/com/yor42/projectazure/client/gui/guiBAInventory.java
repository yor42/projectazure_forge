package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
        this.morale = this.host.getMorale();
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

    private enums.Affection affectionValuetoLevel(){
        if(this.host.getAffection()>=100.0D){
            if(this.host.isOathed())
                return enums.Affection.OATH;
            else
                return enums.Affection.LOVE;
        }
        else if(this.host.getAffection()>80 && this.host.getAffection()<100){
            return enums.Affection.CRUSH;
        }
        else if(this.host.getAffection()>60 && this.host.getAffection()<=80){
            return enums.Affection.FRIENDLY;
        }
        else if(this.host.getAffection()>30 && this.host.getAffection()<=60){
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
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientProxy.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.copyDataFromOld(this.host);
            int entityWidth = (int) entity.getWidth();
            try {
                InventoryScreen.drawEntityOnScreen(this.guiLeft + (46 - (entityWidth / 2)), this.guiTop + 75, 30, mousex * -1 + guiLeft + (53 - entityWidth / 2), mousey * -1 + this.guiTop + 70, (LivingEntity) entity);
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
        if (isPointInRegion(x, y, 12,12,mousex,mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            tooltips.add(new TranslationTextComponent("gui.current_morale_level").appendString(": ").append(new TranslationTextComponent(moraleValuetoLevel().getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            tooltips.add(new TranslationTextComponent("gui.current_morale_value").appendString(": ").appendString(String.format("%.2f",this.morale)+"/150").setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            this.renderWrappedToolTip(matrixStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        matrixStack.pop();
    }

    private void drawButtons(MatrixStack stack, int mousex, int mousey) {

        int FreeRoamingX = this.host.isFreeRoaming()? 185:176;
        int ItemPickupX = this.host.shouldPickupItem()? 194:203;

        ImageButton FreeRoamingButton = new ImageButton(this.x+10,this.y+63,9,9,FreeRoamingX,25,9,TEXTURE, action-> switchFreeRoamingBehavior());
        ImageButton ItemPickupButton = new ImageButton(this.x+10,this.y+53,9,9,ItemPickupX,25,9,TEXTURE, action-> switchItemBehavior());
        if(this.isPointInRegion(10,63,9,9, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.on").mergeStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.off").mergeStyle(TextFormatting.BLUE));
            }

            if(this.host.getHOMEPOS().isPresent()) {
                ITextComponent shift = new StringTextComponent("[SHIFT]").mergeStyle(TextFormatting.YELLOW);
                BlockPos Home = this.host.getHOMEPOS().get();
                tooltips.add(new TranslationTextComponent("gui.tooltip_homepos").appendString(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()).mergeStyle(TextFormatting.BLUE));
                tooltips.add(new TranslationTextComponent("gui.tooltip.shifttoclearhome", shift).mergeStyle(TextFormatting.GRAY));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.nohome").mergeStyle(TextFormatting.GRAY));
            }
            this.renderWrappedToolTip(stack, tooltips, mousex - this.x, mousey - this.y, this.font);
        }
        else if(this.isPointInRegion(10,53,9,9, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.on").mergeStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.off").mergeStyle(TextFormatting.BLUE));
            }
            this.renderWrappedToolTip(stack, tooltips, mousex - this.x, mousey - this.y, this.font);
        }
        this.addButton(FreeRoamingButton);
        this.addButton(ItemPickupButton);
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

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mousex, int mousey) {

        matrixStack.push();

        int InventoryTextCenter = this.font.getStringWidth(new TranslationTextComponent("gui.companioninventory").getString())/2;
        IFormattableTextComponent leveltext = new StringTextComponent("Lv.").appendString(Integer.toString(this.host.getLevel()));
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.ammostorage.title"), backgroundWidth+6, 5, 16777215);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), 9, 101, 0x38393b);
        //this.playerInventory.getDisplayName()
        matrixStack.push();
        float drawscale = 0.8F;
        matrixStack.scale(drawscale,drawscale,drawscale);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.companioninventory"), (float)(140-(InventoryTextCenter*drawscale))/drawscale, (float)this.titleY/drawscale, 16777215);

        this.font.func_243248_b(matrixStack, this.host.getDisplayName(), (float)16/drawscale, (float)78/drawscale, 16777215);
        matrixStack.pop();

        StringTextComponent ExpText = new StringTextComponent((int)this.host.getExp()+"/"+(int)this.host.getMaxExp());
        float ExptextwidthHalf = this.font.getStringWidth(ExpText.getString())/2.0F;

        matrixStack.push();
        drawscale = 0.5F;
        matrixStack.scale(drawscale,drawscale,drawscale);
        this.font.func_243248_b(matrixStack, ExpText.setStyle(Style.EMPTY.setColor(Color.fromInt(0xfdfdfd))), (50.5F-(ExptextwidthHalf*drawscale))/drawscale, 86F/drawscale, 16777215);
        this.font.func_243248_b(matrixStack, leveltext.setStyle(Style.EMPTY.setItalic(true).setColor(Color.fromInt(0xdbe4e9))), 12/drawscale, 86.5F/drawscale, 16777215);
        matrixStack.pop();


        this.renderAffection(matrixStack, mousex, mousey);
        this.renderMorale(matrixStack, mousex, mousey);
        this.drawButtons(matrixStack, mousex, mousey);
        this.renderEntity(mousex, mousey);
        //this.renderButton(matrixStack);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

        int expVal = (int)(49*this.host.getExp()/this.host.getMaxExp());
        matrixStack.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.blit(matrixStack, this.x+backgroundWidth, this.y, 176, 104, 43, 90);
        this.blit(matrixStack, this.x+26, this.y+87, 176, 44, expVal, 2);
        matrixStack.pop();
    }
}
