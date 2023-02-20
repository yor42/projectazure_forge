package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.intermod.curios.CuriosCompat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.CompatibilityUtils.isCurioLoaded;

public class CurioItem extends Item implements ICurioItem {
    public CurioItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(isCurioLoaded()){
            return CuriosCompat.addCapability(stack);
        }
        return super.initCapabilities(stack, nbt);
    }
}
