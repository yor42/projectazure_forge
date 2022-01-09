package com.yor42.projectazure.gameobject.entity.companion.magicuser;

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
    boolean shouldUseSpell();
    void ShootProjectile(World world, @Nonnull LivingEntity target);
    void StartShootingEntity(LivingEntity target);
}
