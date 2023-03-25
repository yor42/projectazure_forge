package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerPCRInventory;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiPCRInventory extends AbstractGUIScreen<ContainerPCRInventory> {
    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/pcr_inventory.png");

    public GuiPCRInventory(ContainerPCRInventory p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 210;
        this.imageHeight = 228;
        this.inventoryLabelX = 24;
        this.inventoryLabelY= 136;
        this.titleLabelX = 114;
        this.titleLabelY= 57;
    }

    @Override
    protected void renderLabels(PoseStack stack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0x40424f);
        this.font.draw(stack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x40424f);

        float renderscale = 0.7F;
        stack.pushPose();
        stack.scale(renderscale, renderscale, renderscale);
        Component string = this.host.getDisplayName();
        float width = (this.font.width(string)*renderscale)/2F;
        this.font.draw(stack, this.host.getDisplayName(), (55-width)/renderscale, 14/renderscale, 0xf3f2f8);

        this.font.draw(stack, new TranslatableComponent("gui.pcr.level"), 15/renderscale, 103/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslatableComponent("gui.pcr.affection"), 15/renderscale, 116/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslatableComponent("gui.pcr.morale"), 57/renderscale, 103/renderscale, 0xedf8ff);
        this.font.draw(stack, new TranslatableComponent("gui.pcr.hunger"), 57/renderscale, 116/renderscale, 0xedf8ff);
        stack.popPose();
        stack.pushPose();
        renderscale = 0.5F;
        stack.scale(renderscale, renderscale, renderscale);
        string = new TextComponent(this.host.getEntityLevel()+"/"+this.host.getMaxLevel());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (54-width)/renderscale, 104/renderscale, 0x4e5460);
        string = new TextComponent((int)this.host.getAffection()+"/"+(int)this.host.getmaxAffection());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (54-width)/renderscale, 117/renderscale, 0x4e5460);
        string = new TextComponent((int)this.host.getMorale()+"/"+(int)this.host.getMaxMorale());
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (99-width)/renderscale, 104/renderscale, 0x4e5460);
        string = new TextComponent(this.host.getFoodStats().getFoodLevel()+"/20");
        width = (this.font.width(string)*renderscale);
        this.font.draw(stack, string, (99-width)/renderscale, 117/renderscale, 0x4e5460);
        stack.popPose();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.rendergauges(matrixStack, mouseX, mouseY);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void rendergauges(PoseStack matrixStack, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
    }

    protected void addButtons() {
            Button homebutton = new EntityStatusButton(this.host, this.leftPos + 183, this.topPos + 71, 16, 16, 240, 0, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
            Button itembutton = new EntityStatusButton(this.host,this.leftPos + 183, this.topPos + 89, 16, 16, 240, 32, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM, ITEM_TOOLTIP);
            Button attackbehaviorbutton = new EntityStatusButton(this.host, this.leftPos + 183, this.topPos + 107, 16, 16, 240, 64, -16, 16, TEXTURE, EntityStatusButton.ACTIONTYPES.DEFENCE);

            this.addButton(homebutton);
            this.addButton(itembutton);
            this.addButton(attackbehaviorbutton);
    }

    @Override
    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        p_230450_1_.pushPose();
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(p_230450_1_, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        p_230450_1_.popPose();

        this.renderEntity(this.leftPos + 55, this.topPos + 74,p_230450_3_, p_230450_4_);
    }
}
