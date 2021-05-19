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
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiAKNInventory  extends ContainerScreen<ContainerAKNInventory> implements IHasContainer<ContainerAKNInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/arknights_inventory.png");
    private final AbstractEntityCompanion host;
    private final double affection, morale;


    public GuiAKNInventory(ContainerAKNInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = Main.PROXY.getSharedMob();
        this.affection = this.host.getAffection();
        this.morale = host.getMorale();
        this.xSize = 170;
        this.ySize = 181;
    }

    public int getBackgroundWidth() {
        return this.xSize;
    }

    public int getBackgroundHeight() {
        return this.ySize;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.renderValues(matrixStack, mouseX, mouseY);
    }

    private void renderValues(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int affectionlv1_scaled = (int)(46*Math.min(this.host.getAffection(), 100)/100);

        this.blit(matrixStack, this.guiLeft + 2, this.guiTop + 72, 173, 98, affectionlv1_scaled, 2);
        if(this.host.getAffection()>100){
            this.blit(matrixStack, this.guiLeft + 2, this.guiTop + 72, 173, 96, (int)(46*(Math.min(this.host.getAffection()-100, 100)/100)), 2);
        }
    }

    /*
        Change Background Gradient color to white-ish for more arknight-looking
         */
    public void renderBackground(MatrixStack matrixStack, int vOffset) {
        if ((this.minecraft != null ? this.minecraft.world : null) != null) {

            this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xcbcbcbC0, 0x6D6D6DC0);
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
        matrixStack.pop();

        matrixStack.push();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.func_243248_b(matrixStack, new StringTextComponent((int)this.host.getAffection()+"%"), 33/scalerate, 68/scalerate, 0x313131);
        matrixStack.pop();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderEntity(x, y);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        if(this.host instanceof EntityGunUserBase) {
            this.blit(matrixStack, this.guiLeft + this.xSize + 4, this.guiTop + 9, 178, 102, 38, 86);
        }
        matrixStack.pop();
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
