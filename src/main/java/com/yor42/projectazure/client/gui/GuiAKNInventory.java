package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiAKNInventory  extends ContainerScreen<ContainerAKNInventory> implements IHasContainer<ContainerAKNInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/arknights_inventory.png");
    private final AbstractEntityCompanion host;


    public GuiAKNInventory(ContainerAKNInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = Main.PROXY.getSharedMob();
        this.xSize = 171;
        this.ySize = 182;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.renderValues(matrixStack, mouseX, mouseY);
        this.drawButtons(matrixStack, mouseX, mouseY);
    }

    private void renderValues(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int affectionlv1_scaled = (int)(46*Math.min(this.host.getAffection(), 100)/100);
        int affectionlv2_scaled = (int)(46*Math.min(this.host.getAffection()-100, 100)/100);
        int morale_scaled = (int)(35*this.host.getMorale()/150);
        int exp_scaled = (int)(63*this.host.getExp()/this.host.getMaxExp());

        this.blit(matrixStack, this.guiLeft + 2, this.guiTop + 72, 173, 98, affectionlv1_scaled, 2);
        if(this.host.getAffection()>100){
            this.blit(matrixStack, this.guiLeft + 2, this.guiTop + 72, 173, 96, affectionlv2_scaled, 2);
        }

        this.blit(matrixStack, this.guiLeft + 1, this.guiTop + 86, 173, 93, exp_scaled, 2);


        this.blit(matrixStack, this.guiLeft + 53, this.guiTop + 11, 173, 90, morale_scaled, 2);
    }

    /*
        Change Background Gradient color to white-ish for more arknight-looking
         */
    public void renderBackground(MatrixStack matrixStack, int vOffset) {
        if ((this.minecraft != null ? this.minecraft.world : null) != null) {

            this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xcbcbcbC0, 0xC06D6D6D);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent(this, matrixStack));
        } else {
            this.renderDirtBackground(vOffset);
        }

    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), 5, 95, 16777215);
        matrixStack.push();
        float scalerate = 0.8F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.akn_inventory"), 115/scalerate, 8/scalerate, 16777215);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.akn_trust"), 2/scalerate, 65/scalerate, 0x313131);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.morale"), 54/scalerate, 4/scalerate, 0xffffff);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent(Integer.toString(this.host.getLevel())), 9/scalerate, 79/scalerate, 0xffffff);
        matrixStack.pop();

        matrixStack.push();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.func_243248_b(matrixStack, new StringTextComponent("Lv."), 1/scalerate, 81/scalerate, 0xffffff);

        ITextComponent text = new StringTextComponent("EXP: ").append(new StringTextComponent(Integer.toString((int)this.host.getExp())).setStyle(Style.EMPTY.setColor(Color.fromInt(0xf4cf03)))).append(new TranslationTextComponent("/"+(int)this.host.getMaxExp()).setStyle(Style.EMPTY.setColor(Color.fromInt(0xffffff))));
        int textwidth = this.font.getStringWidth(text.getString());
        this.font.func_243248_b(matrixStack, text, (64/scalerate)-textwidth, 81/scalerate, 0xffffff);
        this.font.func_243248_b(matrixStack, new StringTextComponent((int)this.host.getAffection()+"%"), 33/scalerate, 68/scalerate, 0x313131);
        matrixStack.pop();

        this.font.drawStringWithShadow(matrixStack, this.host.getDisplayName().getString(), 11, 53, 16777215);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderEntity(x, y);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        if(this.host instanceof EntityGunUserBase) {
            this.blit(matrixStack, this.guiLeft + this.xSize + 4, this.guiTop + 9, 178, 102, 39, 87);
        }
        matrixStack.pop();
    }

    private void switchBehavior() {
        this.host.SwitchFreeRoamingStatus();
    }

    private void drawButtons(MatrixStack stack, int mousex, int mousey) {

        int x = this.host.isFreeRoaming()? 0:10;

        ImageButton button = new ImageButton(this.guiLeft,this.guiTop+51,10,10,x,200,10,TEXTURE, action->switchBehavior());

        if(this.isPointInRegion(this.guiLeft,this.guiTop+51,10,10, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.on"));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.off"));
            }

            if(this.host.getHomePos() != BlockPos.ZERO) {
                tooltips.add(new TranslationTextComponent("gui.tooltip_homepos").appendString(": " + this.host.getHomePos().getX() + " / " + this.host.getHomePos().getY() + " / " + this.host.getHomePos().getZ()));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.nohome"));
            }
            this.renderWrappedToolTip(stack, tooltips, mousex-this.guiLeft, mousey-this.guiTop, this.font);
        }
        this.addButton(button);
    }

    private void renderEntity(int mousex, int mousey){
        AbstractEntityCompanion entity = this.host;

        if (entity != null) {
            int entityWidth = (int) entity.getWidth();
            try {
                InventoryScreen.drawEntityOnScreen(this.guiLeft+(53-(entityWidth/2)), this.guiTop+75, 30,  mousex*-1+guiLeft+(53-entityWidth/2), mousey*-1+this.guiTop+70, entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

}
