package com.yor42.projectazure.gameobject.items.gun;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ItemAbydos550 extends ItemGunBase{
    public ItemAbydos550(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float inaccuracy, Properties properties, boolean isTwohanded, Item magazine) {
        super(semiAuto, minFiretime, clipsize, reloadtime, damage, firesound, reloadsound, roundsPerReload, inaccuracy, properties, isTwohanded, magazine, enums.GunClass.AR);
    }

    @Override
    protected void SecondaryAction(PlayerEntity playerIn, ItemStack heldItem) {

    }

    @Override
    public enums.AmmoCalibur getAmmoType() {
        return enums.AmmoCalibur.AMMO_5_56;
    }

    @Override
    public void onAnimationSync(int id, int state) {

        if (state == FIRING) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("fire", false));
        }
        else if(state == RELOADING) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("reload", false));
        }
    }
}
