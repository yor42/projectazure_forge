package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerFGOInventory;
import com.yor42.projectazure.interfaces.IFGOServant;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiFGOInventory extends AbstractGUIScreen<ContainerFGOInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/fgo_inventory.png");

    public GuiFGOInventory(ContainerFGOInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 203;
        this.imageHeight = 231;
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderEntity(this.leftPos + 39, this.topPos + 101,mouseX, mouseY);
        this.rendergauges(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        matrixStack.pushPose();
        float renderscale = 0.5F;
        matrixStack.scale(renderscale, renderscale, renderscale);
        if(this.host instanceof IFGOServant) {
            this.blit(matrixStack, (int) ((this.leftPos + 32) / renderscale), (int) ((this.topPos + 111) / renderscale), ((IFGOServant) this.host).getServantClass().getIconx(), ((IFGOServant) this.host).getServantClass().getIcony(), 28, 28);
        }
        matrixStack.popPose();

    }

    protected void rendergauges(MatrixStack stack, int mouseX, int mouseY){
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(stack,this.leftPos+145, this.topPos+28, 0, 253, 50, 3);
        this.blit(stack,this.leftPos+145, this.topPos+28, 0, 250, (int)((this.host.getExp()/this.host.getMaxExp())*50), 3);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.draw(p_230451_1_, this.inventory.getDisplayName(), 26,136.25F,0x212121);
        this.font.draw(p_230451_1_, new TranslationTextComponent("gui.servantinventory"), 81,79.25F,0x212121);
        this.font.draw(p_230451_1_, new TranslationTextComponent("gui.servantprofile"), 81,4.25F,0x212121);

        p_230451_1_.pushPose();
        float renderscale = 0.75F;
        p_230451_1_.scale(renderscale, renderscale, renderscale);
        float leveltextwidth = this.font.width("Lv.")*renderscale;

        ITextComponent bondtext =new TranslationTextComponent("gui.fgo.bond");
        float bondwidth = this.font.width(bondtext)*renderscale;

        this.font.drawShadow(p_230451_1_, "Lv.", 83/renderscale,28.25F/renderscale,0xf5c719);
        this.font.drawShadow(p_230451_1_,  bondtext, 83/renderscale,65.25F/renderscale,0xf5c719);
        ITextComponent HPText = new TranslationTextComponent("gui.fgo.health").withStyle(Style.EMPTY.withColor(Color.fromRgb(0xb1dbda))).append(new StringTextComponent(this.host.getHealth()+"/"+this.host.getMaxHealth()).withStyle(TextFormatting.WHITE));
        ITextComponent MoraleText = new TranslationTextComponent("gui.fgo.morale").withStyle(Style.EMPTY.withColor(Color.fromRgb(0xb1dbda))).append(new StringTextComponent((int)this.host.getMorale()+"/"+(int)this.host.getMaxMorale()).withStyle(TextFormatting.WHITE));
        float HPTextWidth = this.font.width(HPText)*renderscale;
        this.font.drawShadow(p_230451_1_, HPText, 83/renderscale,37.25F/renderscale, -1);
        this.font.drawShadow(p_230451_1_, MoraleText, 141/renderscale,37.25F/renderscale, -1);

        this.font.drawShadow(p_230451_1_, new TranslationTextComponent("gui.fgo.hunger").withStyle(Style.EMPTY.withColor(Color.fromRgb(0xf5c719))).append(new StringTextComponent(this.host.getFoodStats().getFoodLevel()+" / 20").withStyle(TextFormatting.WHITE)), 83/renderscale,47.25F/renderscale, -1);

        if(this.host instanceof IFGOServant){
            ITextComponent text = new TranslationTextComponent(((IFGOServant) this.host).getServantClass().getTranslationkey());
            float width = (this.font.width(text)*renderscale)/2;
            this.font.drawShadow(p_230451_1_, text, (39-width)/renderscale,102/renderscale,0xffffff);
        }
        p_230451_1_.popPose();

        renderscale = 0.6F;
        p_230451_1_.pushPose();
        p_230451_1_.scale(renderscale, renderscale, renderscale);
        this.font.drawShadow(p_230451_1_, new StringTextComponent("ATK").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.ITALIC), 4/renderscale,112F/renderscale,0x97131e);this.font.drawShadow(p_230451_1_, new StringTextComponent("ATK").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.ITALIC), 4/renderscale,112.25F/renderscale,0x9a0e0f);

        ITextComponent text = new StringTextComponent("HP").withStyle(TextFormatting.BOLD).withStyle(TextFormatting.ITALIC);
        float textwidth = this.font.width(text)*renderscale;
        this.font.drawShadow(p_230451_1_, text, (74-textwidth)/renderscale,112F/renderscale,0x1c80b2);
        this.font.drawShadow(p_230451_1_, new StringTextComponent(String.valueOf((int)this.host.getHealth())).withStyle(TextFormatting.BOLD), 51/renderscale,119F/renderscale,0xfafaf8);
        p_230451_1_.popPose();

        renderscale = 0.4F;
        p_230451_1_.pushPose();
        p_230451_1_.scale(renderscale, renderscale, renderscale);
        if(this.host instanceof IFGOServant){
            text = this.host.getDisplayName();
            float width = (this.font.width(text)*renderscale)/2;
            this.font.drawShadow(p_230451_1_, text, (39-width)/renderscale,111.5F/renderscale,0xffffff);
        }
        p_230451_1_.popPose();


        this.font.draw(p_230451_1_, this.host.getLevel()+"/"+this.host.getMaxLevel(), 83+leveltextwidth+10,26.25F,0xffffff);

        this.font.draw(p_230451_1_, (int)this.host.getAffection()+"/"+(int)this.host.getmaxAffection(), 83+bondwidth+10,63.25F,0xffffff);
    }

    protected void addButtons(){
            Button homebutton = new EntityStatusButton(this.host,this.leftPos + 4, this.topPos + 16, 14, 14, 242, 0, -14,14, TEXTURE,EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
            Button itembutton = new EntityStatusButton(this.host,this.leftPos + 4, this.topPos + 31, 14, 14, 242, 28, -14,14, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM, ITEM_TOOLTIP);
            Button attackbehaviorbutton = new EntityStatusButton(this.host,this.leftPos + 4, this.topPos + 46, 14, 14, 242, 56, -14,14, TEXTURE, EntityStatusButton.ACTIONTYPES.DEFENCE);

            this.addButton(homebutton);
            this.addButton(itembutton);
            this.addButton(attackbehaviorbutton);
        }
    }
