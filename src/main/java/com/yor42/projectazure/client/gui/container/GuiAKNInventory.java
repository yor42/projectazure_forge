package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiAKNInventory extends AbstractGUIScreen<ContainerAKNInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/arknights_inventory.png");

    public GuiAKNInventory(ContainerAKNInventory screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 171;
        this.imageHeight = 182;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderValues(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }




    private void renderValues(PoseStack matrixStack, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0,TEXTURE);
        int affectionlv1_scaled = (int)(46*Math.min(this.host.getAffection(), 100)/100);
        int affectionlv2_scaled = (int)(46*Math.min(this.host.getAffection()-100, 100)/100);
        int morale_scaled = (int)(35*this.host.getMorale()/150);
        int exp_scaled = (int)(63*this.host.getExp()/this.host.getMaxExp());

        this.blit(matrixStack, this.leftPos + 2, this.topPos + 72, 173, 98, affectionlv1_scaled, 2);
        if(this.host.getAffection()>100){
            this.blit(matrixStack, this.leftPos + 2, this.topPos + 72, 173, 96, affectionlv2_scaled, 2);
        }

        this.blit(matrixStack, this.leftPos + 1, this.topPos + 86, 173, 93, exp_scaled, 2);


        this.blit(matrixStack, this.leftPos + 53, this.topPos + 11, 173, 90, morale_scaled, 2);
    }

    /*
        Change Background Gradient color to white-ish for more arknight-looking
         */
    public void renderBackground(PoseStack matrixStack, int vOffset) {
        if ((this.minecraft != null ? this.minecraft.level : null) != null) {

            this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xcbcbcbC0, 0xC06D6D6D);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundDrawnEvent(this, matrixStack));
        } else {
            this.renderDirtBackground(vOffset);
        }

    }

    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.playerInventoryTitle, 5, 95, 16777215);
        matrixStack.pushPose();
        float scalerate = 0.8F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(matrixStack, new TranslatableComponent("gui.akn_inventory"), 115/scalerate, 8/scalerate, 16777215);
        this.font.draw(matrixStack, new TranslatableComponent("gui.akn_trust"), 2/scalerate, 65/scalerate, 0x313131);
        this.font.draw(matrixStack, new TranslatableComponent("gui.morale"), 54/scalerate, 4/scalerate, 0xffffff);
        this.font.draw(matrixStack, new TranslatableComponent(Integer.toString(this.host.getEntityLevel())), 9/scalerate, 79/scalerate, 0xffffff);
        this.font.drawShadow(matrixStack, this.host.getDisplayName().getString(), 1, 5, 16777215);
        matrixStack.popPose();

        matrixStack.pushPose();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(matrixStack, new TextComponent("Lv."), 1/scalerate, 81/scalerate, 0xffffff);

        Component text = new TextComponent("EXP: ").append(new TextComponent(Integer.toString((int)this.host.getExp())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xf4cf03)))).append(new TranslatableComponent("/"+(int)this.host.getMaxExp()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xffffff))));
        int textwidth = this.font.width(text.getString());
        this.font.draw(matrixStack, text, (64/scalerate)-textwidth, 81/scalerate, 0xffffff);
        this.font.draw(matrixStack, new TextComponent((int)this.host.getAffection()+"%"), 33/scalerate, 68/scalerate, 0x313131);
        matrixStack.popPose();
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        this.renderEntity(48, 75, x, y);
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.host.getAmmoStorage().getSlots()>0) {
            this.blit(matrixStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 214, 0, 42, 87);
        }

        this.blit(matrixStack, this.leftPos-23, this.topPos+7, 114, 7, 20, 10);
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos-23, this.topPos+17+(18*l), 21, 201, 20, 18);
            if(l == (this.host.getSkillItemCount()-1)){
                this.blit(matrixStack, this.leftPos-23, this.topPos+17+(18*(l+1)), 21, 201, 20, 4);
            }
        }
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos-21, this.topPos+20+(18*l), 41, 201, 16, 16);
        }
        matrixStack.popPose();
    }


    @Override
    protected void addButtons() {
        int homeModeX = this.host.isFreeRoaming() ? 0 : 10;
        int ItemPickupX = this.host.shouldPickupItem() ? 0 : 10;

        EntityStatusButton HomeModeButton = new EntityStatusButton(this.host, this.leftPos, this.topPos + 51, 10, 10, homeModeX, 200, 10, 0, TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
        EntityStatusButton ItemPickupButton = new EntityStatusButton(this.host, this.leftPos, this.topPos + 40, 10, 10, ItemPickupX, 220, 10, 0, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM , ITEM_TOOLTIP);

        this.addRenderableWidget(HomeModeButton);
        this.addRenderableWidget(ItemPickupButton);
    }

}
