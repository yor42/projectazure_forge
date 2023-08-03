package com.yor42.projectazure.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IActivatable {
    void onSwitchOn(ItemStack stack, LivingEntity wearer);
    void onSwitchOff(ItemStack stack, LivingEntity wearer);
    void onTick(ItemStack stack, LivingEntity wearer, boolean powerstatus);
}
