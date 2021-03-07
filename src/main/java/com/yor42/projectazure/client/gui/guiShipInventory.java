package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
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

public class guiShipInventory extends ContainerScreen<ContainerKansenInventory> implements IHasContainer<ContainerKansenInventory> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/ship_inventory.png");

    private final EntityKansenBase host;
    private final enums.Affection affectionLevel;
    private final double affection;
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 193;
    private PlayerInventory inventory;

    private int x, y;

    public guiShipInventory(ContainerKansenInventory container, PlayerInventory playerinventory, ITextComponent titleIn) {
        super(container, playerinventory, titleIn);
        this.host = (EntityKansenBase) Main.PROXY.getSharedMob();
        this.affection = this.host.getAffection();
        this.affectionLevel = this.affectionValuetoLevel();
        this.inventory = playerinventory;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2+14;
        this.playerInventoryTitleX = 9;
        this.playerInventoryTitleY = 100;
        this.titleX = 11;
        this.titleY=9;
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

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.blit(matrixStack, this.x+backgroundWidth, this.y, 176, 104, 43, 90);
        matrixStack.pop();
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mousex, int mousey) {
        matrixStack.push();
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 14085119);
        this.font.func_243248_b(matrixStack, this.host.getDisplayName(), (float)76, (float)25, 14085119);
        IFormattableTextComponent leveltext = new StringTextComponent("Lv.").appendString(Integer.toString(this.host.getLevel()));
        this.font.func_243248_b(matrixStack, leveltext, (float)168-this.font.getStringPropertyWidth(leveltext), (float)81, 14085119);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.ammostorage.title"), backgroundWidth+5, 5, 14085119);
        this.renderAffection(matrixStack, mousex, mousey);
        this.renderEntity(mousex, mousey);
        this.drawButtons();
        matrixStack.pop();
        //this.renderButton(matrixStack);
    }

    private void drawButtons() {

        int x = this.host.isFreeRoaming()? 185:176;

        ImageButton button = new ImageButton(this.x+159,this.y+52,9,9,x,25,9,TEXTURE,action->switchBehavior());
        this.addButton(button);
    }

    private void switchBehavior() {
        this.host.SwitchFreeRoamingStatus();
    }

    private void renderButton(MatrixStack matrixStack) {
        for (int i = 0; i < 5; i++){
            if(i==0)
                continue;
            int finalI = i;
            new ImageButton(81+(12*i)+8,6,12,12,1,193,0,TEXTURE, action -> moveTab(finalI));
        }
    }

    private void moveTab(int finalI) {
        inventory.player.closeScreen();
    }

    private void renderEntity(int mousex, int mousey){
        EntityKansenBase entity = this.host;

        //FIXME: MatrixStack Overflow when it tries to render EntityGangwon
        if (entity != null && !(entity instanceof EntityGangwon)) {
            int entityWidth = (int) entity.getWidth();
            try {
                InventoryScreen.drawEntityOnScreen(113+(entityWidth/2), 90, 25, (float)(this.x + 51) - mousex, (float)(this.y + 75 - 50) - mousey, entity);
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
        if (isPointInRegion(139, 35, 12,12,mousex,mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            double AffectionLimit = this.host.isOathed()? 200:100;
            tooltips.add(new TranslationTextComponent("gui.current_affection_level").appendString(": ").append(new TranslationTextComponent(this.affectionLevel.getName())).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            tooltips.add(new TranslationTextComponent("gui.current_affection_value").appendString(": ").appendString(String.format("%.2f",this.affection)+"/"+AffectionLimit).setStyle(Style.EMPTY.setColor(Color.fromInt(color))));
            this.renderWrappedToolTip(matrixStack, tooltips, mousex-this.x, mousey-this.y, this.font);
        }
        matrixStack.pop();
    }
}
