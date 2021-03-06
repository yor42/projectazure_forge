package com.yor42.projectazure.gameobject.items.gun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ItemAbydos550 extends ItemGunBase{
    public ItemAbydos550(boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int roundsPerReload, float accuracy, Properties properties) {
        super(semiAuto, minFiretime, clipsize, reloadtime, damage, firesound, reloadsound, roundsPerReload, accuracy, properties);
    }

    @Override
    protected void SecondaryAction(PlayerEntity playerIn, ItemStack heldItem) {

    }

    @Override
    protected void doReloadAnimation(AnimationController controller) {
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.reload", false));
    }

    @Override
    protected void doFireAnimation(AnimationController controller) {
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.fire", false));
    }
}
