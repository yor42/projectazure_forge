package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerPCRInventory;
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

public class GuiPCRInventory extends ContainerScreen<ContainerPCRInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/pcr_inventory.png");
    private final AbstractEntityCompanion host;
    private boolean populated = false;

    public GuiPCRInventory(ContainerPCRInventory p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = p_i51105_1_.companion;
        this.imageWidth = 210;
        this.imageHeight = 228;
        this.inventoryLabelX = 24;
        this.inventoryLabelY= 136;
        this.titleLabelX = 114;
        this.titleLabelY= 57;
    }

    @Override
    protected void init() {
        super.init();
        this.populated = false;
    }

    @Override
    protected void renderLabels(MatrixStack stack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0x40424f);
        this.font.draw(stack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x40424f);

        float renderscale = 0.7F;
        stack.pushPose();
        stack.scale(renderscale, renderscale, renderscale);
        ITextComponent string = this.host.getDisplayName();
        float width = (this.font.width(string)*renderscale)/2F;
        this.font.draw(stack, this.host.getDisplayName(), (55-width)/renderscale, 14/renderscale, 0xf3f2f8);

        this.font.draw(stack, new TranslationTextComponent("gui.pcr.level"), 15/renderscale, 103/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslationTextComponent("gui.pcr.affection"), 15/renderscale, 116/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslationTextComponent("gui.pcr.morale"), 57/renderscale, 103/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslationTextComponent("gui.pcr.hunger"), 57/renderscale, 116/renderscale, 0xedf8ff);
        stack.popPose();
        stack.pushPose();
        renderscale = 0.5F;
        stack.scale(renderscale, renderscale, renderscale);
        string = new StringTextComponent(this.host.getLevel()+"/"+this.host.getMaxLevel());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (54-width)/renderscale, 104/renderscale, 0x4e5460);
        string = new StringTextComponent((int)this.host.getAffection()+"/"+(int)this.host.getmaxAffection());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (54-width)/renderscale, 117/renderscale, 0x4e5460);
        string = new StringTextComponent((int)this.host.getMorale()+"/"+(int)this.host.getMaxMorale());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (99-width)/renderscale, 104/renderscale, 0x4e5460);
        string = new StringTextComponent(this.host.getFoodStats().getFoodLevel()+"/20");
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (99-width)/renderscale, 117/renderscale, 0x4e5460);
        stack.popPose();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderEntity(mouseX, mouseY);
        this.renderButton();
        this.rendergauges(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    private void rendergauges(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
    }

    private void renderButton() {
        if(!this.populated) {
            Button homebutton = new EntityStatusButton(this.host, this.leftPos + 183, this.topPos + 71, 16, 16, 240, 0, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM);
            Button itembutton = new EntityStatusButton(this.host,this.leftPos + 183, this.topPos + 89, 16, 16, 240, 32, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM);
            Button attackbehaviorbutton = new EntityStatusButton(this.host, this.leftPos + 183, this.topPos + 107, 16, 16, 240, 64, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.DEFENCE);

            this.addButton(homebutton);
            this.addButton(itembutton);
            this.addButton(attackbehaviorbutton);
            this.populated = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            try {
                renderEntityInInventory(this.leftPos + 55, this.topPos + 74, 20, mousex, mousey, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        p_230450_1_.pushPose();
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(p_230450_1_, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        p_230450_1_.popPose();
    }
}
