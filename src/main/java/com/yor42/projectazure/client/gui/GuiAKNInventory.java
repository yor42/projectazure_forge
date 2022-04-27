package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
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
import net.minecraftforge.client.event.ScreenEvent;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiAKNInventory  extends AbstractContainerScreen<ContainerAKNInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/arknights_inventory.png");
    private final AbstractEntityCompanion host;


    public GuiAKNInventory(ContainerAKNInventory screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = screenContainer.companion;
        this.imageWidth = 171;
        this.imageHeight = 182;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderValues(PoseStack, mouseX, mouseY);
        this.drawButtons(PoseStack, mouseX, mouseY);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    private void renderValues(PoseStack PoseStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        int affectionlv1_scaled = (int)(46*Math.min(this.host.getAffection(), 100)/100);
        int affectionlv2_scaled = (int)(46*Math.min(this.host.getAffection()-100, 100)/100);
        int morale_scaled = (int)(35*this.host.getMorale()/150);
        int exp_scaled = (int)(63*this.host.getExp()/this.host.getMaxExp());

        this.blit(PoseStack, this.leftPos + 2, this.topPos + 72, 173, 98, affectionlv1_scaled, 2);
        if(this.host.getAffection()>100){
            this.blit(PoseStack, this.leftPos + 2, this.topPos + 72, 173, 96, affectionlv2_scaled, 2);
        }

        this.blit(PoseStack, this.leftPos + 1, this.topPos + 86, 173, 93, exp_scaled, 2);


        this.blit(PoseStack, this.leftPos + 53, this.topPos + 11, 173, 90, morale_scaled, 2);
    }

    /*
        Change Background Gradient color to white-ish for more arknight-looking
         */
    public void renderBackground(PoseStack PoseStack, int vOffset) {
        if ((this.minecraft != null ? this.minecraft.level : null) != null) {

            this.fillGradient(PoseStack, 0, 0, this.width, this.height, 0xcbcbcbC0, 0xC06D6D6D);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundDrawnEvent(this, PoseStack));
        } else {
            this.renderDirtBackground(vOffset);
        }

    }

    protected void renderLabels(PoseStack PoseStack, int x, int y) {
        //this.font.draw(PoseStack, this.inventory.getDisplayName(), 5, 95, 16777215);
        PoseStack.pushPose();
        float scalerate = 0.8F;
        PoseStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(PoseStack, new TranslatableComponent("gui.akn_inventory"), 115/scalerate, 8/scalerate, 16777215);
        this.font.draw(PoseStack, new TranslatableComponent("gui.akn_trust"), 2/scalerate, 65/scalerate, 0x313131);
        this.font.draw(PoseStack, new TranslatableComponent("gui.morale"), 54/scalerate, 4/scalerate, 0xffffff);
        this.font.draw(PoseStack, new TranslatableComponent(Integer.toString(this.host.getCompanionLevel())), 9/scalerate, 79/scalerate, 0xffffff);
        this.font.drawShadow(PoseStack, this.host.getDisplayName().getString(), 1, 5, 16777215);
        PoseStack.popPose();

        PoseStack.pushPose();
        scalerate = 0.5F;
        PoseStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(PoseStack, new TextComponent("Lv."), 1/scalerate, 81/scalerate, 0xffffff);

        Component text = new TextComponent("EXP: ").append(new TextComponent(Integer.toString((int)this.host.getExp())).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xf4cf03)))).append(new TranslatableComponent("/"+(int)this.host.getMaxExp()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xffffff))));
        int textwidth = this.font.width(text.getString());
        this.font.draw(PoseStack, text, (64/scalerate)-textwidth, 81/scalerate, 0xffffff);
        this.font.draw(PoseStack, new TextComponent((int)this.host.getAffection()+"%"), 33/scalerate, 68/scalerate, 0x313131);
        PoseStack.popPose();
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        PoseStack.pushPose();
        this.renderEntity(x, y);
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.host.getAmmoStorage().getSlots()>0) {
            this.blit(PoseStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 214, 0, 42, 87);
        }

        this.blit(PoseStack, this.leftPos-23, this.topPos+7, 114, 7, 20, 10);
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            this.blit(PoseStack, this.leftPos-23, this.topPos+17+(18*l), 21, 201, 20, 18);
            if(l == (this.host.getSkillItemCount()-1)){
                this.blit(PoseStack, this.leftPos-23, this.topPos+17+(18*(l+1)), 21, 201, 20, 4);
            }
        }
        for(int l = 0; l<this.host.getSkillItemCount(); l++){
            this.blit(PoseStack, this.leftPos-21, this.topPos+20+(18*l), 41, 201, 16, 16);
        }
        PoseStack.popPose();
    }

    private void switchBehavior() {
        if(Screen.hasShiftDown()){
            this.host.clearHomePos();
        }
        else {
            this.host.SwitchFreeRoamingStatus();
        }
    }

    private void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }

    private void drawButtons(PoseStack stack, int mousex, int mousey) {

        int homeModeX = this.host.isFreeRoaming()? 0:10;
        int ItemPickupX = this.host.shouldPickupItem()? 0:10;

        ImageButton HomeModeButton = new ImageButton(this.leftPos,this.topPos+51,10,10,homeModeX,200,10,TEXTURE, action->switchBehavior());
        ImageButton ItemPickupButton = new ImageButton(this.leftPos,this.topPos+40,10,10,ItemPickupX,220,10,TEXTURE, action->switchItemBehavior());

        if(this.isHovering(0,51,10,10, mousex, mousey)){
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
            this.renderComponentTooltip(stack, tooltips, mousex, mousey, this.font);
        }
        else if(this.isHovering(0,40,10,10, mousex, mousey)){
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.off").withStyle(ChatFormatting.BLUE));
            }
            this.renderComponentTooltip(stack, tooltips, mousex, mousey, this.font);
        }

        this.addWidget(HomeModeButton);
        this.addWidget(ItemPickupButton);
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory(this.leftPos + (48 - (entityWidth / 2)), this.topPos + 75, 30, mousex * -1 + leftPos + (53 - (float)entityWidth / 2), mousey * -1 + this.topPos + 70, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

}
