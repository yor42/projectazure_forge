package com.yor42.projectazure.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface ISpellUser {

    /*
    Delay between Each fire
     */
    int getInitialSpellDelay();
    /*
    Delay of Pre animation before actually firing projectile
     */
    int getProjectilePreAnimationDelay();
    Hand getSpellUsingHand();
    boolean shouldUseSpell(LivingEntity target);

    default int SpellCooldown(){
        return 0;
    }

    void ShootProjectile(World world, @Nonnull LivingEntity target);
    default void StartSpellAttack(LivingEntity target){};
}
