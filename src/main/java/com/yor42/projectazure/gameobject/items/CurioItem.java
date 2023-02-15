package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.intermod.curios.client.CurioRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.CompatibilityUtils.isCurioLoaded;

public class CurioItem extends Item {
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

    public void curioTick(LivingEntity entity, int index, ItemStack stack){}

    public void curioOnEquip(LivingEntity wearer, int index, ItemStack prevstack){}
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public CurioRenderer getSlotRenderer(){
        return null;
    }
}
