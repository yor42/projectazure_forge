package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMagazine extends ItemBaseTooltip{

    private enums.AmmoCalibur calibur;
    private int Ammocount;

    public ItemMagazine(enums.AmmoCalibur calibur, int AmmoCount, Properties properties) {
        super(properties);
        this.calibur = calibur;
        this.Ammocount = AmmoCount;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (float)stack.getOrCreateTag().getInt("usedAmmo")/this.Ammocount;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    public int getMagCap() {
        return this.Ammocount;
    }

    public enums.AmmoCalibur getCalibur() {
        return this.calibur;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").appendString(": "+ stack.getOrCreateTag().getInt("usedAmmo")+"/"+this.Ammocount));
    }
}
