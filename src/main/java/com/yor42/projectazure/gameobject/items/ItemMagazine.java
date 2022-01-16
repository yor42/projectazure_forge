package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            ItemStackUtils.setAmmoFull(stack);
            items.add(stack);
        }
    }

    @Override
    public void addInformationAfterShift(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformationAfterShift(stack, worldIn, tooltip, flagIn);
        TextFormatting color;

        if(((float)getRemainingAmmo(stack)/this.Ammocount)<0.3F){
            color = TextFormatting.RED;
        }
        else if(((float)getRemainingAmmo(stack)/this.Ammocount)<0.6F){
            color = TextFormatting.YELLOW;
        }
        else{
            color = TextFormatting.GREEN;
        }

        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").append(": ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(getRemainingAmmo(stack)+"/"+this.Ammocount).withStyle(color)));
    }
}
