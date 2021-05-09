package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemAbydos550 extends ItemGunBase{
    public ItemAbydos550(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float accuracy, Properties properties, boolean isTwohanded, Item magazine) {
        super(semiAuto, minFiretime, clipsize, reloadtime, damage, firesound, reloadsound, roundsPerReload, accuracy, properties, isTwohanded, magazine);
    }

    @Override
    protected void SecondaryAction(PlayerEntity playerIn, ItemStack heldItem) {

    }

    @Override
    public enums.AmmoCalibur getCalibur() {
        return enums.AmmoCalibur.AMMO_5_56;
    }
}
