package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.CreativeModeTab;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ChatFormatting;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.Level;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class ItemMagazine extends ItemBaseTooltip implements ICraftingTableReloadable {

    private final enums.AmmoCalibur calibur;
    private final int Ammocount;

    public ItemMagazine(enums.AmmoCalibur calibur, int AmmoCount, Properties properties) {
        super(properties);
        this.calibur = calibur;
        this.Ammocount = AmmoCount;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0-(double) getRemainingAmmo(stack)/this.Ammocount;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    public int getMaxAmmo() {
        return this.Ammocount;
    }

    public enums.AmmoCalibur getAmmoType() {
        return this.calibur;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    @Override
    public void addInformationAfterShift(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, ITooltipFlag flagIn) {
        super.addInformationAfterShift(stack, worldIn, tooltip, flagIn);
        ChatFormatting color;

        if(((float)getRemainingAmmo(stack)/this.Ammocount)<0.3F){
            color = ChatFormatting.RED;
        }
        else if(((float)getRemainingAmmo(stack)/this.Ammocount)<0.6F){
            color = ChatFormatting.YELLOW;
        }
        else{
            color = ChatFormatting.GREEN;
        }

        tooltip.add(new TranslatableComponent("item.tooltip.remainingammo").append(": ").withStyle(ChatFormatting.GRAY).append(new TextComponent(getRemainingAmmo(stack)+"/"+this.Ammocount).withStyle(color)));
    }
}
