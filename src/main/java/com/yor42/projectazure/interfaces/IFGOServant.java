package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.items.IItemHandler;

import static net.minecraft.util.Hand.MAIN_HAND;

public interface IFGOServant {
    enums.SERVANT_CLASSES getServantClass();

    default boolean SwitchItem(){
        ItemStack MainHandItem = this.getMainHandItem();
        if(MainHandItem.isEmpty()){
            this.setItemInHand(MAIN_HAND, new ItemStack(registerItems.EXCALIBUR.get()));
            return true;
        }

        for(int i=0;i<this.getInventory().getSlots(); i++){
            if(this.getInventory().insertItem(i, MainHandItem, true).isEmpty()){
                this.getInventory().insertItem(i, MainHandItem, false);
                this.setItemInHand(MAIN_HAND, new ItemStack(registerItems.EXCALIBUR.get()));
                this.setItemSwapIndexMainHand(i);
                return true;
            }
        }

        return false;
    }

    void setItemSwapIndexMainHand(int i);

    IItemHandler getInventory();

    void setItemInHand(Hand mainHand, ItemStack stack);

    ItemStack getMainHandItem();
}
