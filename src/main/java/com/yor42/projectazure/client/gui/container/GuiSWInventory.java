package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.client.gui.buttons.EntityStatusButton;
import com.yor42.projectazure.gameobject.containers.entity.ContainerSWInventory;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiSWInventory extends AbstractGUIScreen<ContainerSWInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/sw_inventory.png");

    public GuiSWInventory(ContainerSWInventory p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 225;
        this.imageHeight = 240;

        this.titleLabelX = 115;
        this.titleLabelY = 10;

        this.inventoryLabelX = 33;
        this.inventoryLabelY = 147;
    }

    protected void renderLabels(PoseStack pMatrixStack, int pX, int pY) {
        this.font.draw(pMatrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xf5ba5f);
        this.font.draw(pMatrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(pMatrixStack, 16, 15, 0, 251, this.getHPgauge(68), 5);
        this.blit(pMatrixStack, 41, 131, 0, 245, this.getEXPgauge(71), 3);
        int gaugepixel = this.getMoralegauge(70);

        this.blit(pMatrixStack, 113+(70-gaugepixel), 131, 0, 245, gaugepixel, 3);
        pMatrixStack.pushPose();
        float scale = 0.5F;
        pMatrixStack.scale(scale,scale,scale);
        Component text = new TextComponent(this.host.getHealth()+"/"+this.host.getMaxHealth());
        float width = this.font.width(text)*scale;
        this.font.draw(pMatrixStack, String.format("EXP: %.02f", this.host.getExp()/this.host.getMaxExp()*100)+"%", 9/scale, 125/scale, 0xf5ba5f);
        this.font.draw(pMatrixStack, new TextComponent("LV."+this.host.getEntityLevel()+" ").append(this.host.getDisplayName()), 16/scale, 8/scale, 0xffffff);
        this.font.draw(pMatrixStack, text, (84-width)/scale, 18/scale, 4210752);

        text = new TextComponent(String.format("Morale: %.02f", this.host.getMorale()/this.host.getMaxMorale()*100)+"%");
        width = this.font.width(text)*scale;
        this.font.draw(pMatrixStack, text, (216-width)/scale, 125/scale, 0xf5ba5f);


        pMatrixStack.popPose();

    }

    @Override
    protected void addButtons() {
        Button homebutton = new EntityStatusButton(this.host, this.leftPos + 76, this.topPos + 77, 14, 14, 242, 172, -14, 14, TEXTURE, EntityStatusButton.ACTIONTYPES.FREEROAM, FREEROAM_TOOLTIP);
        Button itembutton = new EntityStatusButton(this.host,this.leftPos + 76, this.topPos + 93, 14, 14, 242, 200, -14, 14, TEXTURE, EntityStatusButton.ACTIONTYPES.ITEM, ITEM_TOOLTIP);
        Button attackbehaviorbutton = new EntityStatusButton(this.host, this.leftPos + 76, this.topPos + 109, 14, 14, 228, 228, 14, 14, TEXTURE, EntityStatusButton.ACTIONTYPES.DEFENCE, DEFENCE_TOOLTIP);

        this.addButton(homebutton);
        this.addButton(itembutton);
        this.addButton(attackbehaviorbutton);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        matrixStack.popPose();
        this.renderEntity(this.leftPos+46, this.topPos+112, x, y);
    }
}
