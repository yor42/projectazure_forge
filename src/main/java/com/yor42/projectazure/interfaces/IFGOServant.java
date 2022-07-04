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

import javax.annotation.Nonnull;

import static net.minecraft.util.Hand.MAIN_HAND;

public interface IFGOServant {
    enums.SERVANT_CLASSES getServantClass();

    default boolean SwitchItem(){
        if(this instanceof AbstractEntityCompanion) {
            ItemStack MainHandItem = (((AbstractEntityCompanion) this).getMainHandItem());
            if (MainHandItem.isEmpty()) {
                ((AbstractEntityCompanion) this).setItemInHand(MAIN_HAND, this.createWeaponStack());
                return true;
            }

            for (int i = 0; i < ((AbstractEntityCompanion) this).getInventory().getSlots(); i++) {
                if (((AbstractEntityCompanion) this).getInventory().insertItem(i, MainHandItem, true).isEmpty()) {
                    ((AbstractEntityCompanion) this).getInventory().insertItem(i, MainHandItem, false);
                    ((AbstractEntityCompanion) this).setItemInHand(MAIN_HAND, this.createWeaponStack());
                    ((AbstractEntityCompanion) this).setItemSwapIndexMainHand(i);
                    return true;
                }
            }
        }
        return false;
    }
    @Nonnull
    ItemStack createWeaponStack();
}
