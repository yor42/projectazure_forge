package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.IItemDestroyable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentDamage;

import net.minecraft.item.Item.Properties;

public class ItemDestroyable extends Item implements IItemDestroyable {

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
