package com.yor42.projectazure.interfaces;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.enums;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

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
    SoundEvent getNormalAmbientSound();
    SoundEvent getBondlevel1Sound();
    SoundEvent getBondlevel2Sound();
    SoundEvent getBondlevel3Sound();
    SoundEvent getBondlevel4Sound();
    SoundEvent getBondlevel5Sound();

    @Nonnull
    ItemStack createWeaponStack();
}