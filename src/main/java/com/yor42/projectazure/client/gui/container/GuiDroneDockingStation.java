package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDroneDockingStation;
import com.yor42.projectazure.libs.ItemFilterEntry;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiDroneDockingStation extends ContainerScreen<ContainerDroneDockingStation> {

    private final List<ItemFilterEntry> load_filters = new ArrayList<>();
    private final List<ItemFilterEntry> unload_filters = new ArrayList<>();
    private TextFieldWidget filterID;
    private ItemFilterEntry editingEntry;

    private static final ResourceLocation BG_NORMAL = ResourceUtils.ModResourceLocation("textures/gui/dronedockingstation.png");
    private static final ResourceLocation BG_FILTEREDIT = ResourceUtils.ModResourceLocation("textures/gui/dronedockingstation_filterconf.png");

    public GuiDroneDockingStation(ContainerDroneDockingStation p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
        this.imageWidth = 256;
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
        this.filterID = new TextFieldWidget(this.font, this.leftPos+80, this.topPos+57, 91,18, new StringTextComponent("ID"));
        this.filterID.setTextColor(-1);
        this.filterID.setTextColorUneditable(-1);
        this.filterID.setBordered(true);
        this.filterID.setMaxLength(35);
        this.filterID.setResponder(this::onNameChanged);
    }

    private void onNameChanged(String s) {

    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

        if(this.menu.getScreenMode() == ContainerDroneDockingStation.ScreenMode.EDITFILTER){
            this.minecraft.getTextureManager().bind(BG_FILTEREDIT);
        }
        else{
            this.minecraft.getTextureManager().bind(BG_NORMAL);
        }
        this.blit(p_230450_1_, this.leftPos, this.topPos, 0,0, this.imageWidth, this.imageHeight);
    }
}
