package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemBaseTooltip;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemBonkBat extends ItemBaseTooltip {
    public ItemBonkBat(Properties properties) {
        super(properties);
    }

    //Can't forget that Bonk Sound
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(entity.isAlive()) {
            entity.hurt(DamageSources.BONK, 25565);
            player.playNotifySound(registerSounds.WEAPON_BONK, SoundSource.PLAYERS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }
}
