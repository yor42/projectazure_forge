package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemBaseTooltip;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import net.minecraft.item.Item.Properties;

public class ItemBonkBat extends ItemBaseTooltip {
    public ItemBonkBat(Properties properties) {
        super(properties);
    }

    //Can't forget that Bonk Sound
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if(entity.isAlive()) {
            entity.hurt(DamageSources.BONK, 25565);
            player.playNotifySound(registerSounds.WEAPON_BONK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }
}
