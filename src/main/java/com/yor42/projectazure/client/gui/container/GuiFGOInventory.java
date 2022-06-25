package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerFGOInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IFGOServant;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;
import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiFGOInventory extends ContainerScreen<ContainerFGOInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/fgo_inventory.png");
    private final AbstractEntityCompanion host;
    private boolean populated = false;

    public GuiFGOInventory(ContainerFGOInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = p_i51105_1_.companion;
        this.imageWidth = 210;
        this.imageHeight = 228;
    }

    @Override
    protected void init() {
        super.init();
        this.populated = false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButton();
        this.renderEntity(mouseX, mouseY);
        this.rendergauges(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            try {
                renderEntityInInventory(this.leftPos + 39, this.topPos + 101, 40, mousex, mousey, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        matrixStack.pushPose();
        float renderscale = 0.5F;
        matrixStack.scale(renderscale, renderscale, renderscale);
        this.blit(matrixStack, (int) ((this.leftPos+32)/renderscale), (int) ((this.topPos+111)/renderscale), 227, 227, 28, 28);
        matrixStack.popPose();

    }

    protected void rendergauges(MatrixStack stack, int mouseX, int mouseY){
        this.blit(stack, this.topPos+98, this.leftPos+160, 0, 253, 50, 3);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.draw(p_230451_1_, this.inventory.getDisplayName(), 26,136.25F,0x212121);
        this.font.draw(p_230451_1_, new TranslationTextComponent("gui.servantinventory"), 81,79.25F,0x212121);
        this.font.draw(p_230451_1_, new TranslationTextComponent("gui.servantprofile"), 81,4.25F,0x212121);

        p_230451_1_.pushPose();
        float renderscale = 0.75F;
        p_230451_1_.scale(renderscale, renderscale, renderscale);
        this.font.draw(p_230451_1_, "Lv.", 83/renderscale,28.25F/renderscale,0xf5c719);

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



        this.font.draw(p_230451_1_, this.host.getLevel()+"/"+this.host.getMaxLevel(), 96,26.25F,0xffffff);
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
    private void switchAttackBehavior() {
        this.host.SwitchPassiveAttack();
    }

    private void renderButton(){
        if(!this.populated) {
            int homeModeX = this.host.isFreeRoaming() ? 228 : 242;
            int itemModeX = this.host.shouldPickupItem() ? 228 : 242;
            int attackmodeX = this.host.shouldAttackFirst() ? 228 : 242;
            Button homebutton = new ImageButton(this.leftPos + 4, this.topPos + 16, 14, 14, homeModeX, 0, 14, TEXTURE, action -> switchBehavior());
            Button itembutton = new ImageButton(this.leftPos + 4, this.topPos + 31, 14, 14, itemModeX, 28, 14, TEXTURE, action -> switchItemBehavior());
            Button attackbehaviorbutton = new ImageButton(this.leftPos + 4, this.topPos + 46, 14, 14, attackmodeX, 56, 14, TEXTURE, action -> switchAttackBehavior());

            this.addButton(homebutton);
            this.addButton(itembutton);
            this.addButton(attackbehaviorbutton);
            this.populated = true;
        }
    }
}
