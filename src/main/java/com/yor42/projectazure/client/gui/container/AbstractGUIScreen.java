package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.AbstractContainerInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;

public abstract class AbstractGUIScreen<T extends AbstractContainerInventory> extends AbstractContainerScreen<T> {

    protected final AbstractEntityCompanion host;

    protected final Button.OnTooltip FREEROAM_TOOLTIP;
    protected final Button.OnTooltip DEFENCE_TOOLTIP;
    protected final Button.OnTooltip ITEM_TOOLTIP;
    public AbstractGUIScreen(T p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = p_i51105_1_.companion;
        this.FREEROAM_TOOLTIP =  (p_238488_0_, matrixStack, p_238488_2_, p_238488_3_) -> {
            List<MutableComponent> tooltips = new ArrayList<>();

            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslatableComponent("gui.tooltip.freeroaming.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.freeroaming.off").withStyle(ChatFormatting.BLUE));
            }

            if(this.host.getHOMEPOS().isPresent()) {
                BlockPos Home = this.host.getHOMEPOS().get();
                tooltips.add(new TranslatableComponent("gui.tooltip.homepos").append(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()));
            }else{
                tooltips.add(new TranslatableComponent("gui.tooltip.homemode.nohome").withStyle(ChatFormatting.GRAY));
            }
            this.renderComponentTooltip(matrixStack, tooltips, p_238488_2_, p_238488_3_, this.font);
        };

        this.DEFENCE_TOOLTIP = (p_238488_0_, matrixStack, p_238488_2_, p_238488_3_) -> {
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.shouldAttackFirst()){
                tooltips.add(new TranslatableComponent("gui.tooltip.aggressive.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.aggressive.off").withStyle(ChatFormatting.BLUE));
            }
            this.renderComponentTooltip(matrixStack, tooltips, p_238488_2_, p_238488_3_, this.font);
        };

        this.ITEM_TOOLTIP = (p_238488_0_, matrixStack, p_238488_2_, p_238488_3_) -> {
            List<MutableComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.on").withStyle(ChatFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslatableComponent("gui.tooltip.itempickup.off").withStyle(ChatFormatting.BLUE));
            }
            this.renderComponentTooltip(matrixStack, tooltips, p_238488_2_, p_238488_3_, this.font);
        };
    }

    protected void switchBehavior() {
        if(Screen.hasShiftDown()){
            this.host.clearHomePos();
        }
        else {
            this.host.SwitchFreeRoamingStatus();
        }
    }

    @Override
    public void render(PoseStack matrixstacck, int mousex, int mousey, float partialticks) {
        this.renderBackground(matrixstacck);
        super.render(matrixstacck, mousex, mousey, partialticks);
        this.rendergauges(matrixstacck, mousex, mousey);
        this.renderTooltip(matrixstacck, mousex, mousey);
    }

    protected void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderEntity(int x, int y, int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientUtils.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.restoreFrom(this.host);
            try {
                renderEntityInInventory(x, y, 30, mousex, mousey, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }
    protected void rendergauges(PoseStack matrixStack, int mousex, int mousey){

    }

    @Override
    protected void init() {
        super.init();
        this.addButtons();
    }

    protected void addButtons(){
    }

    protected int getEXPgauge(int fullpixels){
        return (int) (fullpixels*this.host.getExp()/this.host.getMaxExp());
    }

    protected int getMoralegauge(int fullpixels){
        return (int) (fullpixels*this.host.getMorale()/this.host.getMaxMorale());
    }

    protected int getHPgauge(int fullpixels){
        return (int) (fullpixels*this.host.getHealth()/this.host.getMaxHealth());
    }

}
