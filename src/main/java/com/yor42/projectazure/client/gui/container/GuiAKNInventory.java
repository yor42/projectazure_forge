package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;
import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiAKNInventory extends ContainerScreen<ContainerAKNInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/arknights_inventory.png");
    private final AbstractEntityCompanion companion;


    public GuiAKNInventory(ContainerAKNInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.companion = screenContainer.companion;
        this.imageWidth = 171;
        this.imageHeight = 182;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderValues(matrixStack, mouseX, mouseY);
        this.drawButtons(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }




    private void renderValues(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        int affectionlv1_scaled = (int)(46*Math.min(this.companion.getAffection(), 100)/100);
        int affectionlv2_scaled = (int)(46*Math.min(this.companion.getAffection()-100, 100)/100);
        int morale_scaled = (int)(35*this.companion.getMorale()/150);
        int exp_scaled = (int)(63*this.companion.getExp()/this.companion.getMaxExp());

        this.blit(matrixStack, this.leftPos + 2, this.topPos + 72, 173, 98, affectionlv1_scaled, 2);
        if(this.companion.getAffection()>100){
            this.blit(matrixStack, this.leftPos + 2, this.topPos + 72, 173, 96, affectionlv2_scaled, 2);
        }

        this.blit(matrixStack, this.leftPos + 1, this.topPos + 86, 173, 93, exp_scaled, 2);


        this.blit(matrixStack, this.leftPos + 53, this.topPos + 11, 173, 90, morale_scaled, 2);
    }

    /*
        Change Background Gradient color to white-ish for more arknight-looking
         */
    public void renderBackground(MatrixStack matrixStack, int vOffset) {
        if ((this.minecraft != null ? this.minecraft.level : null) != null) {

            this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xcbcbcbC0, 0xC06D6D6D);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent(this, matrixStack));
        } else {
            this.renderDirtBackground(vOffset);
        }

    }

    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.inventory.getDisplayName(), 5, 95, 16777215);
        matrixStack.pushPose();
        float scalerate = 0.8F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.akn_inventory"), 115/scalerate, 8/scalerate, 16777215);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.akn_trust"), 2/scalerate, 65/scalerate, 0x313131);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.morale"), 54/scalerate, 4/scalerate, 0xffffff);
        this.font.draw(matrixStack, new TranslationTextComponent(Integer.toString(this.companion.getLevel())), 9/scalerate, 79/scalerate, 0xffffff);
        this.font.drawShadow(matrixStack, this.companion.getDisplayName().getString(), 1, 5, 16777215);
        matrixStack.popPose();

        matrixStack.pushPose();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        this.font.draw(matrixStack, new StringTextComponent("Lv."), 1/scalerate, 81/scalerate, 0xffffff);

        ITextComponent text = new StringTextComponent("EXP: ").append(new StringTextComponent(Integer.toString((int)this.companion.getExp())).setStyle(Style.EMPTY.withColor(Color.fromRgb(0xf4cf03)))).append(new TranslationTextComponent("/"+(int)this.companion.getMaxExp()).setStyle(Style.EMPTY.withColor(Color.fromRgb(0xffffff))));
        int textwidth = this.font.width(text.getString());
        this.font.draw(matrixStack, text, (64/scalerate)-textwidth, 81/scalerate, 0xffffff);
        this.font.draw(matrixStack, new StringTextComponent((int)this.companion.getAffection()+"%"), 33/scalerate, 68/scalerate, 0x313131);
        matrixStack.popPose();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderEntity(x, y);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.companion.getAmmoStorage().getSlots()>0) {
            this.blit(matrixStack, this.leftPos + this.imageWidth + 4, this.topPos + 9, 214, 0, 42, 87);
        }

        this.blit(matrixStack, this.leftPos-23, this.topPos+7, 114, 7, 20, 10);
        for(int l = 0; l<this.companion.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos-23, this.topPos+17+(18*l), 21, 201, 20, 18);
            if(l == (this.companion.getSkillItemCount()-1)){
                this.blit(matrixStack, this.leftPos-23, this.topPos+17+(18*(l+1)), 21, 201, 20, 4);
            }
        }
        for(int l = 0; l<this.companion.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos-21, this.topPos+20+(18*l), 41, 201, 16, 16);
        }
        matrixStack.popPose();
    }

    private void switchBehavior() {
        if(Screen.hasShiftDown()){
            this.companion.clearHomePos();
        }
        else {
            this.companion.SwitchFreeRoamingStatus();
        }
    }

    private void switchItemBehavior() {
        this.companion.SwitchItemBehavior();
    }

    private void drawButtons(MatrixStack stack, int mousex, int mousey) {

        int homeModeX = this.companion.isFreeRoaming()? 0:10;
        int ItemPickupX = this.companion.shouldPickupItem()? 0:10;

        ImageButton HomeModeButton = new ImageButton(this.leftPos,this.topPos+51,10,10,homeModeX,200,10,TEXTURE, action->switchBehavior());
        ImageButton ItemPickupButton = new ImageButton(this.leftPos,this.topPos+40,10,10,ItemPickupX,220,10,TEXTURE, action->switchItemBehavior());

        if(this.isHovering(0,51,10,10, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.companion.isFreeRoaming()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.freeroaming.on").withStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.freeroaming.off").withStyle(TextFormatting.BLUE));
            }

            if(this.companion.getHOMEPOS().isPresent()) {
                ITextComponent shift = new StringTextComponent("[SHIFT]").withStyle(TextFormatting.YELLOW);
                BlockPos Home = this.companion.getHOMEPOS().get();
                tooltips.add(new TranslationTextComponent("gui.tooltip_homepos").append(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()).withStyle(TextFormatting.BLUE));
                tooltips.add(new TranslationTextComponent("gui.tooltip.shifttoclearhome", shift).withStyle(TextFormatting.GRAY));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.nohome").withStyle(TextFormatting.GRAY));
            }
            this.renderWrappedToolTip(stack, tooltips, mousex, mousey, this.font);
        }
        else if(this.isHovering(0,40,10,10, mousex, mousey)){
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.companion.shouldPickupItem()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.on").withStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.off").withStyle(TextFormatting.BLUE));
            }
            this.renderWrappedToolTip(stack, tooltips, mousex, mousey, this.font);
        }

        this.addButton(HomeModeButton);
        this.addButton(ItemPickupButton);
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.companion.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.companion);
            int entityWidth = (int) entity.getBbWidth();
            try {
                renderEntityInInventory(this.leftPos + 48, this.topPos + 75, 30, mousex, mousey, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

}
