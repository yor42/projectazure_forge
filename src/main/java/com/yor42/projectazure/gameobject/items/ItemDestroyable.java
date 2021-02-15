package com.yor42.projectazure.gameobject.items;

import net.minecraft.item.ItemStack;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentDamage;

public class ItemDestroyable extends itemBaseTooltip{

    protected int MaxHP;

    public ItemDestroyable(Properties properties, int MaxHP) {
        super(properties);
        this.MaxHP = MaxHP;
    }

    public int getMaxHP(){
        return this.MaxHP;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return getCurrentDamage(stack) / (double)this.getMaxHP();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
