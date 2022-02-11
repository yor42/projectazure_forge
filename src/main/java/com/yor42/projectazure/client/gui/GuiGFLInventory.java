package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerGFLInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiGFLInventory extends ContainerScreen<ContainerGFLInventory> implements IHasContainer<ContainerGFLInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/gfl_inventory.png");
    private final AbstractEntityCompanion host;
    private final double affection, morale;

    public GuiGFLInventory(ContainerGFLInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = screenContainer.companion;
        this.affection = this.host.getAffection();
        this.morale = this.host.getMorale();
        this.imageWidth = 170;
        this.imageHeight = 188;
    }

    @OnlyIn(Dist.CLIENT)
    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            int entityWidth = (int) entity.getBbWidth();
            try {
                InventoryScreen.renderEntityInInventory(this.leftPos + (46 - (entityWidth / 2)), this.topPos + 75, 30, mousex * -1 + leftPos + (53 - entityWidth / 2), mousey * -1 + this.topPos + 70, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderEntity(mouseX, mouseY);
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButton();
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
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
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {

        matrixStack.pushPose();
        float scalerate = 0.75F;
        matrixStack.scale(scalerate,scalerate,scalerate);

        String AffectionString = String.format("%,.2f", this.host.getAffection())+"/"+this.host.getmaxAffection();
        int affectionwidth = (int) ((this.font.width(AffectionString)+8)*scalerate);
        this.font.draw(matrixStack, new StringTextComponent(AffectionString), (140-((float)affectionwidth/2))/scalerate, 77/scalerate, 0x242424);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, (int) (((133)-((float)affectionwidth/2))/scalerate), (int) (77/scalerate), 203, 72, 7, 7);

        int textwidth = (int) (this.font.width(this.host.getDisplayName().getString())*scalerate);
        this.font.draw(matrixStack, this.host.getDisplayName(), (42-textwidth)/scalerate, 81/scalerate, 0x242424);
        this.font.draw(matrixStack, new StringTextComponent("Lv."+this.host.getLevel()), 43/scalerate, 82/scalerate, 0x242424);
        matrixStack.popPose();

        matrixStack.pushPose();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        String MaxHP = "MAX "+(int)this.host.getMaxHealth();
        int maxHPLength = (int) (this.font.width(MaxHP)*scalerate);
        ITextComponent GunClass = new TranslationTextComponent(this.host.getGunSpecialty().getName());
        int gunNameLength = (int) (this.font.width(GunClass.getString())*scalerate);
        this.font.draw(matrixStack, new StringTextComponent(MaxHP), (89-maxHPLength)/scalerate, (float) (92.5/scalerate), 0x7e8552);
        this.font.draw(matrixStack, new StringTextComponent("HP "+ (int) this.host.getHealth()), 48/scalerate, (float) (92.5/scalerate), 0x242424);
        this.font.draw(matrixStack, new StringTextComponent("FD "+ this.host.getFoodStats().getFoodLevel()), 48/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.draw(matrixStack, new StringTextComponent("MR "+ (int) this.host.getMorale()), 72/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.draw(matrixStack, GunClass, (41-gunNameLength)/scalerate, 88.5F/scalerate, 0x242424);
        matrixStack.popPose();
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

    private void renderButton(){
        int homeModeX = this.host.isFreeRoaming()? 1:15;
        int itemModeX = this.host.shouldPickupItem()? 1:15;
        Button homebutton = new ImageButton(this.leftPos+1, this.topPos+64, 14,14, homeModeX, 188, 14,TEXTURE, action->switchBehavior());
        Button itembutton = new ImageButton(this.leftPos+16, this.topPos+64, 14,14, itemModeX, 216, 14,TEXTURE, action->switchItemBehavior());

        this.addButton(homebutton);
        this.addButton(itembutton);
    }
}
