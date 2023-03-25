package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.containers.entity.ContainerGFLInventory;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiGFLInventory extends AbstractGUIScreen<ContainerGFLInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/gfl_inventory.png");
    private final double affection, morale;

    public GuiGFLInventory(ContainerGFLInventory screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.affection = this.host.getAffection();
        this.morale = this.host.getMorale();
        this.imageWidth = 170;
        this.imageHeight = 188;
    }
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.pushPose();
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        for(int l = 0; l<host.getSkillItemCount(); l++){
            this.blit(matrixStack, this.leftPos + 96, this.topPos + 4+l*18, 153, 189, 16,16);
        }

        this.blit(matrixStack, this.leftPos + 93, this.topPos + 75, 173, 88, (int) (76*(this.affection/this.host.getmaxAffection())), 9);
        this.blit(matrixStack, this.leftPos+43, this.topPos+89, 173, 85,  (int) (45*(this.host.getExp()/this.host.getMaxExp())), 2);
        this.blit(matrixStack, this.leftPos+176, this.topPos+7, 173, 109, 42, 78);
        matrixStack.popPose();

        matrixStack.pushPose();
        float renderscale = (float) 5/9;
        matrixStack.scale(renderscale, renderscale, renderscale);
        this.blit(matrixStack, (int) ((this.leftPos+43)/renderscale), (int) ((this.topPos+92)/renderscale), 246, 11, 9, 9);
        this.blit(matrixStack, (int) ((this.leftPos+43)/renderscale), (int) ((this.topPos+98)/renderscale), 236, 11, 9, 9);
        this.blit(matrixStack, (int) ((this.leftPos+67)/renderscale), (int) ((this.topPos+98)/renderscale), 246, 11, 9, 9);
        matrixStack.popPose();
        this.renderEntity(this.leftPos + 46, this.topPos + 75, x, y);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {

        matrixStack.pushPose();
        float scalerate = 0.75F;
        matrixStack.scale(scalerate,scalerate,scalerate);

        String AffectionString = String.format("%,.2f", this.host.getAffection())+"/"+this.host.getmaxAffection();
        int affectionwidth = (int) ((this.font.width(AffectionString)+8)*scalerate);
        this.font.draw(matrixStack, new TextComponent(AffectionString), (140-((float)affectionwidth/2))/scalerate, 77/scalerate, 0x242424);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, (int) (((133)-((float)affectionwidth/2))/scalerate), (int) (77/scalerate), 203, 72, 7, 7);

        int textwidth = (int) (this.font.width(this.host.getDisplayName().getString())*scalerate);
        this.font.draw(matrixStack, this.host.getDisplayName(), (42-textwidth)/scalerate, 81/scalerate, 0x242424);
        this.font.draw(matrixStack, new TextComponent("Lv."+this.host.getLevel()), 43/scalerate, 82/scalerate, 0x242424);
        matrixStack.popPose();

        matrixStack.pushPose();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        String MaxHP = "MAX "+(int)this.host.getMaxHealth();
        int maxHPLength = (int) (this.font.width(MaxHP)*scalerate);
        Component GunClass = new TranslatableComponent(this.host.getGunSpecialty().getName());
        int gunNameLength = (int) (this.font.width(GunClass.getString())*scalerate);
        this.font.draw(matrixStack, new TextComponent(MaxHP), (89-maxHPLength)/scalerate, (float) (92.5/scalerate), 0x7e8552);
        this.font.draw(matrixStack, new TextComponent("HP "+ (int) this.host.getHealth()), 48/scalerate, (float) (92.5/scalerate), 0x242424);
        this.font.draw(matrixStack, new TextComponent("FD "+ this.host.getFoodStats().getFoodLevel()), 48/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.draw(matrixStack, new TextComponent("MR "+ (int) this.host.getMorale()), 72/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.draw(matrixStack, GunClass, (41-gunNameLength)/scalerate, 88.5F/scalerate, 0x242424);
        matrixStack.popPose();
    }


    protected void addButtons(){
        int homeModeX = this.host.isFreeRoaming()? 1:15;
        int itemModeX = this.host.shouldPickupItem()? 1:15;
        Button homebutton = new ImageButton(this.leftPos+1, this.topPos+64, 14,14, homeModeX, 188, 14,TEXTURE, action->switchBehavior());
        Button itembutton = new ImageButton(this.leftPos+16, this.topPos+64, 14,14, itemModeX, 216, 14,TEXTURE, action->switchItemBehavior());

        this.addButton(homebutton);
        this.addButton(itembutton);
    }
}
