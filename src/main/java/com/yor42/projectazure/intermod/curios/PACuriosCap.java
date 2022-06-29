package com.yor42.projectazure.intermod.curios;

import com.yor42.projectazure.gameobject.items.CurioItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class PACuriosCap implements ICurio {

    private final ItemStack stack;
    public PACuriosCap(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        this.getItem().curioTick(livingEntity, index, this.stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        this.getItem().curioOnEquip(slotContext.getWearer(), slotContext.getIndex(), prevStack);
    }

    public CurioItem getItem(){
        if(stack.getItem() instanceof CurioItem){
            return (CurioItem) stack.getItem();
        }
        else{
            throw new IllegalStateException("Item attached to CuriosCapability must be instance of CurioItem!");
        }
    }
}
