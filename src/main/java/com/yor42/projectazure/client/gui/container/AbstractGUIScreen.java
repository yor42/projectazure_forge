package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.AbstractContainerInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;

public abstract class AbstractGUIScreen<T extends AbstractContainerInventory> extends ContainerScreen<T> {

    protected final AbstractEntityCompanion host;

    protected final Button.ITooltip FREEROAM_TOOLTIP;

    protected final Button.ITooltip ITEM_TOOLTIP;
    public AbstractGUIScreen(T p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.host = p_i51105_1_.companion;
        this.FREEROAM_TOOLTIP =  (p_238488_0_, matrixStack, p_238488_2_, p_238488_3_) -> {
            List<IFormattableTextComponent> tooltips = new ArrayList<>();

            if(this.host.isFreeRoaming()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.freeroaming.on").withStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.freeroaming.off").withStyle(TextFormatting.BLUE));
            }

            if(this.host.getHOMEPOS().isPresent()) {
                BlockPos Home = this.host.getHOMEPOS().get();
                tooltips.add(new TranslationTextComponent("gui.tooltip.homepos").append(": " + Home.getX() + " / " + Home.getY() + " / " + Home.getZ()));
            }else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.homemode.nohome").withStyle(TextFormatting.GRAY));
            }
            this.renderWrappedToolTip(matrixStack, tooltips, p_238488_2_, p_238488_3_, this.font);
        };

        this.ITEM_TOOLTIP = (p_238488_0_, matrixStack, p_238488_2_, p_238488_3_) -> {
            List<IFormattableTextComponent> tooltips = new ArrayList<>();
            if(this.host.shouldPickupItem()){
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.on").withStyle(TextFormatting.GREEN));
            }
            else{
                tooltips.add(new TranslationTextComponent("gui.tooltip.itempickup.off").withStyle(TextFormatting.BLUE));
            }
            this.renderWrappedToolTip(matrixStack, tooltips, p_238488_2_, p_238488_3_, this.font);
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
    public void render(MatrixStack matrixstacck, int mousex, int mousey, float partialticks) {
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
    protected void rendergauges(MatrixStack matrixStack, int mousex, int mousey){

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

}
