package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerSRInventory;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiSRInventory extends AbstractGUIScreen<ContainerSRInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/shiningresonance_inventory.png");

    public GuiSRInventory(ContainerSRInventory p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 174;
        this.imageHeight = 232;
        this.inventoryLabelX = 6;
        this.inventoryLabelY= 137;
        this.titleLabelX = 114;
        this.titleLabelY= 57;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(matrixStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xfeffff);

        matrixStack.pushPose();
        float renderscale = 0.75F;
        matrixStack.scale(renderscale,renderscale,renderscale);
        this.font.draw(matrixStack, new TextComponent(String.valueOf(this.host.getEntityLevel())),6/renderscale, 18.5F/renderscale, 0xfeffff);
        matrixStack.popPose();

        matrixStack.pushPose();
        renderscale = 0.5F;
        matrixStack.scale(renderscale,renderscale,renderscale);
        this.font.draw(matrixStack, new TextComponent("LEVEL"),6/renderscale, 13/renderscale, 0xfeffff);
        this.font.draw(matrixStack, new TextComponent("Name  ").append(this.host.getDisplayName()),36/renderscale, 10.5F/renderscale, 0xfeffff);
        this.font.draw(matrixStack, new TranslatableComponent("gui.sr.atk"),10/renderscale, 40/renderscale, 0xfeffff);
        String value = String.valueOf(((int)this.host.getAttackDamageMainHand()));
        float txtwidth = this.font.width(value)*renderscale;
        this.font.draw(matrixStack, new TextComponent(value),(44-txtwidth)/renderscale, 40/renderscale, 0xfeffff);

        this.font.draw(matrixStack, new TranslatableComponent("gui.sr.def"),10/renderscale, 48/renderscale, 0xfeffff);

        value = String.valueOf(this.host.getArmorValue());
        txtwidth = this.font.width(value)*renderscale;
        this.font.draw(matrixStack, new TextComponent(value),(44-txtwidth)/renderscale, 48/renderscale, 0xfeffff);

        this.font.draw(matrixStack, new TextComponent("Next Level  "+((int)this.host.getMaxExp()-(int)this.host.getExp())),42/renderscale, 28/renderscale, 0xfeffff);
        this.font.drawShadow(matrixStack, new TextComponent("Hp  "+(int)this.host.getHealth()+"/"+(int)this.host.getMaxHealth()),36/renderscale, 19.5F/renderscale, 0xfeffff);
        this.font.drawShadow(matrixStack, new TextComponent("Mr  "+(int)this.host.getHealth()+"/"+(int)this.host.getMaxHealth()),68/renderscale, 19.5F/renderscale, 0xfeffff);
        matrixStack.popPose();

    }

    protected void rendergauges(PoseStack matrixStack, int mouseX, int mouseY) {

        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0,TEXTURE);
        int width = (int)((this.host.getHealth()/this.host.getMaxHealth())*28);
        this.blit(matrixStack, this.leftPos+36, this.topPos+23, 0, 254, width, 2);

        width = (int)((this.host.getMorale()/this.host.getMaxMorale())*28);
        this.blit(matrixStack, this.leftPos+66, this.topPos+23, 0, 252, width, 2);
        matrixStack.popPose();
    }

    @Override
    protected void addButtons() {
        Button homebutton = new EntityStatusButton(this.host, this.leftPos + 6, this.topPos + 59, 16, 16, 240, 0, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
        Button itembutton = new EntityStatusButton(this.host,this.leftPos + 6, this.topPos + 76, 16, 16, 240, 32, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM, ITEM_TOOLTIP);
        Button attackbehaviorbutton = new EntityStatusButton(this.host, this.leftPos + 6, this.topPos + 93, 16, 16, 240, 64, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.DEFENCE);

        this.addRenderableWidget(homebutton);
        this.addRenderableWidget(itembutton);
        this.addRenderableWidget(attackbehaviorbutton);
    }

    @Override
    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(p_230450_1_, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
