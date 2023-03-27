package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.interfaces.IItemDestroyable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getCurrentDamage;

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
    public int getBarWidth(ItemStack pStack) {
        return (int) (13*getCurrentDamage(pStack) / (double)this.getMaxHP());
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }
}
