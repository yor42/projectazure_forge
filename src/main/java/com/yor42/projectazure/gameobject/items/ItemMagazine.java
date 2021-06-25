package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.ICraftingTableReloadable;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
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

    public enums.AmmoCalibur getCalibur() {
        return this.calibur;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.tooltip.remainingammo").appendString(": "+ getRemainingAmmo(stack)+"/"+this.Ammocount));
    }
}
