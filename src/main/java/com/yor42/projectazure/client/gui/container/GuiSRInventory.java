package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerShiningResonanceInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;
import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiSRInventory extends AbstractGUIScreen<ContainerShiningResonanceInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/shiningresonance_inventory.png");

    public GuiSRInventory(ContainerShiningResonanceInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 174;
        this.imageHeight = 232;
        this.inventoryLabelX = 6;
        this.inventoryLabelY= 137;
        this.titleLabelX = 114;
        this.titleLabelY= 57;
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xfeffff);

        matrixStack.pushPose();
        float renderscale = 0.75F;
        matrixStack.scale(renderscale,renderscale,renderscale);
        this.font.draw(matrixStack, new StringTextComponent(String.valueOf(this.host.getLevel())),6/renderscale, 18.5F/renderscale, 0xfeffff);
        matrixStack.popPose();

        matrixStack.pushPose();
        renderscale = 0.5F;
        matrixStack.scale(renderscale,renderscale,renderscale);
        this.font.draw(matrixStack, new StringTextComponent("LEVEL"),6/renderscale, 13/renderscale, 0xfeffff);
        this.font.draw(matrixStack, new StringTextComponent("Name  ").append(this.host.getDisplayName()),36/renderscale, 10.5F/renderscale, 0xfeffff);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.sr.atk"),10/renderscale, 40/renderscale, 0xfeffff);
        String value = String.valueOf(((int)this.host.getAttackDamageMainHand()));
        float txtwidth = this.font.width(value)*renderscale;
        this.font.draw(matrixStack, new StringTextComponent(value),(44-txtwidth)/renderscale, 40/renderscale, 0xfeffff);

        this.font.draw(matrixStack, new TranslationTextComponent("gui.sr.def"),10/renderscale, 48/renderscale, 0xfeffff);

        value = String.valueOf(this.host.getArmorValue());
        txtwidth = this.font.width(value)*renderscale;
        this.font.draw(matrixStack, new StringTextComponent(value),(44-txtwidth)/renderscale, 48/renderscale, 0xfeffff);

        this.font.draw(matrixStack, new StringTextComponent("Next Level  "+((int)this.host.getMaxExp()-(int)this.host.getExp())),42/renderscale, 28/renderscale, 0xfeffff);
        this.font.drawShadow(matrixStack, new StringTextComponent("Hp  "+(int)this.host.getHealth()+"/"+(int)this.host.getMaxHealth()),36/renderscale, 19.5F/renderscale, 0xfeffff);
        this.font.drawShadow(matrixStack, new StringTextComponent("Mr  "+(int)this.host.getHealth()+"/"+(int)this.host.getMaxHealth()),68/renderscale, 19.5F/renderscale, 0xfeffff);
        matrixStack.popPose();

    }

    protected void rendergauges(MatrixStack matrixStack, int mouseX, int mouseY) {

        matrixStack.pushPose();
        this.minecraft.getTextureManager().bind(TEXTURE);
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

        this.addButton(homebutton);
        this.addButton(itembutton);
        this.addButton(attackbehaviorbutton);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(p_230450_1_, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
